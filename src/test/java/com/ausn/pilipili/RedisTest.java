package com.ausn.pilipili;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.ausn.pilipili.dao.VideoVoteDao;
import com.ausn.pilipili.entity.VideoVote;
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
public class RedisTest
{
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    VideoVoteDao videoVoteDao;

    @Autowired
    RBloomFilter<String> bloomFilter;

    @Test
    void test()
    {
        String bv="11223344557";
        Long userId=1L;

        VideoVote videoVote = videoVoteDao.getByBvUserId("11223344557", 1L);
        Map<String, Object> videoVoteMap = BeanUtil.beanToMap(
                videoVote,new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((field,val)->val.toString())
        );
        /*stringRedisTemplate.opsForHash().putAll(RedisConstants.VIDEO_VOTE_CACHE_KEY_PREFIX+bv+":"+userId,videoVoteMap);*/
    }

    @Test
    void addBloomFilter()
    {
        String bv="JHSbeRZ9NiG";
        bloomFilter.add("JHSbeRZ9NiG");
    }

}
