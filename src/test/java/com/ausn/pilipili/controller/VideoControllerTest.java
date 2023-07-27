package com.ausn.pilipili.controller;

import cn.hutool.json.JSONUtil;
import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/26 23:46
 * @Description:
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class VideoControllerTest
{
    @Autowired
    MockMvc mockMvc;
    @Test
    void uploadTest()
    {
        /*mockMvc.perform(MockMvcRequestBuilders.post("/videos/upload"))*/
    }

    @Test
    void jsonTest()
    {
        VideoUploadRequest videoUploadRequest=new VideoUploadRequest();
        videoUploadRequest.setDescription("141ascasda");
        videoUploadRequest.setTags("tag1:tag2:tag3");
        videoUploadRequest.setTitle("tisifosid");

        String json=JSONUtil.toJsonStr(videoUploadRequest);
        log.info(json);
        log.info(JSONUtil.toBean(json, VideoUploadRequest.class).toString());
    }
}
