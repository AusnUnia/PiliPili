package com.ausn.pilipili.entity;

import lombok.Data;

import java.sql.Timestamp;

/*
the relationship between user saved or not and video
 */
@Data
public class VideoFavorite
{
    private String bv;
    private Long userId;
    private Timestamp favoriteTime;
}

/*
create table video_saves
(
    bv varchar(11),
    userId bigint,
    saveTime timestamp,
    primary key(bv,userId)
)
 */
