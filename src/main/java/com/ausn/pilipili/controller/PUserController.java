package com.ausn.pilipili.controller;

import com.ausn.pilipili.entity.dto.LoginFormDTO;
import com.ausn.pilipili.service.PUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class PUserController
{
    @Autowired
    private PUserService pUserService;

    @PostMapping("/code")
    public Result sendVerificationCode(@RequestParam("phone") String phoneNumber)
    {
        return pUserService.sendVerificationCode(phoneNumber);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginFormDTO)
    {
        return pUserService.login(loginFormDTO);
    }


}
