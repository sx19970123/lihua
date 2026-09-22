package com.lihua.monitor.service.impl;

import com.lihua.common.exception.ServiceException;
import com.lihua.common.model.bridge.setting.CacheBlackIp;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.monitor.model.CacheMonitor;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.cache.publisher.RedisPublisher;
import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.cache.enums.RedisTopicEnum;
import com.lihua.monitor.service.MonitorCacheService;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lihua.cache.enums.RedisKeyPrefixEnum.CAPTCHA_TYPE_VALUE_REDIS_PREFIX;
import static com.lihua.cache.enums.RedisKeyPrefixEnum.SYSTEM_IP_BLACKLIST_REDIS_PREFIX;

@Service
public class MonitorCacheServiceImpl implements MonitorCacheService {

    @Resource
    private RedisCacheManager redisCacheManager;

    @Resource
    private RedisPublisher redisPublisher;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public String memoryInfo() {
        return redisCacheManager.memoryInfo();
    }

    @Override
    public List<CacheMonitor> cacheKeyGroups() {
        List<RedisKeyPrefixEnum> redisKeyPrefix = RedisKeyPrefixEnum.getRedisKeyPrefix();
        return redisKeyPrefix.stream().map(keyPrefix -> new CacheMonitor(keyPrefix.getValue(), keyPrefix.getLabel())).toList();
    }

    @Override
    public Set<String> cacheKeys(String keyPrefix) {
        if (!RedisKeyPrefixEnum.OTHER.getValue().equals(keyPrefix)) {
            return redisCacheManager.keys(keyPrefix);
        }

        Set<String> keys = redisCacheManager.keys();
        // 拿到非other的Key
        List<RedisKeyPrefixEnum> redisKeyPrefix = RedisKeyPrefixEnum.getRedisKeyPrefix();
        List<String> notOtherKeys = redisKeyPrefix.stream().map(RedisKeyPrefixEnum::getValue).filter(key -> !RedisKeyPrefixEnum.OTHER.getValue().equals(key)).toList();
        // 从keys中减去非other的Key，拿到other的key
        return keys
                .stream()
                .filter(k -> notOtherKeys.stream().noneMatch(k::startsWith))
                .collect(Collectors.toSet());
    }

    @Override
    public CacheMonitor cacheInfo(String key) {
        CacheMonitor cacheMonitor = new CacheMonitor(null, key);
        // 获取key在redis中对应的数据类型（key 不存在/已过期时返回空串）
        String redisType = redisCacheManager.getRedisType(key);
        if (redisType.isEmpty()) {
            throw new ServiceException("缓存key已失效");
        }

        switch (redisType) {
            case "object", "string": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCacheManager.getCacheObject(key, Object.class)));
                break;
            }
            case "list": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCacheManager.getCacheList(key, Object.class)));
                break;
            }
            case "map": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCacheManager.getCacheMap(key, Object.class)));
                break;
            }
            // 当业务需要有其他数据类型时，可在此添加
        }
        cacheMonitor.setExpireMinutes(redisCacheManager.getExpireMinutes(key));
        return cacheMonitor;
    }

    @Override
    public void remove(String keyPrefix) {
        if (keyPrefix.startsWith(CAPTCHA_TYPE_VALUE_REDIS_PREFIX.getValue())) {
            throw new ServiceException("验证码缓存不可删除");
        }

        // 黑名单走全量重建（重建侧先算后写，失败时旧名单保留且异常向上传播），不本地预删
        if (keyPrefix.startsWith(SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue())) {
            applicationEventPublisher.publishEvent(new CacheBlackIp());
            return;
        }

        Set<String> keys = cacheKeys(keyPrefix);
        // 发送缓存失效广播（订阅端按精确 key 失效本地缓存，前缀删除须逐 key 发送）
        keys.forEach(key -> redisPublisher.send(RedisTopicEnum.INVALIDATE_LOCAL_CACHE.getValue(), key));
        redisCacheManager.delete(keys);
    }
}
