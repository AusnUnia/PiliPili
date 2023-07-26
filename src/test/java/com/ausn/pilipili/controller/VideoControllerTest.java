package com.ausn.pilipili.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@WebMvcTest(VideoController.class)
public class VideoControllerTest
{
    @Autowired
    MockMvc mockMvc;
    @Test
    void uploadTest()
    {
        /*mockMvc.perform(MockMvcRequestBuilders.post())*/
    }
}
