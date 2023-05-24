package com.ausn.pilipili.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

public class UserReply
{
    private String commentId; //所属评论编号
    private String userId;  //发布的用户的id
    private String replyUserId; //所回复的那个用户的id
    private Timestamp sendDate; //回复发布日期
    private BigInteger upvotes; //点赞数量
    private BigInteger downvotes; //点踩数量
}
