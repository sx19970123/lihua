package com.lihua.service.impl;

import com.lihua.common.model.bridge.setting.CacheBlackIp;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.model.CacheMonitor;
import com.lihua.redis.cache.RedisCache;
import com.lihua.redis.enums.RedisKeyPrefixEnum;
import com.lihua.service.MonitorCacheService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MonitorCacheServiceImpl implements MonitorCacheService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public String memoryInfo() {
        return redisCache.memoryInfo();
    }

    @Override
    public List<CacheMonitor> cacheKeyGroups() {
        List<RedisKeyPrefixEnum> redisKeyPrefix = RedisKeyPrefixEnum.getRedisKeyPrefix();
        return redisKeyPrefix.stream().map(keyPrefix -> new CacheMonitor(keyPrefix.getValue(), keyPrefix.getLabel())).toList();
    }

    @Override
    public Set<String> cacheKeys(String keyPrefix) {
        if (!"OTHER".equals(keyPrefix)) {
            return redisCache.keys(keyPrefix);
        }

        Set<String> keys = redisCache.keys();
        // 拿到非other的Key
        List<RedisKeyPrefixEnum> redisKeyPrefix = RedisKeyPrefixEnum.getRedisKeyPrefix();
        List<String> notOtherKeys = redisKeyPrefix.stream().map(RedisKeyPrefixEnum::getValue).filter(key -> !"OTHER".equals(key)).toList();
        // 从keys中减去非other的Key，拿到other的key
        return keys
                .stream()
                .filter(k -> {
                    for (String prefix : notOtherKeys)
                        if (k.startsWith(prefix)) return false;
                    return true;
                })
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    @Override
    public CacheMonitor cacheInfo(String key) {
        CacheMonitor cacheMonitor = new CacheMonitor(null, key);
        // 获取key在redis中对应的数据类型
        String redisType = redisCache.getRedisType(key);

        switch (redisType) {
            case "object", "string": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCache.getCacheObject(key, Object.class)));
                break;
            }
            case "list": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCache.getCacheList(key, Object.class)));
                break;
            }
            case "map": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCache.getCacheMap(key, Object.class)));
                break;
            }
            // 当业务需要有其他数据类型时，可在此添加
        }
        cacheMonitor.setExpireMinutes(redisCache.getExpireMinutes(key));
        return cacheMonitor;
    }

    @Override
    public void remove(String keyPrefix) {
        Set<String> keys = cacheKeys(keyPrefix);
        redisCache.delete(keys);

        // 重新刷新黑名单
        applicationEventPublisher.publishEvent(new CacheBlackIp());
    }
}
