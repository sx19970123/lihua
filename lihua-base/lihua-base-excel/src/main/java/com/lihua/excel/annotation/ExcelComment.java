package com.lihua.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单元格批注（填表指引，仅挂表头行）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelComment {

    /**
     * 批注内容
     */
    String value() default "";

    /**
     * 应用表头位置，多级表头指定
     */
    int headRowNum() default 0;
}
