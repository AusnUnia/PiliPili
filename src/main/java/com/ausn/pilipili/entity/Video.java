package com.ausn.pilipili.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

/*
create table videos
(
 bv varchar(11) primary key ,
 title varchar(64),
 authorId varchar(11),
 views bigint,
 uploadDate datetime,
 bulletScreenNum bigint,
 commentNum bigint,
 upvoteNum bigint,
 downvoteNum bigint,
 coinNum bigint,
 saveNum bigint,
 sharesNum bigint,
 tags varchar(128),
 videoPath varchar(1024),
 introduction text
)
*/

@Data
@TableName("videos")
public class Video {
    private String bv; //视频编号 11位数字或字母
    private String title; //标题
    private Long authorId; //作者id
    private Long viewNum; //播放量
    private Timestamp uploadDate; //上传日期
    private Long bulletScreenNum; //弹幕数量
    private Long commentNum; //评论数量
    private Long upvoteNum; //点赞数量
    private Long downvoteNum; //点踩数量
    private Long coinNum; //投币数量
    private Long saveNum; //收藏数量
    private Long shareNum; //转发数量
    private String tags; //标签  形如 "标签1,标签2,标签3"
    private String videoPath; //视频存放位置  视频直接存本地，不存数据库
    private String description; //视频简介

};



