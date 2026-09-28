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
     * 应用的最大行（0-based 结束行）。
     * 默认 1048575 = Excel 单 sheet 最后一行，即下拉约束覆盖至整列末尾，
     * 避免粘贴数据超过固定行数后失去校验；超出 Excel 行上限会生成非法 sqref 触发文件修复，勿调大
     */
    int max() default 1048575;

}
