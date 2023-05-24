package com.ausn.pilipili.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

public class BulletScreen{

    private String bv; //所属视频编号
    private BigInteger sendTime; //弹幕发送时间（在该视频第几秒发送的）
    private Timestamp sendDate; //发送日期
    private String content; //弹幕内容

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public BigInteger getSendTime() {
        return sendTime;
    }

    public void setSendTime(BigInteger sendTime) {
        this.sendTime = sendTime;
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
}