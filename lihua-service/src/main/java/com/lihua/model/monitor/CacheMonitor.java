package com.lihua.model.monitor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CacheMonitor {

    /**
     * 缓存key前缀
     */
    private String keyPrefix;

    /**
     * 缓存key 标签
     */
    private String label;

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

    public CacheMonitor(String keyPrefix, String label) {
        this.keyPrefix = keyPrefix;
        this.label = label;
    }

    public CacheMonitor() {
    }
}
