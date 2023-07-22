package com.ausn.pilipili.config;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/23 0:02
 * @Description:
 */
@Configuration
public class RedisConfig
{
    @Bean
    public Redisson redisson()
    {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.107.128:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }

    //add bloom filter to avoid cache penetration
    @Bean
    public RBloomFilter<String> bloomFilter(Redisson redisson)
    {
        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("name_of_the_filter");
        // 初始化布隆过滤器：预计元素为100000000L,期望的误差率为0.01
        bloomFilter.tryInit(100000000L, 0.01);
        return bloomFilter;
    }
}
