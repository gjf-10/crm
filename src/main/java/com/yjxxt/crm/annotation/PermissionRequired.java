package com.yjxxt.crm.annotation;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionRequired {
    String value() default "";
}
