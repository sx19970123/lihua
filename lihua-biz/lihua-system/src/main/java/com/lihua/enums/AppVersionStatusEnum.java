package com.lihua.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * App 版本状态字典（app_version_status）枚举：草稿 / 已发布 / 已下线
 */
@Getter
@AllArgsConstructor
public enum AppVersionStatusEnum implements DictEnum {

    /**
     * 草稿
     */
    DRAFT("0"),

    /**
     * 已发布
     */
    PUBLISHED("1"),

    /**
     * 已下线
     */
    OFFLINE("2");

    private final String value;

    @Override
    public String getType() {
        return "app_version_status";
    }
}
