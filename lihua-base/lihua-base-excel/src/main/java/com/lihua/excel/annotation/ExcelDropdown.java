package com.lihua.excel.annotation;

import com.lihua.excel.enums.DropdownTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单元格下拉菜单
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelDropdown {

    /**
     * 下拉类型
     * 在 DropdownTypeEnum 中定义
     * 在 DropdownWriteHandler 中进行逻辑实现
     */
    DropdownTypeEnum type() default DropdownTypeEnum.CUSTOM;

    /**
     * 下拉类型对应的标识，
     * 例：字典下拉对应字典类型编码
     */
    String value() default "";

    /**
     * 自定义下拉对应的下拉数组，type 非 CUSTOM 可不填
     */
    String[] options() default {};

    /**
     * 应用的最大行数
     */
    int max() default 1000;

}
