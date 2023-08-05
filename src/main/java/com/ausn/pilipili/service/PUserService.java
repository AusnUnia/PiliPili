package com.ausn.pilipili.service;

import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.entity.dto.LoginFormDTO;

public interface PUserService
{
    Result login(LoginFormDTO loginFormDTO);

    Result sendVerificationCode(String phoneNumber);
}
