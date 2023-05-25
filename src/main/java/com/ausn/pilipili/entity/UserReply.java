package com.ausn.pilipili.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

/*
create table comments
(
 commentId int,
 userId varchar(11),
 replyUserId varchar(11),
 sendDate datetime,
 content varchar(512),
 upvoteNum int,
 downvoteNum int,
)
*/

public class UserReply
{
    private int commentId; //所属评论编号
    private String userId;  //发布的用户的id
    private String replyUserId; //所回复的那个用户的id
    private Timestamp sendDate; //回复发布日期
    private String content; //回复内容
    private int upvoteNum; //点赞数量
    private int downvoteNum; //点踩数量

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
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
}
