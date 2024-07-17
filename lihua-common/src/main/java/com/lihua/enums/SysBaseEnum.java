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
    JWT_TOKEN_SECRET("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890");

    private final String value;
}
