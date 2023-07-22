package com.ausn.pilipili.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 * @Author: 付显贵
 * @DateTime: 2023/7/22 23:15
 * @Description: synchronize redis data to mysql on a regular basis
 */

@Component
public class RedisToMysqlTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Scheduled(fixedRate = 60000) //每60秒执行一次
    public void persistData()
    {
        //TODO 编写定时持久化任务
        return;
    }

}
