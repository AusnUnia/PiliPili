package com.ausn.pilipili.config;

import com.ausn.pilipili.utils.interceptor.LoginInterceptor;
import com.ausn.pilipili.utils.interceptor.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
                        "/user/login",     //use login
                        "/index.html","/", //home page
                        "/pages/**",
                        "/images/**",
                        "/js/**",
                        "/video/**",
                        "/error",
                        "/css/**",
                        "/videos/upload",
                        "/static/**"
                );
    }
}
