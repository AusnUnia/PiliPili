package com.ausn.pilipili;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.ausn.pilipili.dao.VideoVoteDao;
import com.ausn.pilipili.entity.VideoVote;
import com.ausn.pilipili.strategy.login.LoginStrategy;
import com.ausn.pilipili.strategy.login.LoginStrategyFactory;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/29 20:45
 * @Description:
 */

@SpringBootTest
public class LoginTest
{
    @Autowired
    LoginStrategyFactory loginStrategyFactory;

    @Test
    void test()
    {
        LoginStrategy sms = loginStrategyFactory.getStrategy("sms");
        System.out.println(sms);
        LoginStrategy password = loginStrategyFactory.getStrategy("password");
        System.out.println(password);
    }


}
