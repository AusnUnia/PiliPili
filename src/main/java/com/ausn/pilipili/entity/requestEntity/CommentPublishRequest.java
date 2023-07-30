package com.ausn.pilipili.entity.requestEntity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/30 19:20
 * @Description:
 */

@Data
public class CommentPublishRequest
{
    private String bv; //所属视频编号
    private String content; //评论内容
}
