package com.ausn.pilipili.entity;

import lombok.Data;

import java.sql.Timestamp;

/*
create table pusers
(
    uid bigint primary key ,
    phoneNumber varchar(11),
    nickName varchar(32),
    gender varchar(8),
    birthday timestamp,
    avatarPath varchar(256)
)
 */


@Data
public class PUser
{
    private Long uid; //用户id 11位数字
    private String phoneNumber; //user's phone number
    private String nickName; //昵称
    private String gender; //性别
    private Timestamp birthday; //生日
    private String avatarPath; //头像存放位置
}
