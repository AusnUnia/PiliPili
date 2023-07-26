package com.ausn.pilipili.utils;

import cn.hutool.core.lang.Snowflake;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/26 22:11
 * @Description:
 */

@Component
public class BvGenerator
{
    @Autowired
    private Snowflake snowflake;

    public String generateBv()
    {
        //generate a UUID using snowflake
        long id = snowflake.nextId();
        String bv=longToBv(id);
        return bv;
    }


    private static final String ALPHABET = "0123456789ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz";

    private String longToBv(Long id)
    {

        StringBuilder sb = new StringBuilder();
        int seed= LocalDateTime.now().getSecond();
        HashFunction hashFunction = Hashing.murmur3_128(seed);
        HashCode hashCode= hashFunction.hashLong(id);

        for (int i = 0; i < 11; i++)
        {
            long index = hashCode.asLong() % ALPHABET.length();
            if(index<0)
            {
                index+=ALPHABET.length();
            }
            sb.append(ALPHABET.charAt((int)index));

            hashCode=hashFunction.hashLong(hashCode.asLong());
        }

        return sb.toString();
    }
}
