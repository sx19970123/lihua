package com.lihua.log.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志执行状态字典（sys_log_status）枚举
 */
@Getter
@AllArgsConstructor
public enum LogStatusEnum implements DictEnum {

    /**
     * 成功
     */
    SUCCESS("0"),

    /**
     * 失败
     */
    ERROR("1");

    private final String value;

    @Override
    public String getType() {
        return "sys_log_status";
    }
}
