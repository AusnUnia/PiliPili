package com.ausn.pilipili.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

/*
create table comments
(
 commentId int primary key,
 bv varchar(11),
 userId varchar(11),
 sendDate datetime,
 content varchar(1024),
 upvoteNum int,
 downvoteNum int,
 replyNum int
)
*/

public class UserComment{
    private int commentId; //评论编号
    private String bv; //所属视频编号
    private String userId;  //发布评论的用户的id
    private Timestamp sendDate; //评论发布日期
    private String content; //评论内容
    private int upvoteNum; //点赞数量
    private int downvoteNum; //点踩数量
    private int replyNum; //该评论下回复的数量

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpvoteNum() {
        return upvoteNum;
    }

    public void setUpvoteNum(int upvoteNum) {
        this.upvoteNum = upvoteNum;
    }

    public int getDownvoteNum() {
        return downvoteNum;
    }

    public void setDownvoteNum(int downvoteNum) {
        this.downvoteNum = downvoteNum;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }
}
