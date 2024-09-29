package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysBaseEnum {
    /**
     * 用户登录成功redis缓存前缀
     */
    LOGIN_USER_REDIS_PREFIX("REDIS_CACHE_LOGIN_USER:"),

    /**
     * redis缓存字典数据前缀
     */
    DICT_DATA_REDIS_PREFIX("REDIS_CACHE_DICT_DATA:"),

    /**
     * 系统设置redis前缀
     */
    SYSTEM_SETTING_REDIS_PREFIX("REDIS_CACHE_SYSTEM_SETTING:"),

    /**
     * 系统ip黑名单redis前缀
     */
    SYSTEM_IP_BLACKLIST_REDIS_PREFIX("IP_BLACKLIST:"),
    /**
     * redis 分布式锁 key
     */
    REDIS_LOCK("REDIS_LOCK"),
    /**
     * redis缓存临时token前缀
     */
    TEMPORARY_TOKEN_REDIS_PREFIX("REDIS_CACHE_TEMPORARY_TOKEN:"),

    /**
     * JWT 密钥
     */
    JWT_TOKEN_SECRET("c8f2f1fa89b78f4862d6e2a6a8b5b4a5c8e8b7a3a1c0e8b9f1a7e8a6f0b9c8a7c8e7a9b7a5c8f7e4a8b6f9e2b3c7d9f8e6b5a3f2a9d8c7b9a4e8c7d8b9f4e7a9d3b9f6");

    private final String value;
}
