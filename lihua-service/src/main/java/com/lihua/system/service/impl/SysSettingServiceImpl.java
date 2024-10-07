package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.cache.RedisCache;
import com.lihua.system.entity.SysSetting;
import com.lihua.system.mapper.SysSettingMapper;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.service.SysSettingService;
import com.lihua.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.lihua.enums.SysBaseEnum.SYSTEM_IP_BLACKLIST_REDIS_PREFIX;
import static com.lihua.enums.SysBaseEnum.SYSTEM_SETTING_REDIS_PREFIX;

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
        // 新增后重新缓存
        reCacheSetting(getSettingList());
        return sysSetting.getSettingComponentName();
    }

    @Override
    public List<SysSetting> initSetting() {
        List<SysSetting> sysSettingList = redisCache.getCacheList(REDIS_SETTING_KEY, SysSetting.class);

        if (sysSettingList == null) {
            return getSettingList();
        }

        // 缓存中数据为空则查询数据库后进行缓存
        if (sysSettingList.isEmpty()) {
            return getSettingList();
        }

        return sysSettingList;
    }

    @Override
    public SysSetting getSysSettingByComponentName(String componentName) {
        List<SysSetting> settings = initSetting()
                .stream()
                .filter(item -> componentName.equals(item.getSettingComponentName()))
                .toList();
        if (settings.isEmpty()) {
            return null;
        }

        return settings.get(0);
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
    public List<String> getIpBlackList() {
        List<String> ipBlackList = redisCache.getCacheList(IP_BLACKLIST_KEY, String.class);
        return ipBlackList == null ? new ArrayList<>(): ipBlackList;
    }

    // 查询配置数据后进行缓存
    private List<SysSetting> getSettingList() {
        List<SysSetting> sysSettings = sysSettingMapper.selectList(new QueryWrapper<>());
        reCacheSetting(sysSettings);
        return sysSettings;
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

    // 重新缓存系统设置
    private void reCacheSetting(List<SysSetting> sysSettings) {
        redisCache.delete(REDIS_SETTING_KEY);
        redisCache.setCacheList(REDIS_SETTING_KEY, sysSettings);
        cacheIpBlackList();
    }
}
