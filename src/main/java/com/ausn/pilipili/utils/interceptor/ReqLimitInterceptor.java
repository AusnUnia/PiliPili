package com.ausn.pilipili.utils.interceptor;

import com.alibaba.fastjson2.JSON;
import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.common.constants.RedisConstants;
import com.ausn.pilipili.utils.IpUtil;
import com.ausn.pilipili.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/27 1:32
 * @Description:
 */

public class ReqLimitInterceptor implements HandlerInterceptor
{
    private StringRedisTemplate stringRedisTemplate;

    public ReqLimitInterceptor(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String ipAddr = IpUtil.getIpAddr(request);
        String requestURI = request.getRequestURI();
        String key= RedisConstants.REQUEST_LIMIT_KEY_PREFIX+requestURI+":"+ipAddr;
        System.out.println("ReqLimit:"+key);

        String countStr = stringRedisTemplate.opsForValue().get(key);
        if(countStr==null)
        {
            stringRedisTemplate.opsForValue().set(key,"1");
            stringRedisTemplate.expire(key,1, TimeUnit.MINUTES);
            return true;
        }

        Integer count=Integer.parseInt(countStr);
        if(count>=5)
        {
            Result.fail("please try later...");
            PrintWriter writer = response.getWriter();
            writer.append( JSON.toJSONString(Result.fail("please try later...")) );
            return false;
        }

        stringRedisTemplate.opsForValue().increment(key);

        return true;
    }
}
