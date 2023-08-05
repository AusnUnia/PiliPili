package com.ausn.pilipili.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.common.ResultCode;
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
import com.ausn.pilipili.utils.constants.LocalConstants;
import com.ausn.pilipili.utils.constants.RedisConstants;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private RBloomFilter<String> bloomFilter;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    private static final DefaultRedisScript<Long> UPVOTE_SCRIPT;

    static {
        UPVOTE_SCRIPT = new DefaultRedisScript<>();
        UPVOTE_SCRIPT.setLocation(new ClassPathResource("./lua/upvote.lua"));
        UPVOTE_SCRIPT.setResultType(Long.class);
    }



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
        VideoUploadRequest videoUploadRequest=JSON.parseObject(jsonStr,VideoUploadRequest.class);

        //generate the bv for the video.
        String bv= bvGenerator.generateBv();

        //create the path for video file
        String resourcesPath= LocalConstants.VIDEO_SRC_PATH;

        String relativePath=LocalConstants.VIDEO_RELATIVE_PATH_PREFIX
                + bv+ LocalConstants.VIDEO_RELATIVE_PATH_SUFFIX; //the front end server can find the source of the video through this path

        //create the entity of the video which will be stored in mysql
        Video video= VideoConverter.toVideo(videoUploadRequest);

        video.setBv(bv);
        video.setAuthorId(UserHolder.getUser().getUid());
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
        try
        {
            videoFile.transferTo(new File(videoFilePath));
        }
        catch (IOException e)
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //failed to save the video file, rollback this service
        }

        //save the bv into bloom filter
        if(!bloomFilter.add(bv))
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return Result.ok(ResultCode.SAVE_OK);
    }

    @Override
    @Transactional
    public Result upload2(MultipartHttpServletRequest request)
    {
        MultipartFile chunkFile = request.getFile("chunk");
        if(chunkFile!=null&&!chunkFile.isEmpty())
        {
            handleChunk(request);
            return Result.ok(ResultCode.SAVE_OK,"succeeded to save chunk");
        }

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        //parse the VideoUploadRequest which contains the title, description and tags of the uploading video
        String jsonStr = request.getParameter("videoUploadRequest");
        if(jsonStr==null||jsonStr.isEmpty())
        {
            return Result.fail("no video information needed!");
        }
        VideoUploadRequest videoUploadRequest=JSON.parseObject(jsonStr,VideoUploadRequest.class);

        //allocate a bv and create the video entity
        String bv=bvGenerator.generateBv();
        Video video=createNewVideo(videoUploadRequest,bv);

        //save the video information in mysql. the bv may duplicate, so when first duplicate occur, generate another bv.
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

        try
        {
            mergeChunks(bv,request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Result.ok(ResultCode.SAVE_OK);
    }

    private void handleChunk(MultipartHttpServletRequest request)
    {
        MultipartFile chunkFile = request.getFile("chunk");
        Long seqNum=Long.valueOf(request.getParameter("seqNum"));
        String videoId=request.getParameter("videoId");


        // create the path for chunk file
        Path tempFilePath = Path.of(LocalConstants.VIDEO_TMP_PATH, videoId+"_"+seqNum + ".part");

        // save chunk file to temporary folder
        try (InputStream inputStream = chunkFile.getInputStream())
        {
            Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Video createNewVideo(VideoUploadRequest videoUploadRequest,String bv)
    {
        String relativePath=LocalConstants.VIDEO_RELATIVE_PATH_PREFIX
                + bv+ LocalConstants.VIDEO_RELATIVE_PATH_SUFFIX; //the front end server can find the source of the video through this path

        //create the entity of the video which will be stored in mysql
        Video video= VideoConverter.toVideo(videoUploadRequest);

        video.setBv(bv);
        video.setAuthorId(UserHolder.getUser().getUid());
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

        return video;
    }

    private void mergeChunks(String bv,MultipartHttpServletRequest request) throws IOException
    {
        //TODO chunk文件还没到齐就开始merge，视频会缺失
        String videoId = request.getParameter("videoId");
        Long totalSize=Long.valueOf(request.getParameter("totalSize"));
        Long chunkSize=Long.valueOf(request.getParameter("chunkSize"));


        Path videoFilePath = Path.of(LocalConstants.VIDEO_SRC_PATH,"BV"+bv+".mp4");

        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(videoFilePath.toFile())))
        {
            for (int seqNum = 0; seqNum < totalSize/chunkSize; seqNum++) {
                // 读取临时文件
                Path tempFilePath = Path.of(LocalConstants.VIDEO_TMP_PATH, videoId+"_"+seqNum + ".part");
                byte[] chunkData = Files.readAllBytes(tempFilePath);

                // 写入目标文件
                outputStream.write(chunkData);

                // 删除临时文件
                Files.deleteIfExists(tempFilePath);
            }
        }
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
        String key= RedisConstants.VIDEO_CACHE_KEY_PREFIX+bv;

        //use bloom filter to determine whether the data may exist in redis or mysql or not

        if(!bloomFilter.contains(bv))
        {
            System.out.println("bloom filter rejected!");
            return Result.fail(ResultCode.GET_ERR,"no such video!");
        }

        //the request passed the bloom filter, then query it in redis
        String videoJson = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(videoJson))
        {
            video= JSON.parseObject(videoJson,Video.class);
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
                stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(video),RedisConstants.VIDEO_CACHE_TTL, TimeUnit.MINUTES);
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

    /**
     * update the information of the video in mysql, meanwhile delete the cached video in redis
     * @param video
     * @return
     */

    public Result updateInMysql(Video video)
    {
        if(videoDao.update(video)>0)
        {
            stringRedisTemplate.delete(RedisConstants.VIDEO_CACHE_KEY_PREFIX+video.getBv());
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

    @Override
    public Result upvote(String bv)
    {
        /**
         * upvote operation only update the upvote number in redis, and the data in redis will
         * be written into mysql by scheduled task
         */

        //get current user's id
        Long userId=UserHolder.getUser().getUid();

        //get the keys of sets in redis that contains users who upvoted or downvoted or canceled to the video
        String upvoteKey=RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX + bv;
        String novoteKey=RedisConstants.VIDEO_NOVOTE_CACHE_KEY_PREFIX + bv;
        String downvoteKey=RedisConstants.VIDEO_DOWNVOTE_CACHE_KEY_PREFIX + bv;

        /**
         * if the upvote data may be not in redis, don't need to reconstruct it. upvote usually
         * go along with getUpVoteNum , getUpVoteNum will reconstruct it.
         */


        //when the user is upvoting or canceling the upvote, the downvote should always be false
        stringRedisTemplate.opsForSet().remove(downvoteKey,userId.toString());

        //if the user has upvoted, cancel the upvote
        if(stringRedisTemplate.opsForSet().isMember(upvoteKey,userId.toString()))
        {
            stringRedisTemplate.opsForSet().remove(upvoteKey,userId.toString());
            stringRedisTemplate.opsForSet().add(novoteKey,userId.toString());
        }
        //if the user has not upvoted, do upvote
        else
        {
            stringRedisTemplate.opsForSet().add(upvoteKey,userId.toString());
            stringRedisTemplate.opsForSet().remove(novoteKey,userId.toString());
        }

        //return the refreshed upvote number
        Long upvoteNum=stringRedisTemplate.opsForSet().size(upvoteKey);

        return Result.ok(ResultCode.UPDATE_OK,upvoteNum);
    }

    @Transactional
    @Override
    public Result downvote(String bv)
    {
        //TODO
        return Result.ok(ResultCode.UPDATE_OK);
    }

    @Override
    @Transactional
    public Result coin(String bv, int num)
    {
        //the coin number should not be lager than 2
        if(num>2||num<1)
        {
            return Result.fail("can only put 1 or 2 coins every time!");
        }

        //get current user's id
        Long userId= UserHolder.getUser().getUid();

        //get the coin information about the video in redis
        String key=RedisConstants.VIDEO_COIN_TODAY_CACHE_KEY_PREFIX+bv;
        Object cachedCoinNum =stringRedisTemplate.opsForHash().get(key, userId.toString());
        int coinNum=0;

        //must have put coin today
        if(cachedCoinNum!=null)
        {
            coinNum=Integer.valueOf((String)cachedCoinNum);
        }

        //determine whether refuse the put coin request or not only according to the data in redis
        //this guarantee that only at most 2 requests can reach mysql for each user every day
        if(coinNum+num>2)
        {
            return Result.fail("can only put at most 2 coins everyday for one video!");
        }

        //when reach the mysql, discard those data that got in redis, use the data from mysql.
        //get the latest coin put from mysql
        VideoCoin videoCoin = videoCoinDao.getLatestByBvAndUserId(bv, userId);
        if(  videoCoin==null||videoCoin.getPutDate().before( Date.valueOf(LocalDate.now()) )  ) //haven't put coin today
        {
            videoCoin=createVideoCoin(bv,userId);
        }

        //this operation is to guarantee the consistence of mysql
        if(videoCoin.getCoinNum()+num>2)
        {
            return Result.fail("can only put at most 2 coins everyday for one video!");
        }
        videoCoin.setCoinNum(videoCoin.getCoinNum()+num);

        //update the relationship between coin, user and the video in mysql
        System.out.println(videoCoin);
        if(videoCoin.getId()==null) //haven't put coin today,so the videoCoin is a new VideoCoin which has no id yet, should save in mysql
        {
            videoCoinDao.save(videoCoin);
        }
        else if(videoCoinDao.update(videoCoin)==0) // have put coin today, update it.
        {
            System.out.println("videoCoinDao.update(videoCoin)=0!!!!!!!!!!!!!!!");
            return Result.fail(ResultCode.UPDATE_ERR,"failed to put coin!");
        }

        //update the coins number in video
        if(videoDao.updateCoinNumByBv(bv,num)==0)
        {
            System.out.println("videoDao.updateCoinNumByBv(bv,num)=0&&&&&&&&&&&&&");
            return Result.fail(ResultCode.UPDATE_ERR,"failed to put coin!");
        }


        //update data in redis
        stringRedisTemplate.opsForHash().put(key,userId.toString(),videoCoin.getCoinNum().toString());
        stringRedisTemplate.delete(RedisConstants.VIDEO_COIN_NUM_CACHE_KEY_PREFIX+bv); //delete the cached coin number

        return Result.ok(ResultCode.UPDATE_OK);
    }

    @Override
    public Result favorite(String bv)
    {
        //TODO

        return null;
    }

    @Override
    public Result getUpvoteNumByBv(String bv)
    {
        //see if in bloom filter
        if(!bloomFilter.contains(bv))
        {
            return Result.fail(ResultCode.GET_ERR,"the video doesn't exist!");
        }

        //then, see if in redis
        String key=RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX + bv;
        Boolean isExist = stringRedisTemplate.hasKey(key);

        //if not in redis , reconstruct is
        if(isExist==null||!isExist)
        {
            RLock lock = redissonClient.getLock(RedisConstants.VIDEO_VOTE_LOCK_KEY_PREFIX + bv);
            boolean isLocked=lock.tryLock();
            if(isLocked)
            {
                try
                {
                    loadVoteIntoRedis(bv);
                }
                finally
                {
                    lock.unlock();
                }
            }
        }

        Long upvoteNum = stringRedisTemplate.opsForSet().size(key);

        return Result.ok(ResultCode.GET_OK,upvoteNum);
    }

    @Override
    public Result getCoinNumByBv(String bv)
    {
        //see if in bloom filter first
        if(!bloomFilter.contains(bv))
        {
            return Result.fail(ResultCode.GET_ERR,"the video doesn't exist!");
        }


        //then, see if in redis first
        String key=RedisConstants.VIDEO_COIN_NUM_CACHE_KEY_PREFIX + bv;
        Boolean isExist = stringRedisTemplate.hasKey(key);
        Long coinNum=null;

        //if not in redis , reconstruct it
        if(isExist==null||!isExist)
        {
            RLock lock = redissonClient.getLock(RedisConstants.VIDEO_COIN_NUM_LOCK_KEY_PREFIX + bv);
            boolean isLocked=lock.tryLock();
            if(isLocked)
            {
                try
                {
                    coinNum = videoDao.getCoinNumByBv(bv);
                    stringRedisTemplate.opsForValue()
                            .set(key,coinNum.toString(),RedisConstants.VIDEO_COIN_NUM_CACHE_TTL,TimeUnit.MINUTES);
                }
                finally
                {
                    lock.unlock();
                }
            }
        }

        //the data is probably in redis, but may also be expired before this line and
        // after judging "Boolean isExist = stringRedisTemplate.hasKey(key);"
        if(coinNum==null)
        {
            String coinNumStr = stringRedisTemplate.opsForValue().get(key);
            if(coinNumStr!=null&&!coinNumStr.isEmpty())
            {
                //the data is still in redis, refresh the ttl when get it.
                stringRedisTemplate.expire(key,RedisConstants.VIDEO_COIN_NUM_CACHE_TTL,TimeUnit.MINUTES);
                coinNum=Long.valueOf(coinNumStr);
            }
        }

        return Result.ok(ResultCode.GET_OK,coinNum);
    }


    private void loadVoteIntoRedis(String bv)
    {
        //get the keys of sets in redis that contains users who upvoted or downvoted or canceled to the video
        String upvoteKey=RedisConstants.VIDEO_UPVOTE_CACHE_KEY_PREFIX + bv;
        String novoteKey=RedisConstants.VIDEO_NOVOTE_CACHE_KEY_PREFIX + bv;
        String downvoteKey=RedisConstants.VIDEO_DOWNVOTE_CACHE_KEY_PREFIX + bv;

        //query all the vote information about the video from mysql
        List<VideoVote> videoVoteList = videoVoteDao.getByBv(bv);

        //add these votes into redis
        Map<Integer, List<VideoVote>> groupedMap = videoVoteList.stream()
                .collect(Collectors.groupingBy(VideoVote::getVote));

        if(groupedMap.get(1)!=null)
        {
            String[] upvoteUserIds = groupedMap.get(1).stream()
                    .map(videoVote -> String.valueOf(videoVote.getUserId()))
                    .toArray(String[]::new);
            stringRedisTemplate.opsForSet().add(upvoteKey,upvoteUserIds);
        }

        if(groupedMap.get(0)!=null)
        {
            String[] novoteUserIds = groupedMap.get(0).stream()
                    .map(videoVote -> String.valueOf(videoVote.getUserId()))
                    .toArray(String[]::new);
            stringRedisTemplate.opsForSet().add(novoteKey,novoteUserIds);
        }

        if(groupedMap.get(-1)!=null)
        {
            String[] downvoteUserIds = groupedMap.get(-1).stream()
                    .map(videoVote -> String.valueOf(videoVote.getUserId()))
                    .toArray(String[]::new);
            stringRedisTemplate.opsForSet().add(downvoteKey,downvoteUserIds);
        }
    }

    private VideoCoin createVideoCoin(String bv,Long userId)
    {
        VideoCoin videoCoin=new VideoCoin();
        videoCoin.setCoinNum(0);
        videoCoin.setBv(bv);
        videoCoin.setUserId(userId);
        videoCoin.setPutDate(Date.valueOf(LocalDate.now()));

        return videoCoin;
    }

    private VideoVote createVideoVote(String bv, Long userId)
    {
        VideoVote videoVote = new VideoVote();
        videoVote.setVote(0);
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
