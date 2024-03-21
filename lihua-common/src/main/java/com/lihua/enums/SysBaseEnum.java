package com.lihua.enums;

import lombok.Getter;

@Getter
public enum SysBaseEnum {
    /**
     * 用户登录成功 redis 缓存前缀
     */
    LOGIN_USER_REDIS_PREFIX("REDIS_CACHE_LOGIN_USER:"),

    /**
     * redis缓存字典数据前缀
     */
    DICT_DATA_REDIS_PREFIX("REDIS_CACHe_DICT_DATA:"),
    /**
     * JWT 密钥
     */
    JWT_TOKEN_SECRET("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890");

    private final String value;

    SysBaseEnum(String value) {
        this.value = value;
    }

}
