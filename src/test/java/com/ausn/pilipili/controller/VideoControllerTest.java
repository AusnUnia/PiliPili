package com.ausn.pilipili.controller;

import com.alibaba.fastjson2.JSON;
import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.entity.requestEntity.CommentPublishRequest;
import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    String token="bef4af919f194f9d8eacaab6e376f189";

    @Test
    void uploadTest()
    {
        /*mockMvc.perform(MockMvcRequestBuilders.post("/videos/upload"))*/
    }

    @Test
    void getByBvTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/videos/BV11223344557")
                                .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void getUpvoteNumByBvTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/videos/upvoteNum/BV11223344557")
                                .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void getCoinNumByBvTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/videos/coinNum/BV11223344557")
                                .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

}
