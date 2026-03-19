package com.lihua.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Redis-Key前缀枚举
 * 在此枚举中维护的前缀可通过「系统监控」-「缓存监控」进行具体类型的维护
 * 不在此枚举中维护的前缀会在「系统监控」-「缓存监控」的OTHER类型中维护
 */
@Getter
@AllArgsConstructor
public enum RedisKeyPrefixEnum {

    LOGIN_USER_REDIS_PREFIX("REDIS_CACHE_LOGIN_USER:", "登录用户"),

    DICT_DATA_REDIS_PREFIX("REDIS_CACHE_DICT_DATA:", "系统字典"),

    SYSTEM_SETTING_REDIS_PREFIX("REDIS_CACHE_SYSTEM_SETTING:", "系统设置"),

    SYSTEM_IP_BLACKLIST_REDIS_PREFIX("REDIS_CACHE_IP_BLACKLIST:", "IP黑名单"),

    PREVENT_DUPLICATE_SUBMIT_REDIS_PREFIX("REDIS_CACHE_REQUEST_SUBMIT:", "防重复提交"),

    CAPTCHA_TYPE_VALUE_REDIS_PREFIX("captcha:config:", "验证码缓存"),

    CAPTCHA_REDIS_PREFIX("REDIS_CACHE_CAPTCHA:", "验证码验证中"),

    SECONDARY_CAPTCHA_REDIS_PREFIX("REDIS_CACHE_SECONDARY_CAPTCHA:", "验证码二次验证"),

    CHUNK_UPLOAD_ID_REDIS_PREFIX("REDIS_CACHE_CHUNK_UPLOAD_ID:", "分片上传uploadId"),

    CHECK_PASSWORD_REDIS_PREFIX("REDIS_CACHE_CHECK_PASSWORD:", "检测密码"),

    ONCE_TOKEN_REDIS_PREFIX("REDIS_CACHE_ONCE_TOKEN:", "一次性令牌"),

    // 业务需要，非真实 redis key
    OTHER("OTHER", "其他");

    private final String value;
    private final String label;

    /**
     * 获取全部枚举
     */
    public static List<RedisKeyPrefixEnum> getRedisKeyPrefix() {
        return new ArrayList<>(Arrays.asList(values()));
    }
}
