package com.ausn.pilipili.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

public class UserComment{
    private String bv; //所属视频编号
    private String commentId; //评论编号
    private String userId;  //发布评论的用户的id
    private Timestamp sendDate; //评论发布日期
    private BigInteger upvotes; //点赞数量
    private BigInteger downvotes; //点踩数量
    private BigInteger replies; //该评论下回复的数量

}

/*
create table comments
(
 bv varchar(16) primary key ,
 title varchar(128),
 authorId varchar(11),
 views bigint,
 uploadDate datetime,
 bulletScreenNum bigint,
 upvotes bigint,
 downvotes bigint,
 coins bigint,
 save bigint,
 shares bigint,
 tags varchar(128),
 videoPath varchar(1024),
 introduction varchar(2048)
)
*/