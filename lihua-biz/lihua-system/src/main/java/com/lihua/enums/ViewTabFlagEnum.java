package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * view-tab 收藏/固定位标记值（sys_view_tab.star/affix，char(1)；存储层结构标记，不挂字典）
 */
@Getter
@AllArgsConstructor
public enum ViewTabFlagEnum {

    /**
     * 是
     */
    YES("1"),

    /**
     * 否
     */
    NO("0");

    private final String value;
}
