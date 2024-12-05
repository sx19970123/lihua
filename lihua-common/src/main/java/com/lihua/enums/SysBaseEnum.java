package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysBaseEnum {
    /**
     * 用户登录成功redis前缀
     */
    LOGIN_USER_REDIS_PREFIX("REDIS_CACHE_LOGIN_USER:"),

    /**
     * 字典数据redis前缀
     */
    DICT_DATA_REDIS_PREFIX("REDIS_CACHE_DICT_DATA:"),

    /**
     * 系统设置redis前缀
     */
    SYSTEM_SETTING_REDIS_PREFIX("REDIS_CACHE_SYSTEM_SETTING:"),

    /**
     * 系统ip黑名单redis前缀
     */
    SYSTEM_IP_BLACKLIST_REDIS_PREFIX("REDIS_CACHE_IP_BLACKLIST:"),

    /**
     * 防重复提交redis前缀
     */
    PREVENT_DUPLICATE_SUBMIT_REDIS_PREFIX("REDIS_CACHE_REQUEST_SUBMIT:"),

    /**
     * 验证码redis前缀
     */
    CAPTCHA_REDIS_PREFIX("REDIS_CACHE_CAPTCHA:"),

    /**
     * 二次验证redis前缀
     */
    SECONDARY_CAPTCHA_REDIS_PREFIX("REDIS_CACHE_SECONDARY_CAPTCHA:"),

    /**
     * redis缓存临时可访问文件前缀
     */
    TEMPORARY_FILE_REDIS_PREFIX("REDIS_CACHE_TEMPORARY_FILE:"),

    /**
     * JWT 密钥
     */
    JWT_TOKEN_SECRET("c8f2f1fa89b78f4862d6e2a6a8b5b4a5c8e8b7a3a1c0e8b9f1a7e8a6f0b9c8a7c8e7a9b7a5c8f7e4a8b6f9e2b3c7d9f8e6b5a3f2a9d8c7b9a4e8c7d8b9f4e7a9d3b9f6"),

    /**
     * 系统默认密码的加密key和iv 需与前端 src/utils/Crypto.ts 中定义的 DEFAULT_PASSWORD_KEY、DEFAULT_PASSWORD_IV 一致
     */
    DEFAULT_PASSWORD_KEY("lihuaLIHUALIhuam"),

    DEFAULT_PASSWORD_IV("mauhILAUHILauhil");


    private final String value;
}
