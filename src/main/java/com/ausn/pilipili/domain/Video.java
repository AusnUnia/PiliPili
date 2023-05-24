package com.ausn.pilipili.domain;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class Video {
    private String bv; //视频编号 11位数字或字母
    private String title; //标题
    private String authorId; //作者id
    private BigInteger views; //播放量
    private Timestamp uploadDate; //上传日期
    private BigInteger bulletScreenNum; //弹幕数量
    private BigInteger comments; //评论数量
    private BigInteger upvotes; //点赞数量
    private BigInteger downvotes; //点踩数量
    private BigInteger coins; //投币数量
    private BigInteger saves; //收藏数量
    private BigInteger shares; //转发数量
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

    public BigInteger getViews() {
        return views;
    }

    public void setViews(BigInteger views) {
        this.views = views;
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

    public BigInteger getComments() {
        return comments;
    }

    public void setComments(BigInteger comments) {
        this.comments = comments;
    }

    public BigInteger getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(BigInteger upvotes) {
        this.upvotes = upvotes;
    }

    public BigInteger getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(BigInteger downvotes) {
        this.downvotes = downvotes;
    }

    public BigInteger getCoins() {
        return coins;
    }

    public void setCoins(BigInteger coins) {
        this.coins = coins;
    }

    public BigInteger getSaves() {
        return saves;
    }

    public void setSaves(BigInteger saves) {
        this.saves = saves;
    }

    public BigInteger getShares() {
        return shares;
    }

    public void setShares(BigInteger shares) {
        this.shares = shares;
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

/*
create table videos
(
 bv varchar(11) primary key ,
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
 introduction text
)
*/

