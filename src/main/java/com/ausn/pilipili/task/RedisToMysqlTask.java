package com.ausn.pilipili.task;

import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.dao.VideoVoteDao;
import com.ausn.pilipili.entity.VideoVote;
import com.ausn.pilipili.utils.constants.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Set;


/**
 * @Author: 付显贵
 * @DateTime: 2023/7/22 23:15
 * @Description: synchronize redis data to mysql on a regular basis
 */

@Component
public class RedisToMysqlTask
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private VideoVoteDao videoVoteDao;


    @Scheduled(fixedRate = 10000) //每10秒执行一次
    @Transactional
    public void persistData()
    {
        persistVote(RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX);
        persistVote(RedisConstants.VIDEO_DOWNVOTE_CACHE_KEY_PREFIX);
        persistVote(RedisConstants.VIDEO_NOVOTE_CACHE_KEY_PREFIX);
    }

    private void persistVote(String prefix)
    {
        Set<String> keys = stringRedisTemplate.keys(prefix + "*");
        if(keys!=null&&!keys.isEmpty())
        {
            for(String key:keys)
            {
                String bv=key.substring(prefix.length());
                Set<String> members = stringRedisTemplate.opsForSet().members(key);
                int voteNum=members.size();

                //update the vote number in table videos
                if(prefix.equals(RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX))
                {
                    if(videoDao.setUpvoteNumByBv(bv,voteNum)==0)
                    {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
                else if(prefix.equals(RedisConstants.VIDEO_DOWNVOTE_CACHE_KEY_PREFIX))
                {
                    if(videoDao.setDownvoteNumByBv(bv,voteNum)==0)
                    {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }
    }

}
