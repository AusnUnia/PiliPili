package com.ausn.pilipili.entity.dto;

import lombok.Data;

@Data
public class LoginFormDTO
{
    private String phoneNumber;
    private String verificationCode;
    private String password;
}
