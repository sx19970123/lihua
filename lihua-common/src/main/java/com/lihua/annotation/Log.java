package com.lihua.annotation;

import com.lihua.enums.LogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 模块描述
     * @return
     */
    String description();

    /**
     * 操作类型
     * @return
     */
    LogTypeEnum type();

    /**
     * 排除参数
     * @return
     */
    String[] excludeParams() default {};

    /**
     * 记录返回结果
     * @return
     */
    boolean recordResult() default true;
}
