package com.ausn.pilipili.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.controller.ResultCode;
import com.ausn.pilipili.dao.VideoCoinDao;
import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.dao.VideoVoteDao;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.entity.VideoCoin;
import com.ausn.pilipili.entity.VideoVote;
import com.ausn.pilipili.entity.converter.VideoConverter;
import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import com.ausn.pilipili.service.VideoService;
import com.ausn.pilipili.utils.BvGenerator;
import com.ausn.pilipili.utils.UserHolder;
import com.ausn.pilipili.utils.constants.RedisConstants;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.IdGenerator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class VideoServiceImpl implements VideoService
{
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private VideoVoteDao videoVoteDao;
    @Autowired
    private VideoCoinDao videoCoinDao;
    @Autowired
    private ResourceLoader resourceLoader;


    @Autowired
    private RBloomFilter<String> rBloomFilter;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private BvGenerator bvGenerator;

    private static final ExecutorService CACHE_REBUILD_EXECUTOR= Executors.newFixedThreadPool(8);

    @Override
    @Transactional
    public Result upload(MultipartHttpServletRequest request)
    {
        //parse the video file
        MultipartFile videoFile = request.getFile("video");
        if(videoFile==null||videoFile.isEmpty())
        {
            return Result.fail("the video file is empty!");
        }

        //parse the VideoUploadRequest which contains the title, description and tags of the uploading video
        String jsonStr = request.getParameter("videoUploadRequest");
        VideoUploadRequest videoUploadRequest=JSONUtil.toBean(jsonStr,VideoUploadRequest.class);

        //generate the bv for the video.
        String bv= bvGenerator.generateBv();

        //create the path for video file
        //TODO 保存路径有问题
        String resourcesPath="C:\\Users\\16377\\Desktop\\Java\\PiliPili\\src\\main\\resources\\static\\video";

        String relativePath="\\static\\video\\BV"+bv+".mp4";

        //create the entity of the video which will be stored in mysql
        Video video= VideoConverter.toVideo(videoUploadRequest);

        video.setBv(bv);
        video.setAuthorId("1234");
        video.setViewNum(0L);
        video.setUploadDate(Timestamp.valueOf(LocalDateTime.now()));
        video.setBulletScreenNum(0L);
        video.setCommentNum(0L);
        video.setSaveNum(0L);
        video.setShareNum(0L);
        video.setUpvoteNum(0L);
        video.setDownvoteNum(0L);
        video.setCoinNum(0L);
        video.setVideoPath(relativePath);


        //save the video information in mysql. the bv may duplicate , so when first duplicate occur, generate another bv.
        try
        {
            videoDao.save(video);
        }
        catch (SQLException e)
        {
            if(e.getErrorCode()==1062) //the primary key is duplicated, try to generate a new bv
            {
                bv=bvGenerator.generateBv();
                video.setBv(bv);
                try
                {
                    videoDao.save(video);
                }
                catch (SQLException ex)
                {
                    if(ex.getErrorCode()==1062)
                    {
                        return Result.fail(ResultCode.SAVE_ERR,"failed to save video! bv duplicated!");
                    }
                    return Result.fail(ResultCode.SAVE_ERR,"failed to save video!");
                }
            }
            else
            {
                return Result.fail(ResultCode.SAVE_ERR,"failed to save video!");
            }
        }

        //save the video file
        String videoFilePath=resourcesPath+relativePath;
        System.out.println(videoFilePath);
        try
        {
            videoFile.transferTo(new File(videoFilePath));
        }
        catch (IOException e)
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //failed to save the video file, rollback this service
        }

        return Result.ok(ResultCode.SAVE_OK);
    }

    @Override
    public Result delete(Video video)
    {
        if(videoDao.delete(video)>0)
        {
            return Result.ok(ResultCode.DELETE_OK);
        }

        return Result.fail(ResultCode.DELETE_ERR,"failed to delete video!");
    }

    @Override
    public Result getByBv(String bv)
    {
        /*
        TODO 要抽象出一个查询函数，传入要查数据的key，回调函数（查数据库用），回调函数的参数，等。这个查询函数逻辑是这样的：
        先用布隆过滤器查，没有就直接返回。可能有就去redis查，查到就返回，查不到就去数据库查。去数据库查时要加一个分布式锁，
        然后查到了数据就把数据写入redis并返回，没查到就把空数据写入redis。
         */
        Video video=null;

        //use bloom filter to determine whether the data may exist in redis or mysql or not
        String key= RedisConstants.VIDEO_CACHE_KEY_PREFIX+bv;
/*        if(!rBloomFilter.contains(key))
        {
            System.out.println("bloom filter rejected!");
            return Result.fail(ResultCode.GET_ERR,"no such video!");
        }*/

        //the request passed the bloom filter, then query it in redis
        String videoJson = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(videoJson))
        {
            video= JSONUtil.toBean(videoJson,Video.class);
            return Result.ok(ResultCode.GET_OK,video);
        }

        //can't find in redis, must query mysql.
        RLock lock = redissonClient.getLock(RedisConstants.VIDEO_LOCK_KEY_PREFIX + bv);
        boolean isLocked=lock.tryLock();

        if(isLocked)
        {
            try
            {
                //TODO 这部分查数据库操作太费时间，需要用消息队列异步得去查询数据库。
                video=videoDao.getByBv(bv);
                if(video==null)
                {
                    stringRedisTemplate.opsForValue().set(key,"",RedisConstants.VIDEO_CACHE_TTL, TimeUnit.MINUTES);
                    return Result.fail(ResultCode.GET_ERR,"no such video!");
                }
                stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(video),RedisConstants.VIDEO_CACHE_TTL, TimeUnit.MINUTES);
                return Result.ok(ResultCode.GET_OK,video);
            }
            finally
            {
                lock.unlock();
            }
        }

        return Result.fail(ResultCode.GET_ERR,"no such video!");
    }

    @Override
    public Result getByAuthorId(String authorId)
    {
        List<Video> videos=videoDao.getByAuthorId(authorId);
        return Result.ok(ResultCode.GET_OK,videos);
    }

    @Override
    public Result update(Video video)
    {
        if(videoDao.update(video)>0)
        {
            return Result.ok(ResultCode.UPDATE_OK);
        }

        return Result.fail(ResultCode.UPDATE_ERR,"failed to update video!");
    }

    @Override
    public Result getRandomly()
    {
        List<Video> videos=videoDao.getRandomly();
        return Result.ok(ResultCode.GET_OK,videos);
    }

    @Transactional
    @Override
    public Result upvote(String bv)
    {
        //TODO 这里要针对用户加锁，不然同一个用户高并发点赞会出问题

        //get current user's id
        Long userId= UserHolder.getUser().getUid();

        //query the information about the votes of a video
        VideoVote videoVote=videoVoteDao.getByBvUserId(bv,userId);
        if(videoVote==null)
        {
            videoVote=createVideoVote(bv,userId);
            if(videoVote==null)
            {
                return Result.fail(ResultCode.SAVE_ERR,"failed to upvote!");
            }
        }

        //do upvote or cancel the upvote
        ///if the user is upvoting or canceling the upvote, the downvote should always be false

        if(videoVote.getDownvote())
        {
            videoVote.setDownvote(false);

            ///update the downvote number of the video at the same time
            if(videoDao.updateDownvoteNumByBv(bv,-1)==0)
            {
                return Result.fail(ResultCode.UPDATE_ERR,"failed to upvote!");
            }
        }

        if(videoVote.getUpvote())
        {
            ///if the user has upvoted, cancel the upvote
            videoVote.setUpvote(false);

            ///update the upvote number of the video at the same time
            if(videoDao.updateUpvoteNumByBv(bv,-1)==0)
            {
                return Result.fail(ResultCode.UPDATE_ERR,"failed to upvote!");
            }
        }
        else
        {
            ///if the user has not upvoted, do upvote
            videoVote.setUpvote(true);

            ///update the upvote number of the video at the same time
            if(videoDao.updateUpvoteNumByBv(bv,1)==0)
            {
                return Result.fail(ResultCode.UPDATE_ERR,"failed to upvote!");
            }
        }

        if(videoVoteDao.update(videoVote)==0)
        {
            return Result.fail(ResultCode.UPDATE_ERR,"failed to upvote!");
        }

        return Result.ok(ResultCode.UPDATE_OK);
    }

    @Transactional
    @Override
    public Result downvote(String bv) {
        //TODO 这里要针对用户加锁，不然同一个用户高并发点踩会出问题

        //get current user's id
        Long userId= UserHolder.getUser().getUid();

        //query the information about the votes of a video
        VideoVote videoVote=videoVoteDao.getByBvUserId(bv,userId);
        if(videoVote==null)
        {
            videoVote=createVideoVote(bv,userId);
            if(videoVote==null)
            {
                return Result.fail(ResultCode.SAVE_ERR,"failed to upvote!");
            }
        }

        //do downvote or cancel the downvote
        ///if the user is downvote or canceling the upvote, the upvote should always be false

        if(videoVote.getUpvote())
        {
            videoVote.setUpvote(false);

            ///update the upvote number of the video at the same time
            if(videoDao.updateUpvoteNumByBv(bv,-1)==0)
            {
                return Result.fail(ResultCode.UPDATE_ERR,"failed to downvote!");
            }
        }

        if(videoVote.getDownvote())
        {
            ///if the user has downvoted, cancel the downvote
            videoVote.setDownvote(false);

            ///update the downvote number of the video at the same time
            if(videoDao.updateDownvoteNumByBv(bv,-1)==0)
            {
                return Result.fail(ResultCode.UPDATE_ERR,"failed to downvote!");
            }
        }
        else
        {
            ///if the user has not downvoted, do downvote
            videoVote.setDownvote(true);

            ///update the downvote number of the video at the same time
            if(videoDao.updateDownvoteNumByBv(bv,1)==0)
            {
                return Result.fail(ResultCode.UPDATE_ERR,"failed to downvote!");
            }
        }

        if(videoVoteDao.update(videoVote)==0)
        {
            return Result.fail(ResultCode.UPDATE_ERR,"failed to downvote!");
        }

        return Result.ok(ResultCode.UPDATE_OK);
    }

    @Override
    @Transactional
    public Result coin(String bv, int num)
    {
        //the coin number should not be lager than 2
        if(num>2||num<=0)
        {
            return Result.fail("can only put 1 or 2 coins every time!");
        }

        //get current user's id
        Long userId= UserHolder.getUser().getUid();

        //query the information about the coins of a video. when query the coins ,must use the date today, for a user can put coin under the same video in different day.
        VideoCoin videoCoin=videoCoinDao.getByBvUserIdPutDate(bv,userId, Date.valueOf(LocalDate.now()));
        if(videoCoin==null) //the user haven't put coin today
        {
            videoCoin=createVideoCoin(bv,userId, Date.valueOf(LocalDate.now()));
            if(videoCoin==null)
            {
                return Result.fail(ResultCode.SAVE_ERR,"failed to put coin!");
            }
        }

        //can't put more coins
        if(videoCoin.getCoinNum()+num>2)
        {
            return Result.fail("can only put at most 2 coins everyday for one video!");
        }

        //update the relationship between coin, user and the video
        videoCoin.setCoinNum((short) (videoCoin.getCoinNum()+num));
        if(videoCoinDao.update(videoCoin)==0)
        {
            return Result.fail(ResultCode.UPDATE_ERR,"failed to put coin!");
        }

        //update the coins number in video
        if(videoDao.updateCoinNumByBv(bv,num)==0)
        {
            return Result.fail(ResultCode.UPDATE_ERR,"failed to put coin!");
        }

        return Result.ok(ResultCode.UPDATE_OK);
    }

    @Override
    public Result favorite(String bv)
    {
        //TODO

        return null;
    }

    private VideoCoin createVideoCoin(String bv,Long userId,Date dateToday)
    {
        VideoCoin videoCoin=new VideoCoin();
        videoCoin.setCoinNum((short) 0);
        videoCoin.setBv(bv);
        videoCoin.setUserId(userId);
        videoCoin.setPutDate(dateToday);

        if(videoCoinDao.save(videoCoin)==0)
        {
            System.out.println("failed to save videoCoin!");
            return null;
        }

        return videoCoin;
    }

    private VideoVote createVideoVote(String bv, Long userId)
    {
        VideoVote videoVote = new VideoVote();
        videoVote.setUpvote(false);
        videoVote.setDownvote(false);
        videoVote.setBv(bv);
        videoVote.setUserId(userId);

        if(videoVoteDao.save(videoVote)==0)
        {
            System.out.println("failed to save videoVote!");
            return null;
        }

        return videoVote;
    }
}
