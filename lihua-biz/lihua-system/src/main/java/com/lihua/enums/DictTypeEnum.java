package com.lihua.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字典类型字典（sys_dict_type）枚举：一般字典 / 树型字典
 */
@Getter
@AllArgsConstructor
public enum DictTypeEnum implements DictEnum {

    /**
     * 一般字典
     */
    GENERAL("0"),

    /**
     * 树型字典
     */
    TREE("1");

    private final String value;

    @Override
    public String getType() {
        return "sys_dict_type";
    }
}
