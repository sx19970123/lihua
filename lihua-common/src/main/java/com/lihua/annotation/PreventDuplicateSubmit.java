package com.lihua.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 接口防抖
 * 防重复提交
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicateSubmit {

    /**
     * 时间间隔 默认 1000 毫秒
     * 1000 毫秒内，同一用户访问了同一接口多次，服务器只会处理第一次请求，其余将拒绝
     */
    long value() default 1000;

    /**
     * 时间单位，默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
