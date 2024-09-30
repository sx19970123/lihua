package com.lihua.monitor.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CacheMonitor {

    /**
     * 缓存key前缀
     */
    private final String keyPrefix;

    /**
     * 缓存key 标签
     */
    private final String label;

    /**
     * 缓存 Key
     */
    @NotNull(message = "缓存key不存在")
    private String key;

    /**
     * 缓存值
     */
    private String value;

    /**
     * 缓存过期时间（分钟）
     */
    private Long expireMinutes;
}
