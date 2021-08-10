package com.yjxxt.crm.aop;

import com.yjxxt.crm.annotation.PermissionRequired;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.exceptions.PermissionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionAop{

    @Autowired
    private HttpSession session;

    @Around(value = "@annotation(com.yjxxt.crm.annotation.PermissionRequired)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        List<String> aclVal = (List<String>) session.getAttribute("aclVal");
        if(aclVal == null || aclVal.size() == 0){
            throw new NoLoginException();
        }
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        // 获取注解
        PermissionRequired pr = methodSignature.getMethod().getDeclaredAnnotation(PermissionRequired.class);
        if(pr != null ){
            // 如果存在注解
            if(!aclVal.contains(pr.value())){
                throw new PermissionException();
            }
        }
        Object proceed = pjp.proceed();
        return proceed;
    }
}
