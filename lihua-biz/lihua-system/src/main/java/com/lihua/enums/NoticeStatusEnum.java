package com.lihua.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知公告状态字典（sys_notice_status）枚举：未发布 / 已发布 / 已撤销
 */
@Getter
@AllArgsConstructor
public enum NoticeStatusEnum implements DictEnum {

    /**
     * 未发布
     */
    UNPUBLISHED("0"),

    /**
     * 已发布
     */
    RELEASED("1"),

    /**
     * 已撤销
     */
    REVOKED("2");

    private final String value;

    @Override
    public String getType() {
        return "sys_notice_status";
    }
}
