package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户注册类型字典（sys_user_register_type）枚举
 */
@Getter
@AllArgsConstructor
public enum RegisterTypeEnum implements DictEnum {

    /**
     * 管理员新增
     */
    ADMIN_CREATE("0"),

    /**
     * 用户自助注册
     */
    SELF_REGISTER("1"),

    /**
     * 导入新增
     */
    IMPORT("2");

    private final String value;

    @Override
    public String getType() {
        return "sys_user_register_type";
    }
}
