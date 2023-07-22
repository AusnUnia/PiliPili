package com.ausn.pilipili.service;

import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.dto.LoginFormDTO;

import javax.servlet.http.HttpSession;

public interface PUserService
{
    Result login(LoginFormDTO loginFormDTO);

    Result sendVerificationCode(String phoneNumber);
}
