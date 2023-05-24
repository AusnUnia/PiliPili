package com.ausn.pilipili.entity;

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

public class Video {
    private String bv; //视频编号 11位数字或字母
    private String title; //标题
    private String authorId; //作者id
    private BigInteger viewNum; //播放量
    private Timestamp uploadDate; //上传日期
    private BigInteger bulletScreenNum; //弹幕数量
    private BigInteger commentNum; //评论数量
    private BigInteger upvoteNum; //点赞数量
    private BigInteger downvoteNum; //点踩数量
    private BigInteger coinNum; //投币数量
    private BigInteger saveNum; //收藏数量
    private BigInteger shareNum; //转发数量
    private String tags; //标签  形如 "标签1,标签2,标签3"
    private String videoPath; //视频存放位置  视频直接存本地，不存数据库
    private String introduction; //视频简介

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public BigInteger getViewNum() {
        return viewNum;
    }

    public void setViewNum(BigInteger viewNum) {
        this.viewNum = viewNum;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public BigInteger getBulletScreenNum() {
        return bulletScreenNum;
    }

    public void setBulletScreenNum(BigInteger bulletScreenNum) {
        this.bulletScreenNum = bulletScreenNum;
    }

    public BigInteger getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(BigInteger commentNum) {
        this.commentNum = commentNum;
    }

    public BigInteger getUpvoteNum() {
        return upvoteNum;
    }

    public void setUpvoteNum(BigInteger upvoteNum) {
        this.upvoteNum = upvoteNum;
    }

    public BigInteger getDownvoteNum() {
        return downvoteNum;
    }

    public void setDownvoteNum(BigInteger downvoteNum) {
        this.downvoteNum = downvoteNum;
    }

    public BigInteger getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(BigInteger coinNum) {
        this.coinNum = coinNum;
    }

    public BigInteger getSaveNum() {
        return saveNum;
    }

    public void setSaveNum(BigInteger saveNum) {
        this.saveNum = saveNum;
    }

    public BigInteger getShareNum() {
        return shareNum;
    }

    public void setShareNum(BigInteger shareNum) {
        this.shareNum = shareNum;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
};



