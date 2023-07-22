package com.ausn.pilipili.service.impl;

import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.controller.ResultCode;
import com.ausn.pilipili.dao.VideoCoinDao;
import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.dao.VideoVoteDao;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.entity.VideoCoin;
import com.ausn.pilipili.entity.VideoVote;
import com.ausn.pilipili.service.VideoService;
import com.ausn.pilipili.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService
{
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private VideoVoteDao videoVoteDao;
    @Autowired
    private VideoCoinDao videoCoinDao;

    @Override
    public Result upload(Video video)
    {
        video.setUploadDate(Timestamp.valueOf(LocalDateTime.now()));
        video.setSaveNum(0L);
        video.setShareNum(0L);
        video.setUpvoteNum(0L);
        video.setDownvoteNum(0L);
        video.setCoinNum(0L);

        if(videoDao.save(video)>0)
        {
            return Result.ok(ResultCode.SAVE_OK);
        }

        return Result.fail(ResultCode.SAVE_ERR,"failed to save video!");
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
        Video video=videoDao.getByBv(bv);
        return Result.ok(ResultCode.GET_OK,video);
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
    public Result favorite()
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
