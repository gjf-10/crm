package com.yjxxt.crm.interceptor;

import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Interceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if(userId == 0 || userService.selectByPrimaryKey(userId) == null){
            throw new NoLoginException();
        }
        return true;
    }
}
