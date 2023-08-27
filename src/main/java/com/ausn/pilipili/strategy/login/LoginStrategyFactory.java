package com.ausn.pilipili.strategy.login;

import com.ausn.pilipili.config.LoginTypeConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/27 20:56
 * @Description:
 */
@Component
public class LoginStrategyFactory implements ApplicationContextAware
{
    @Autowired
    private LoginTypeConfig loginTypeConfig;

    private static Map<String,LoginStrategy> strategyMap=new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        loginTypeConfig.getTypes().forEach
        (
            (type,strategyName)->{
                strategyMap.put(type,(LoginStrategy)applicationContext.getBean(strategyName));
            }
        );
    }

    public LoginStrategy getStrategy(String type)
    {
        return strategyMap.get(type);
    }
}
