package com.ausn.pilipili.utils.interceptor;

import com.ausn.pilipili.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //determine whether there is the user
        System.out.println("login interceptor:"+request.getRequestURI());
        if(UserHolder.getUser()==null)
        {
            System.out.println("login interceptor:用户不存在！"+request.getRequestURI());
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
