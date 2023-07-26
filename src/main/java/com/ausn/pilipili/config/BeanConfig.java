package com.ausn.pilipili.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/26 22:00
 * @Description:
 */
@Configuration
public class BeanConfig
{
    @Bean
    public Snowflake snowflake()
    {
        return new Snowflake(1, 1);
    }
}
