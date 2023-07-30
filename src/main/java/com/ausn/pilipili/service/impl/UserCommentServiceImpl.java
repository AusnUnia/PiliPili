package com.ausn.pilipili.service.impl;

import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.dao.UserCommentDao;
import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.entity.UserComment;
import com.ausn.pilipili.entity.requestEntity.CommentPublishRequest;
import com.ausn.pilipili.service.UserCommentService;
import com.ausn.pilipili.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCommentServiceImpl implements UserCommentService
{
    @Autowired
    private UserCommentDao userCommentDao;

    @Autowired
    private VideoDao videoDao;

    @Override
    public Result publish(CommentPublishRequest commentPublishRequest)
    {
        int commentLength = commentPublishRequest.getContent().length();
        if(commentLength==0)
        {
            return Result.fail("评论不能为空！");
        }
        if(commentLength>=1024)
        {
            return Result.fail("超出字数限制！");
        }

        //get current user's id
        Long userId = UserHolder.getUser().getUid();

        //create the comment
        UserComment userComment=createUserComment(commentPublishRequest.getBv(), userId);
        userComment.setContent(commentPublishRequest.getContent());

        //TODO 用消息队列异步将评论存入mysql

        return null;
    }

    private UserComment createUserComment(String bv,Long userId)
    {
        UserComment userComment=new UserComment();
        userComment.setUserId(userId);
        userComment.setBv(bv);
        userComment.setSendDate(Timestamp.valueOf(LocalDateTime.now()));
        userComment.setUpvoteNum(0);
        userComment.setDownvoteNum(0);
        userComment.setReplyNum(0);
        return userComment;
    }
    public boolean delete(UserComment userComment)
    {
        return userCommentDao.delete(userComment)>0&&
                videoDao.updateCommentNumByBv(userComment.getBv(),-1)>0;
    }
    public List<UserComment> getByBv(String bv)
    {
        return userCommentDao.getByBv(bv);
    }
    public boolean update(UserComment userComment)
    {
        return userCommentDao.update(userComment)>0;
    }
}
