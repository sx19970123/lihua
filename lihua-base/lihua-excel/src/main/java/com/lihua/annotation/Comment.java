package com.lihua.annotation;

import com.lihua.enums.CommentUseEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单元格批注
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Comment {

    /**
     * 批注内容
     */
    String value() default "";

    /**
     * 批注应用位置
     * 全部｜表头｜内容
     */
    CommentUseEnum use() default CommentUseEnum.ALL;

    /**
     * 应用表头位置，多级表头指定
     */
    int headRowNum() default 0;
}
