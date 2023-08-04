package com.ausn.pilipili.controller;

import com.alibaba.fastjson2.JSON;
import com.ausn.pilipili.dao.UserCommentDao;
import com.ausn.pilipili.entity.UserComment;
import com.ausn.pilipili.entity.requestEntity.CommentPublishRequest;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/5 1:11
 * @Description:
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class UserCommentControllerTest
{
    @Autowired
    MockMvc mockMvc;

    String token="8e3eb099544043bcb6aa99fd63efb31b";

    @Test
    void publishTest() throws Exception {
        CommentPublishRequest commentPublishRequest=new CommentPublishRequest();
        commentPublishRequest.setBv("11223344557");
        commentPublishRequest.setContent("o'i'f'no'iAfhnoFio oifjw干埃及的是");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments/publish")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSON.toJSONString(commentPublishRequest))
                                .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }


    @Autowired
    UserCommentDao userCommentDao;

    @Test
    void saveTest()
    {
        UserComment userComment=new UserComment();
        userComment.setSendDate(Timestamp.valueOf(LocalDateTime.now()));

        String json= JSON.toJSONString(userComment);

        userCommentDao.save(JSON.parseObject(json,UserComment.class));

    }
}
