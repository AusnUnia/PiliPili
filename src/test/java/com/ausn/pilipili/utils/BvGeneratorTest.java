package com.ausn.pilipili.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/26 22:22
 * @Description:
 */

@SpringBootTest
@Slf4j
public class BvGeneratorTest
{
    @Autowired
    BvGenerator bvGenerator;

    @Test
    void test()
    {
        log.info(bvGenerator.generateBv());
        log.info(bvGenerator.generateBv());
        log.info(bvGenerator.generateBv());
        log.info(bvGenerator.generateBv());
        log.info(bvGenerator.generateBv());
        log.info(bvGenerator.generateBv());
    }
}
