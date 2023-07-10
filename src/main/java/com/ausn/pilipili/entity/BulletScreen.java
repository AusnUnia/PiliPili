package com.ausn.pilipili.entity;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class BulletScreen
{
    private String bv; //所属视频编号
    private int sendTimeSec; //弹幕发送时间（在该视频第几秒发送的）
    private Timestamp sendDate; //发送日期
    private String content; //弹幕内容
}