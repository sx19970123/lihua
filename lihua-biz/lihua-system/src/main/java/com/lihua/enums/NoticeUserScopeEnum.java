package com.lihua.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知公告用户范围字典（sys_notice_user_scope）枚举：全部用户 / 指定用户
 */
@Getter
@AllArgsConstructor
public enum NoticeUserScopeEnum implements DictEnum {

    /**
     * 全部用户
     */
    ALL("0"),

    /**
     * 指定用户
     */
    DESIGNATED("1");

    private final String value;

    @Override
    public String getType() {
        return "sys_notice_user_scope";
    }
}
