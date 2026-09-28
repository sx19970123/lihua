package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SysSettingEnum {

    /**
     * 验证码
     */
    CAPTCHA("CaptchaSetting"),
    /**
     * 限制访问ip
     */
    RESTRICT_ACCESS_IP("RestrictAccessIpSetting"),
    /**
     * 默认密码
     */
    DEFAULT_PASSWORD("DefaultPasswordSetting"),
    /**
     * 多账号登录
     */
    SAME_ACCOUNT_LOGIN("SameAccountLoginSetting"),
    /**
     * 注册
     */
    SIGN_UP("SignUpSetting"),
    /**
     * 灰色模式
     */
    GRAY_MODEL("GrayModelSetting"),
    /**
     * 定期修改密码
     */
    INTERVAL_UPDATE_PASSWORD("IntervalUpdatePasswordSetting");

    /**
     * 配置项对应的 key
     */
    private final String key;
}
