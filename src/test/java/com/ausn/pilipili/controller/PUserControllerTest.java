package com.ausn.pilipili.controller;

import com.alibaba.fastjson2.JSON;
import com.ausn.pilipili.entity.dto.LoginFormDTO;
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

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/26 23:46
 * @Description:
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class PUserControllerTest
{
    @Autowired
    MockMvc mockMvc;



    @Test
    void loginTest() throws Exception {

        LoginFormDTO loginFormDTO=new LoginFormDTO();
        loginFormDTO.setType("password");
        loginFormDTO.setPhoneNumber("17300987810");
        loginFormDTO.setPassword("123456");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSON.toJSONString(loginFormDTO))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void registerTest() throws Exception {

        LoginFormDTO loginFormDTO=new LoginFormDTO();
        loginFormDTO.setPhoneNumber("17300980000");
        loginFormDTO.setPassword("123456");
        loginFormDTO.setVerificationCode("572547");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSON.toJSONString(loginFormDTO))

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void codeTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/code?phone=17300980000")

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

}
