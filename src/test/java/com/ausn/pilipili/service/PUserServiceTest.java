package com.ausn.pilipili.service;

import com.ausn.pilipili.service.PUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PUserServiceTest
{
    @Autowired
    PUserService pUserService;

    @Test
    void sendCodeTest()
    {
        pUserService.sendVerificationCode("17300987810");
    }
}
