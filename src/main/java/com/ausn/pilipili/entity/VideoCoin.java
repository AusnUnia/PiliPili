package com.ausn.pilipili.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/*
the relationship between user saved or not and video
 */
@Data
public class VideoCoin
{
    private String bv; //the id of video that the user put coin
    private Long userId; //the user's uid
    private short coinNum; //the number of coins put one time
    private Date putDate; //the time of putting coins, a user can only put at most 2 coins to one video each day.
}

/*
 create table video_coins
 (
    bv varchar(11),
    userId bigint,
    coinNum short,
    putDate date,
    primary key(bv,userId,putTime)
 )
 */