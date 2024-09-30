package com.lihua.monitor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CacheMonitor {

    /**
     * 缓存key
     */
    private final String keyPrefix;

    /**
     * 缓存key 标签
     */
    private final String label;

    /**
     * 缓存值
     */
    private String value;

    /**
     * 缓存过期时间（分钟）
     */
    private Long expireMinutes;
}
