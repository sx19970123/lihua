package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.cache.RedisCache;
import com.lihua.system.entity.SysSetting;
import com.lihua.system.mapper.SysSettingMapper;
import com.lihua.system.service.SysSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lihua.enums.SysBaseEnum.SYSTEM_SETTING_REDIS_PREFIX;

@Service
public class SysSettingServiceImpl implements SysSettingService {

    @Resource
    private SysSettingMapper sysSettingMapper;

    @Resource
    private RedisCache redisCache;

    private final String REDIS_SETTING_KEY = SYSTEM_SETTING_REDIS_PREFIX.getValue();

    @Override
    public String insert(SysSetting sysSetting) {
        sysSettingMapper.insert(sysSetting);
        // 新增后重新缓存
        reCacheSetting(findList());
        return sysSetting.getId();
    }

    @Override
    public List<SysSetting> findList() {
        Object cacheObject = redisCache.getCacheObject(REDIS_SETTING_KEY);

        // 缓存中数据为空则查询数据库后进行缓存
        if (cacheObject == null) {
            List<SysSetting> sysSettings = sysSettingMapper.selectList(new QueryWrapper<>());
            reCacheSetting(sysSettings);
            return sysSettings;
        }

        return (List<SysSetting>) cacheObject;
    }

    // 重新缓存系统设置
    private void reCacheSetting(List<SysSetting> sysSettings) {
        redisCache.delete(REDIS_SETTING_KEY);
        redisCache.setCacheObject(REDIS_SETTING_KEY, sysSettings);
    }
}
