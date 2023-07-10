package com.ausn.pilipili.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PUserDTO
{
    private Long uid; //用户id 11位数字
    private String nickName; //昵称
    private String gender; //性别
    private Timestamp birthday; //生日
    private String avatarPath; //头像存放位置
}
