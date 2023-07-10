package com.ausn.pilipili.service.impl;

import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.controller.ResultCode;
import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.dao.VideoVoteDao;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.entity.VideoVote;
import com.ausn.pilipili.service.VideoService;
import com.ausn.pilipili.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService
{
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private VideoVoteDao videoVoteDao;

    @Override
    public Result save(Video video)
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
}
