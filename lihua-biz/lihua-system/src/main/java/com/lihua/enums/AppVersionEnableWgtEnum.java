package com.lihua.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * App 版本热更新支持字典（app_version_enable_wgt）枚举：否 / 是。
 * 与 sys_whether 值集同为 0/1 但语义不同，按约定独立建字典与枚举，禁止跨字典复用
 */
@Getter
@AllArgsConstructor
public enum AppVersionEnableWgtEnum implements DictEnum {

    /**
     * 不支持热更新
     */
    NO("0"),

    /**
     * 支持热更新
     */
    YES("1");

    private final String value;

    @Override
    public String getType() {
        return "app_version_enable_wgt";
    }
}
