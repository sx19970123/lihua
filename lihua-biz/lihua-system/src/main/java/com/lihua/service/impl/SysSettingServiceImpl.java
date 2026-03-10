package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.cache.RedisCache;
import com.lihua.entity.SysSetting;
import com.lihua.mapper.SysSettingMapper;
import com.lihua.model.bridge.setting.CacheBlackIp;
import com.lihua.model.bridge.setting.CacheSetting;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.service.SysSettingService;
import com.lihua.utils.SecurityUtils;
import com.lihua.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lihua.enums.RedisKeyPrefixEnum.SYSTEM_IP_BLACKLIST_REDIS_PREFIX;
import static com.lihua.enums.RedisKeyPrefixEnum.SYSTEM_SETTING_REDIS_PREFIX;

@Service
@Slf4j
public class SysSettingServiceImpl implements SysSettingService {

    @Resource
    private SysSettingMapper sysSettingMapper;

    @Resource
    private RedisCache redisCache;

    private final String REDIS_SETTING_KEY = SYSTEM_SETTING_REDIS_PREFIX.getValue();

    private final String IP_BLACKLIST_KEY = SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue();

    @Override
    @Transactional
    public String insert(SysSetting sysSetting) {
        sysSettingMapper.deleteById(sysSetting.getSettingComponentName());
        sysSettingMapper.insert(sysSetting);
        // redis缓存
        cacheSettingItem(sysSetting);
        // 缓存登录黑名单
        cacheIpBlackList();
        return sysSetting.getSettingComponentName();
    }

    @Override
    public SysSetting getSysSettingByComponentName(String componentName) {
        SysSetting sysSetting = redisCache.getCacheMap(REDIS_SETTING_KEY, SysSetting.class).get(componentName);
        // 从缓存中获取配置不存在时，从数据库查询对应配置
        if (sysSetting == null) {
            SysSetting dbSysSetting = sysSettingMapper.selectById(componentName);
            // 查询到对应配置后，设置缓存并返回
            if (dbSysSetting != null) {
                cacheSettingItem(dbSysSetting);
                return dbSysSetting;
            }
        }
        return sysSetting;
    }

    @Override
    public boolean enableCaptcha() {
        SysSetting verificationCodeSetting = getSysSettingByComponentName("VerificationCodeSetting");

        if (verificationCodeSetting == null) {
            return true;
        }

        // 获取具体配置后解析json返回是否启用验证码
        // 出现任何值为空都认为需要验证码
        String settingJson = verificationCodeSetting.getSettingJson();

        try {
            if (org.springframework.util.StringUtils.hasText(settingJson)) {
                SysSettingDTO sysSettingDTO = JsonUtils.toObject(settingJson, SysSettingDTO.class);

                if (sysSettingDTO == null) {
                    return true;
                }

                return sysSettingDTO.isEnable();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return true;
        }

        return true;
    }

    @Override
    public SysSettingDTO.SignInSetting getSignInSetting() {
        SysSetting setting = getSysSettingByComponentName("SignInSetting");

        if (setting == null) {
            return null;
        }

        // 自助注册配置
        return JsonUtils.toObject(setting.getSettingJson(), SysSettingDTO.SignInSetting.class);
    }

    @Override
    public String getDefaultPassword() {
        SysSetting defaultPasswordSetting = getSysSettingByComponentName("DefaultPasswordSetting");
        // 没有配置默认密码情况下返回 ""
        if (defaultPasswordSetting == null) {
            return "";
        }
        SysSettingDTO.DefaultPasswordSetting passwordSetting = JsonUtils.toObject(defaultPasswordSetting.getSettingJson(), SysSettingDTO.DefaultPasswordSetting.class);
        return passwordSetting.getDefaultPassword();
    }

    // 缓存ip黑名单
    @Override
    public void cacheIpBlackList() {
        redisCache.delete(IP_BLACKLIST_KEY);
        // 系统中配置的禁止访问ip
        SysSetting restrictAccessIpSetting = getSysSettingByComponentName("RestrictAccessIpSetting");
        // 没有此配置项直接返回
        if (restrictAccessIpSetting == null) {
            return;
        }
        SysSettingDTO.RestrictAccessIpSetting ipSetting = JsonUtils.toObject(restrictAccessIpSetting.getSettingJson(), SysSettingDTO.RestrictAccessIpSetting.class);
        // 未开启配置直接返回
        if (!ipSetting.isEnable()) {
            return;
        }
        redisCache.setCacheList(IP_BLACKLIST_KEY, ipSetting.getIpList());
    }

    @EventListener
    public void cacheIpBlackList(CacheBlackIp cacheBlackIp) {
        cacheIpBlackList();
    }

    @EventListener
    public void cacheSetting(CacheSetting cacheSetting) {
        // 重新设置缓存
        reCacheSetting(getSettingMap());
    }

    // 获取系统设置数据
    private Map<String, SysSetting> getSettingMap() {
        // 数据库查询全部设置项
        List<SysSetting> sysSettings = sysSettingMapper.selectList(new QueryWrapper<>());
        // 转为map，key为组件名称，value为配置对象
        return sysSettings.stream().collect(Collectors.toMap(SysSetting::getSettingComponentName, setting -> setting));
    }

    // 重新缓存系统设置
    private void reCacheSetting(Map<String, SysSetting> sysSettingMap) {
        redisCache.delete(REDIS_SETTING_KEY);
        redisCache.setCacheMap(REDIS_SETTING_KEY, sysSettingMap);
        cacheIpBlackList();
    }

    // 缓存某一项系统设置
    private void cacheSettingItem(SysSetting sysSetting) {
        redisCache.setCacheMapItem(REDIS_SETTING_KEY, sysSetting.getSettingComponentName(), sysSetting);
    }
}
