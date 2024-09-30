package com.lihua.monitor.service;


import com.lihua.monitor.model.CacheMonitor;

import java.util.List;
import java.util.Set;

public interface MonitorCacheService {

    /**
     * 内存占用
     * @return
     */
    String memoryInfo();

    /**
     * 缓存组列表
     * @return
     */
    List<CacheMonitor> cacheKeyGroups();

    /**
     * 根据key前缀获取所有相关 key
     * @param keyPrefix
     * @return
     */
    Set<String> cacheKeys(String keyPrefix);

    /**
     * 根据key获取缓存信息
     * @param key
     * @return
     */
    CacheMonitor cacheInfo(String key);


    /**
     * 根据前缀删除缓存
     */
    void remove(String keyPrefix);
}
