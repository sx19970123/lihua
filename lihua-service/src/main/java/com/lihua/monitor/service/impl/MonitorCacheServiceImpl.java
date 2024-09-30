package com.lihua.monitor.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.monitor.model.CacheMonitor;
import com.lihua.monitor.service.MonitorCacheService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lihua.enums.SysBaseEnum.*;

@Service
public class MonitorCacheServiceImpl implements MonitorCacheService {

    @Resource
    private RedisCache<Object> redisCache;

    @Override
    public String memoryInfo() {
        return redisCache.memoryInfo();
    }

    @Override
    public List<CacheMonitor> cacheKeyGroups() {
        return List.of(
            new CacheMonitor(LOGIN_USER_REDIS_PREFIX.getValue(), "登录用户"),
            new CacheMonitor(DICT_DATA_REDIS_PREFIX.getValue(), "系统字典"),
            new CacheMonitor(SYSTEM_SETTING_REDIS_PREFIX.getValue(), "系统设置"),
            new CacheMonitor(SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue(), "IP黑名单"),
            new CacheMonitor(TEMPORARY_TOKEN_REDIS_PREFIX.getValue(), "临时可访问文件"),
            new CacheMonitor("OTHER", "其他")
        );
    }

    @Override
    public Set<String> cacheKeys(String keyPrefix) {
        if (!"OTHER".equals(keyPrefix)) {
            return redisCache.keys(keyPrefix);
        }
        Set<String> keys = redisCache.keys();
        return keys.stream()
                .filter(key -> !LOGIN_USER_REDIS_PREFIX.getValue().equals(key))
                .filter(key -> !DICT_DATA_REDIS_PREFIX.getValue().equals(key))
                .filter(key -> !SYSTEM_SETTING_REDIS_PREFIX.getValue().equals(key))
                .filter(key -> !SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue().equals(key))
                .filter(key -> !TEMPORARY_TOKEN_REDIS_PREFIX.getValue().equals(key))
                .collect(Collectors.toSet());
    }

    @Override
    public CacheMonitor cacheInfo(String key) {
        CacheMonitor cacheMonitor = new CacheMonitor(null, key);
        cacheMonitor.setValue(String.valueOf(redisCache.getCacheObject(key)));
        cacheMonitor.setExpireMinutes(redisCache.getExpireMinutes(key));
        return cacheMonitor;
    }

    @Override
    public void remove(String key) {
        redisCache.delete(key);
    }

    @Override
    public void removeByKeyPrefix(String keyPrefix) {
        Set<String> keys = cacheKeys(keyPrefix);
        keys.forEach(this::remove);
    }
}
