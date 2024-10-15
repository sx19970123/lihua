package com.lihua.annotation;

import com.lihua.enums.LogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统接口日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 模块描述
     */
    String description();

    /**
     * 操作类型
     */
    LogTypeEnum type();

    /**
     * 排除参数
     */
    String[] excludeParams() default {};

    /**
     * 记录返回结果
     */
    boolean recordResult() default true;
}
