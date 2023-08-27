package com.ausn.pilipili.config;

import com.ausn.pilipili.utils.interceptor.LoginInterceptor;
import com.ausn.pilipili.utils.interceptor.RefreshTokenInterceptor;
import com.ausn.pilipili.utils.interceptor.ReqLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate));

        registry.addInterceptor(new LoginInterceptor())
            .excludePathPatterns(
                    "/user/code",      // ask for verification code
                    "/user/login",     //user login
                    "/video/**",
                    "/error",
                    "/videos/upload",
                    "/static/**",
                    "/user/register"
            );

        registry.addInterceptor(new ReqLimitInterceptor(stringRedisTemplate))
            .addPathPatterns(
                    "/videos/upvote/**",
                    "/videos/upvoteNum/**",
                    "/videos/downvote**",
                    "/comments/publish**"
            );
    }

}
