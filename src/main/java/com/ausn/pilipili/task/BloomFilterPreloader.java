package com.ausn.pilipili.task;

import com.ausn.pilipili.dao.VideoDao;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/5 17:33
 * @Description:
 */

@Component
public class BloomFilterPreloader implements ApplicationRunner
{

    private final RBloomFilter bloomFilter;
    private VideoDao videoDao;

    @Autowired
    public BloomFilterPreloader(RBloomFilter bloomFilter,VideoDao videoDao)
    {
        this.bloomFilter=bloomFilter;
        this.videoDao=videoDao;
    }


    @Override
    public void run(ApplicationArguments args)
    {
        // 执行预热逻辑，向布隆过滤器添加初始数据
        // 这里可以根据具体需求自定义需要添加的数据

        int pageSize = 100;
        int page = 0;
        boolean done = false;

        while (!done) {
            // 分页查询数据
            List<String> bvList = videoDao.getBvByPage(page * pageSize, pageSize);

            System.out.println("正在预热布隆过滤器......  "+"已预热"+page * pageSize+"条数据.");

            // 处理当前页的数据
            for (String bv : bvList)
            {
                bloomFilter.add(bv);
            }

            // 判断是否还有下一页
            if (bvList.size() < pageSize)
            {
                done = true;
            }
            else
            {
                page++;
            }
        }
    }
}
