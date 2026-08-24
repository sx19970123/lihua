package com.lihua.cache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis 发布订阅模式topic
 */
@Getter
@AllArgsConstructor
public enum RedisTopicEnum {
    /**
     * 清除本地缓存
     */
    INVALIDATE_LOCAL_CACHE("invalidate_local_cache");

    private final String value;
}
