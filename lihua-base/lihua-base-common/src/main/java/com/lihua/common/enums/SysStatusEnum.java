package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统状态字典（sys_status）枚举
 */
@Getter
@AllArgsConstructor
public enum SysStatusEnum implements DictEnum {

    /**
     * 正常
     */
    NORMAL("0"),

    /**
     * 停用
     */
    DISABLED("1");

    private final String value;

    @Override
    public String getType() {
        return "sys_status";
    }

    /**
     * 状态翻转：正常与停用互补
     */
    public static String toggle(String currentStatus) {
        return NORMAL.getValue().equals(currentStatus) ? DISABLED.getValue() : NORMAL.getValue();
    }
}
