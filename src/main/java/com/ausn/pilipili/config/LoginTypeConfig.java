package com.ausn.pilipili.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/27 21:02
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "login")
@Data
public class LoginTypeConfig
{
    private HashMap<String,String> types;

}
