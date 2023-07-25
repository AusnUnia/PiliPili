package com.ausn.pilipili.entity.requestEntity;

import lombok.Data;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/26 0:45
 * @Description:
 */
@Data
public class VideoUploadRequest
{
    private String title; //标题
    private String tags; //标签  形如 "标签1,标签2,标签3"
    private String description; //视频简介
}
