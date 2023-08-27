package com.ausn.pilipili.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson2.JSON;
import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.common.ResultCode;
import com.ausn.pilipili.dao.UserCommentDao;
import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.entity.UserComment;
import com.ausn.pilipili.entity.requestEntity.CommentPublishRequest;
import com.ausn.pilipili.service.UserCommentService;
import com.ausn.pilipili.utils.UserHolder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCommentServiceImpl extends ServiceImpl<UserCommentDao,UserComment> implements UserCommentService
{
    @Autowired
    private UserCommentDao userCommentDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    @Qualifier("snowflake")
    private Snowflake snowflake;

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
        kafkaTemplate.send("comment_topic", JSON.toJSONString(userComment)).addCallback
        (
            new ListenableFutureCallback<SendResult<String,String>>()
            {
                @Override
                public void onSuccess(SendResult<String, String> result)
                {
                    System.out.println("评论发送成功！！！！！");
                }

                @Override
                public void onFailure(Throwable ex)
                {
                    System.out.println("评论发送失败！！！！！");
                    throw new RuntimeException("评论发送失败！！");
                }
            }
        );

        return Result.ok(ResultCode.DEFAULT_OK,"评论已发送");
    }

    @KafkaListener(topics = "comment_topic",groupId = "comment_group",concurrency = "8")
    @Transactional
    public void saveCommentToMysql(String userCommentStr, Acknowledgment acknowledgment)
    {
        UserComment userComment=JSON.parseObject(userCommentStr,UserComment.class);

        //to avoid consume repeatedly
        boolean consumed = query().eq("commentId", userComment.getCommentId()).exists();
        if(consumed)
        {
            System.out.println("comment id 重复了！！！");
        }
        if(!consumed)
        {
            userCommentDao.save(userComment);
            videoDao.updateCommentNumByBv(userComment.getBv(),1);
        }
        //TODO 要关闭自动提交偏移量，手动提交偏移量，最好是异步+同步的提交方式，防止消费者消费时重复消费或者丢失消息。 还需要做成幂等方案
        //submit offset manually
        acknowledgment.acknowledge();
    }

    private UserComment createUserComment(String bv,Long userId)
    {
        UserComment userComment=new UserComment();
        userComment.setCommentId(snowflake.nextId());
        userComment.setUserId(userId);
        userComment.setBv(bv);
        userComment.setSendDate(Timestamp.valueOf(LocalDateTime.now()));
        userComment.setUpvoteNum(0);
        userComment.setDownvoteNum(0);
        userComment.setReplyNum(0);
        return userComment;
    }

    @Override
    public boolean delete(UserComment userComment)
    {
        return userCommentDao.delete(userComment)>0&&
                videoDao.updateCommentNumByBv(userComment.getBv(),-1)>0;
    }

    @Override
    public List<UserComment> getByBv(String bv)
    {
        //TODO 加布隆过滤和redis缓存
        return userCommentDao.getByBv(bv);
    }

    @Override
    public boolean update(UserComment userComment)
    {
        return userCommentDao.update(userComment)>0;
    }
}
