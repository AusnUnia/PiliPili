package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.VideoCoin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/30 13:38
 * @Description:
 */
@SpringBootTest
@Slf4j
public class VideoCoinDaoTest
{
    @Autowired
    VideoCoinDao videoCoinDao;

    @Test
    void test()
    {
        VideoCoin videoCoin=videoCoinDao.getLatestByBvAndUserId("11223344557",1L);
        log.info(videoCoin.toString());
    }
}
