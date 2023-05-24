package com.ausn.pilipili.domain;

import java.sql.Timestamp;

public class PUser {
    private String uid; //用户id 11位数字
    private String nickName; //昵称
    private String gender; //性别
    private Timestamp birthday; //生日
    private String avatarPath; //头像存放位置
    private String fanIds; //粉丝的id
    private String followedIds; //关注的人的id
}
