package com.ausn.pilipili.service.impl;

import com.ausn.pilipili.dao.UserCommentDao;
import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.entity.UserComment;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.service.UserCommentService;
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

    public boolean save(UserComment userComment)
    {
        userComment.setSendDate(Timestamp.valueOf(LocalDateTime.now()));
        userComment.setUpvoteNum(0);
        userComment.setDownvoteNum(0);
        userComment.setReplyNum(0);

        return userCommentDao.save(userComment)>0&&
                videoDao.updateCommentNumByBv(userComment.getBv(),1)>0;
    }
    public boolean delete(UserComment userComment)
    {
        return userCommentDao.delete(userComment)>0&&videoDao.updateCommentNumByBv(userComment.getBv(),-1)>0;
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
