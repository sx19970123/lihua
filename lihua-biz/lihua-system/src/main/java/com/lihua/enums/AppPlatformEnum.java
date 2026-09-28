package com.lihua.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * App 版本平台字典（app_version_platform）枚举：Android / iOS / 鸿蒙。
 * value 取 uni-app 平台标识（条件编译 APP-HARMONY 同源）
 */
@Getter
@AllArgsConstructor
public enum AppPlatformEnum implements DictEnum {

    /**
     * Android
     */
    ANDROID("android"),

    /**
     * iOS
     */
    IOS("ios"),

    /**
     * 鸿蒙
     */
    HARMONYOS("harmony");

    private final String value;

    /**
     * 平台合法性正则（DTO @Pattern 引用；值集单点维护，新增平台只改本枚举）
     */
    public static final String REGEX = "^(android|ios|harmony)$";

    @Override
    public String getType() {
        return "app_version_platform";
    }
}
