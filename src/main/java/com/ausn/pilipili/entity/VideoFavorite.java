package com.ausn.pilipili.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/*
the relationship between user saved or not and video
 */
@Data
@TableName("user_favorites")
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
