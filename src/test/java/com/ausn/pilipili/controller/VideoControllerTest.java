package com.ausn.pilipili.controller;

import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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

    }
}
