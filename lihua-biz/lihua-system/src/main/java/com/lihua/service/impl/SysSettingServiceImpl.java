package com.lihua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.common.model.bridge.setting.CacheBlackIp;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.entity.SysSetting;
import com.lihua.enums.SysSettingEnum;
import com.lihua.mapper.SysSettingMapper;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.redis.cache.RedisCache;
import com.lihua.service.SysSettingService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.lihua.redis.enums.RedisKeyPrefixEnum.SYSTEM_IP_BLACKLIST_REDIS_PREFIX;
import static com.lihua.redis.enums.RedisKeyPrefixEnum.SYSTEM_SETTING_REDIS_PREFIX;

@Service
@Slf4j
public class SysSettingServiceImpl extends ServiceImpl<SysSettingMapper, SysSetting> implements SysSettingService {

    @Resource
    private RedisCache redisCache;

    private final String REDIS_SETTING_KEY = SYSTEM_SETTING_REDIS_PREFIX.getValue();

    private final String IP_BLACKLIST_KEY = SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue();

    @Override
    public String saveSetting(SysSetting sysSetting) {
        // 保存数据
        saveOrUpdate(sysSetting);
        // 删除 redis map 中对应属性
        redisCache.removeMapItem(REDIS_SETTING_KEY, sysSetting.getSettingKey());
        // 修改黑名单相关配置时，重新缓存ip黑名单
        if (SysSettingEnum.RESTRICT_ACCESS_IP.getKey().equals(sysSetting.getSettingKey())) {
            cacheIpBlackList(null);
        }
        return sysSetting.getSettingKey();
    }

    @Override
    public SysSetting getSysSettingByKey(String key) {
        SysSetting sysSetting = redisCache.getCacheMapItem(REDIS_SETTING_KEY, key, SysSetting.class);
        // 从缓存中获取配置不存在时，从数据库查询对应配置
        if (sysSetting == null) {
            SysSetting setting = queryByKey(key);
            // 查询到对应配置后，设置缓存并返回
            if (setting != null) {
                redisCache.setCacheMapItem(REDIS_SETTING_KEY, setting.getSettingKey(), setting);
            }

            return setting;
        }
        return sysSetting;
    }

    @Override
    public boolean enableCaptcha() {
        SysSetting captchaSetting = getSysSettingByKey(SysSettingEnum.CAPTCHA.getKey());
        if (captchaSetting == null) {
            return true;
        }
        // 出现任何值为空都认为需要验证码
        String json = captchaSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return true;
        }
        SysSettingDTO sysSettingDTO = JsonUtils.toObject(json, SysSettingDTO.class);
        return sysSettingDTO.isEnable();
    }

    @Override
    public boolean enableGrayMode() {
        SysSetting captchaSetting = getSysSettingByKey(SysSettingEnum.GRAY_MODEL.getKey());
        if (captchaSetting == null) {
            return false;
        }
        String json = captchaSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return false;
        }
        SysSettingDTO.GrayModelSetting grayModelSetting = JsonUtils.toObject(json, SysSettingDTO.GrayModelSetting.class);
        // 关闭时间
        LocalDateTime closeTime = grayModelSetting.getCloseTime();
        // 指定的过期时间对比
        if (grayModelSetting.isEnable() && closeTime != null && DateUtils.differenceMinute(DateUtils.now(), closeTime) > 0) {
            return true;
        }
        return grayModelSetting.isEnable();
    }

    @Override
    public boolean enableSignUp() {
        SysSetting captchaSetting = getSysSettingByKey(SysSettingEnum.SIGN_UP.getKey());
        if (captchaSetting == null) {
            return false;
        }
        String json = captchaSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return false;
        }
        SysSettingDTO sysSettingDTO = JsonUtils.toObject(json, SysSettingDTO.class);
        return sysSettingDTO.isEnable();
    }

    @Override
    public int getMaxConcurrentLogins() {

        SysSetting captchaSetting = getSysSettingByKey(SysSettingEnum.SAME_ACCOUNT_LOGIN.getKey());
        if (captchaSetting == null) {
            return -1;
        }

        SysSettingDTO.SameAccountLoginSetting setting = JsonUtils.toObject(captchaSetting.getJson(), SysSettingDTO.SameAccountLoginSetting.class);
        if (!setting.isEnable()) {
            return -1;
        }

        return setting.getMaximum() > 0 ? setting.getMaximum() : 1;
    }

    @Override
    public SysSettingDTO.SignInSetting getSignInSetting() {
        SysSetting setting = getSysSettingByKey(SysSettingEnum.SIGN_UP.getKey());

        if (setting == null) {
            return null;
        }

        // 自助注册配置
        return JsonUtils.toObject(setting.getJson(), SysSettingDTO.SignInSetting.class);
    }

    @Override
    public SysSettingDTO.IntervalUpdatePasswordSetting getIntervalUpdatePasswordSetting() {
        SysSetting intervalUpdatePasswordSetting = getSysSettingByKey(SysSettingEnum.INTERVAL_UPDATE_PASSWORD.getKey());
        if (intervalUpdatePasswordSetting == null) {
            return null;
        }

        return JsonUtils.toObject(intervalUpdatePasswordSetting.getJson(), SysSettingDTO.IntervalUpdatePasswordSetting.class);
    }

    @Override
    public String getDefaultPassword() {
        SysSetting defaultPasswordSetting = getSysSettingByKey(SysSettingEnum.DEFAULT_PASSWORD.getKey());
        // 没有配置默认密码情况下返回 ""
        if (defaultPasswordSetting == null) {
            return "";
        }
        SysSettingDTO.DefaultPasswordSetting passwordSetting = JsonUtils.toObject(defaultPasswordSetting.getJson(), SysSettingDTO.DefaultPasswordSetting.class);
        return passwordSetting.getDefaultPassword();
    }

    // 缓存ip黑名单
    @EventListener
    public void cacheIpBlackList(CacheBlackIp cacheBlackIp) {
        redisCache.delete(IP_BLACKLIST_KEY);
        // 系统中配置的禁止访问ip
        SysSetting restrictAccessIpSetting = getSysSettingByKey(SysSettingEnum.RESTRICT_ACCESS_IP.getKey());
        // 没有此配置项直接返回
        if (restrictAccessIpSetting == null) {
            return;
        }
        SysSettingDTO.RestrictAccessIpSetting ipSetting = JsonUtils.toObject(restrictAccessIpSetting.getJson(), SysSettingDTO.RestrictAccessIpSetting.class);
        // 未开启配置直接返回
        if (!ipSetting.isEnable()) {
            return;
        }
        redisCache.setCacheList(IP_BLACKLIST_KEY, ipSetting.getIpList());
    }

    /**
     * 根据Key获取配置信息
     */
    private SysSetting queryByKey(String key) {
        return lambdaQuery().select(SysSetting::getSettingKey, SysSetting::getJson).eq(SysSetting::getSettingKey, key).one();
    }
}
