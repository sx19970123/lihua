package com.lihua.monitor.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.monitor.model.CacheMonitor;
import com.lihua.monitor.service.MonitorCacheService;
import com.lihua.system.service.SysSettingService;
import com.lihua.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lihua.enums.SysBaseEnum.*;

@Service
public class MonitorCacheServiceImpl implements MonitorCacheService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private SysSettingService sysSettingService;


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
            new CacheMonitor(TEMPORARY_FILE_REDIS_PREFIX.getValue(), "临时可访问文件"),
            new CacheMonitor(PREVENT_DUPLICATE_SUBMIT_REDIS_PREFIX.getValue(), "防重复提交"),
            new CacheMonitor(CAPTCHA_REDIS_PREFIX.getValue(), "验证码"),
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
                .filter(key -> !key.startsWith(LOGIN_USER_REDIS_PREFIX.getValue()))
                .filter(key -> !key.startsWith(DICT_DATA_REDIS_PREFIX.getValue()))
                .filter(key -> !key.startsWith(SYSTEM_SETTING_REDIS_PREFIX.getValue()))
                .filter(key -> !key.startsWith(SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue()))
                .filter(key -> !key.startsWith(TEMPORARY_FILE_REDIS_PREFIX.getValue()))
                .filter(key -> !key.startsWith(PREVENT_DUPLICATE_SUBMIT_REDIS_PREFIX.getValue()))
                .filter(key -> !key.startsWith(CAPTCHA_REDIS_PREFIX.getValue()))
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    @Override
    public CacheMonitor cacheInfo(String key) {
        CacheMonitor cacheMonitor = new CacheMonitor(null, key);
        // 获取key在redis中对应的数据类型
        String redisType = redisCache.getRedisType(key);

        switch (redisType) {
            case "string": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCache.getCacheObject(key, Object.class)));
                break;
            }
            case "list": {
                cacheMonitor.setValue(JsonUtils.toJson(redisCache.getCacheList(key, Object.class)));
                break;
            }
            // todo：当业务需要有其他数据类型时，可在此添加
        }

        cacheMonitor.setExpireMinutes(redisCache.getExpireMinutes(key));
        return cacheMonitor;
    }

    @Override
    public void remove(String keyPrefix) {
        Set<String> keys = cacheKeys(keyPrefix);
        for (String key : keys) {
            redisCache.delete(key);
        }

        // 系统配置和 ip 黑名单删除后立即刷新
        if (keyPrefix.startsWith(SYSTEM_SETTING_REDIS_PREFIX.getValue())) {
            sysSettingService.initSetting();
        }

        if ( keyPrefix.startsWith(SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue())) {
            sysSettingService.cacheIpBlackList();
        }
    }
}
