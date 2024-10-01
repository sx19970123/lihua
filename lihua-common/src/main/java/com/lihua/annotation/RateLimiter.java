package com.lihua.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统接口限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    /**
     * 默认每秒放入桶中的令牌数
     * 同一请求每秒超过 limitNumber 次之后的请求全部拒绝
     */
    double value() default 5;
}
