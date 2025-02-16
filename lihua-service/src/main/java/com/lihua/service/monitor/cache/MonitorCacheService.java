package com.lihua.service.monitor.cache;


import com.lihua.model.monitor.CacheMonitor;

import java.util.List;
import java.util.Set;

public interface MonitorCacheService {

    /**
     * 内存占用
     */
    String memoryInfo();

    /**
     * 缓存组列表
     */
    List<CacheMonitor> cacheKeyGroups();

    /**
     * 根据key前缀获取所有相关 key
     */
    Set<String> cacheKeys(String keyPrefix);

    /**
     * 根据key获取缓存信息
     */
    CacheMonitor cacheInfo(String key);

    /**
     * 根据前缀删除缓存
     */
    void remove(String keyPrefix);
}
