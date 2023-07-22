package com.ausn.pilipili.utils.redis;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/23 0:35
 * @Description:
 */
public class RedisCacheClient
{
    public <T,R> R queryWithBloomFilterAndMutex(String redisKey, Class<R> dataType, Long time, TimeUnit timeUnit, Function<T,R> dbFallBack, T param)
    {
        // TODO
        return null;
    }
}
