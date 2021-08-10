package com.yjxxt.crm;

import com.alibaba.fastjson.JSON;
import com.yjxxt.crm.annotation.PermissionRequired;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.exceptions.PermissionException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("");
        mav.addObject("code",500);
        mav.addObject("msg", "服务器错误");
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            if(e instanceof NoLoginException){
                mav.setViewName("redirect:/index");
                return mav;
            }
            if(e instanceof PermissionException){
                mav.setViewName("redirect:/less_permission");
                mav.addObject("msg", "权限不足");
                return mav;
            }
            if(null == responseBody){
                // handler上面没有ResponseBody注解，返回视图
                if(e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    mav.addObject("code", pe.getCode());
                    mav.addObject("msg", pe.getMsg());
                }

                return mav;
            }else{
                ResultInfo res = new ResultInfo();
                if(e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    res.setCode(pe.getCode());
                    res.setMsg(pe.getMsg());
                }else{
                    res.setCode(500);
                    res.setMsg("服务器错误");
                }
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = null;
                try {
                    out = resp.getWriter();
                    out.write(JSON.toJSONString(res));
                    out.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    if(null != out){
                        out.close();
                    }
                }
                return null;
            }
        }

        return mav;
    }
}
