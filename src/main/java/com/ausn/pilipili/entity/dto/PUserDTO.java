package com.ausn.pilipili.entity.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class PUserDTO
{
    private Long uid; //用户id 11位数字
    private String nickName; //昵称
    private String gender; //性别
    private Date birthday; //生日
    private String avatarPath; //头像存放位置
}
