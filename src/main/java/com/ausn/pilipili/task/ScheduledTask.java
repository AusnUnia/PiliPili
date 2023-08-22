package com.ausn.pilipili.task;

import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.dao.VideoVoteDao;
import com.ausn.pilipili.entity.VideoVote;
import com.ausn.pilipili.common.constants.RedisConstants;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.*;


/**
 * @Author: 付显贵
 * @DateTime: 2023/7/22 23:15
 * @Description: synchronize redis data to mysql on a regular basis
 */

@Component
public class ScheduledTask
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private VideoVoteDao videoVoteDao;
    @Autowired
    private RedissonClient redissonClient;


    static private final ExecutorService SCHEDULED_TASK_EXECUTOR;

    static
    {
        SCHEDULED_TASK_EXECUTOR=new ThreadPoolExecutor(
                4,4,
                0, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(16),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Scheduled(cron = "0 0 0 * * ?") // 每天零点触发任务
    public void expireKeyAtMidnight()
    {
        RLock lock = redissonClient.getLock(RedisConstants.EXPIRE_KEY_AT_MIDNIGHT_LOCK);
        boolean locked= lock.tryLock();
        if(locked)
        {
            try
            {
                Set<String> keys = stringRedisTemplate.keys( RedisConstants.VIDEO_COIN_TODAY_CACHE_KEY_PREFIX+ "*");
                if(keys!=null&&!keys.isEmpty())
                {
                    stringRedisTemplate.delete(keys);
                }
            }
            finally
            {
                lock.unlock();
            }
        }
    }


    @Scheduled(fixedRate = 180000) //每180秒执行一次
    @Transactional
    public void persistData()
    {
        RLock lock = redissonClient.getLock(RedisConstants.PERSIST_DATA_LOCK);
        boolean locked= lock.tryLock();
        if(locked)
        {
            try
            {
                persistVote(RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX);
                persistVote(RedisConstants.VIDEO_DOWNVOTE_CACHE_KEY_PREFIX);
                persistVote(RedisConstants.VIDEO_NOVOTE_CACHE_KEY_PREFIX);
            }
            finally
            {
                lock.unlock();
            }
        }
    }

    @Transactional
    public void persistVote(String prefix)
    {
        Set<String> keys = stringRedisTemplate.keys(prefix + "*");
        if(keys!=null&&!keys.isEmpty())
        {
            for(String key:keys)
            {
                SCHEDULED_TASK_EXECUTOR.submit( ()->{updateVoteInMysql(prefix,key);} );
            }
        }
    }

    @Transactional
    public void updateVoteInMysql(String prefix,String key)
    {
        String bv=key.substring(prefix.length());
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        int voteNum=members.size();

        //update the vote number in table videos
        if(prefix.equals(RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX))
        {
            if(videoDao.setUpvoteNumByBv(bv,voteNum)==0)
            {
                throw new RuntimeException("Failed to update upvote number in table videos");
            }
        }
        else if(prefix.equals(RedisConstants.VIDEO_DOWNVOTE_CACHE_KEY_PREFIX))
        {
            if(videoDao.setDownvoteNumByBv(bv,voteNum)==0)
            {
                throw new RuntimeException("Failed to update downvote number in table videos");
            }
        }

        //update the vote relationship in table video_votes
        for(String member:members)
        {
            Long userId=Long.valueOf(member);
            VideoVote videoVote = videoVoteDao.getByBvUserId(bv, userId);
            if(videoVote==null)
            {
                videoVote=new VideoVote();
                videoVote.setBv(bv);
                videoVote.setUserId(userId);
            }

            if(prefix.equals(RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX))
            {
                videoVote.setVote(1);
            }
            else if(prefix.equals(RedisConstants.VIDEO_NOVOTE_CACHE_KEY_PREFIX))
            {
                videoVote.setVote(0);
            }
            else
            {
                videoVote.setVote(-1);
            }

            if(videoVoteDao.update(videoVote)==0)
            {
                throw new RuntimeException("Failed to update vote relationship in table video_votes");
            }
        }
    }

}
