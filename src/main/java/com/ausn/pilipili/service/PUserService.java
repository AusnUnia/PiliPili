package com.ausn.pilipili.service;

import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.entity.PUser;
import com.ausn.pilipili.entity.dto.LoginFormDTO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PUserService extends IService<PUser>
{
    Result login(LoginFormDTO loginFormDTO);

    Result sendVerificationCode(String phoneNumber);
}
