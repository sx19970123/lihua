package com.lihua.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lihua.cache.enums.RedisTopicEnum;
import com.lihua.cache.publisher.RedisPublisher;
import com.lihua.common.model.bridge.setting.CacheBlackIp;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.entity.SysSetting;
import com.lihua.enums.SysSettingEnum;
import com.lihua.mapper.SysSettingMapper;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.service.SysSettingService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.lihua.cache.enums.RedisKeyPrefixEnum.SYSTEM_IP_BLACKLIST_REDIS_PREFIX;
import static com.lihua.cache.enums.RedisKeyPrefixEnum.SYSTEM_SETTING_REDIS_PREFIX;

@Service
@Slf4j
public class SysSettingServiceImpl extends ServiceImpl<SysSettingMapper, SysSetting> implements SysSettingService {

    @Resource
    private RedisCacheManager redisCacheManager;

    @Resource
    private RedisPublisher redisPublisher;

    private final String REDIS_SETTING_KEY = SYSTEM_SETTING_REDIS_PREFIX.getValue();

    private final String IP_BLACKLIST_KEY = SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue();

    @Override
    public String saveSetting(SysSetting sysSetting) {
        // 保存数据
        saveOrUpdate(sysSetting);
        // 删除 manager map 中对应属性
        redisCacheManager.removeMapItem(REDIS_SETTING_KEY, sysSetting.getSettingKey());
        // 修改黑名单相关配置时，重新缓存ip黑名单
        if (SysSettingEnum.RESTRICT_ACCESS_IP.getKey().equals(sysSetting.getSettingKey())) {
            cacheIpBlackList();
        }
        return sysSetting.getSettingKey();
    }

    @Override
    public SysSetting getSysSettingByKey(String key) {
        SysSetting sysSetting = redisCacheManager.getCacheMapItem(REDIS_SETTING_KEY, key, SysSetting.class);
        // 从缓存中获取配置不存在时，从数据库查询对应配置
        if (sysSetting == null) {
            SysSetting setting = queryByKey(key);
            // 查询到对应配置后，设置缓存并返回
            if (setting != null) {
                redisCacheManager.setCacheMapItem(REDIS_SETTING_KEY, setting.getSettingKey(), setting);
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
        SysSetting grayModelSetting = getSysSettingByKey(SysSettingEnum.GRAY_MODEL.getKey());
        if (grayModelSetting == null) {
            return false;
        }
        String json = grayModelSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return false;
        }
        SysSettingDTO.GrayModelSetting grayModelSettingDTO = JsonUtils.toObject(json, SysSettingDTO.GrayModelSetting.class);
        // 未开启时直接返回
        if (!grayModelSettingDTO.isEnable()) {
            return false;
        }
        // 关闭时间
        LocalDateTime closeTime = grayModelSettingDTO.getCloseTime();
        // 未设置关闭时间或还未到关闭时间视为开启中，到达关闭时间后视为已关闭
        return closeTime == null || DateUtils.differenceMinute(DateUtils.now(), closeTime) > 0;
    }

    @Override
    public boolean enableSignUp() {
        SysSetting signUpSetting = getSysSettingByKey(SysSettingEnum.SIGN_UP.getKey());
        if (signUpSetting == null) {
            return false;
        }
        String json = signUpSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return false;
        }
        SysSettingDTO sysSettingDTO = JsonUtils.toObject(json, SysSettingDTO.class);
        return sysSettingDTO.isEnable();
    }

    @Override
    public int getMaxConcurrentLogins() {

        SysSetting sameAccountLoginSetting = getSysSettingByKey(SysSettingEnum.SAME_ACCOUNT_LOGIN.getKey());
        if (sameAccountLoginSetting == null) {
            return -1;
        }
        String json = sameAccountLoginSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return -1;
        }
        SysSettingDTO.SameAccountLoginSetting setting = JsonUtils.toObject(json, SysSettingDTO.SameAccountLoginSetting.class);
        if (!setting.isEnable()) {
            return -1;
        }

        return setting.getMaximum() > 0 ? setting.getMaximum() : 1;
    }

    @Override
    public SysSettingDTO.SignUpSetting getSignUpSetting() {
        SysSetting setting = getSysSettingByKey(SysSettingEnum.SIGN_UP.getKey());

        if (setting == null) {
            return null;
        }
        String json = setting.getJson();
        if (!StringUtils.hasText(json)) {
            return null;
        }

        // 自助注册配置
        return JsonUtils.toObject(json, SysSettingDTO.SignUpSetting.class);
    }

    @Override
    public SysSettingDTO.IntervalUpdatePasswordSetting getIntervalUpdatePasswordSetting() {
        SysSetting intervalUpdatePasswordSetting = getSysSettingByKey(SysSettingEnum.INTERVAL_UPDATE_PASSWORD.getKey());
        if (intervalUpdatePasswordSetting == null) {
            return null;
        }
        String json = intervalUpdatePasswordSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return null;
        }

        return JsonUtils.toObject(json, SysSettingDTO.IntervalUpdatePasswordSetting.class);
    }

    @Override
    public String getDefaultPassword() {
        SysSetting defaultPasswordSetting = getSysSettingByKey(SysSettingEnum.DEFAULT_PASSWORD.getKey());
        // 没有配置默认密码情况下返回 ""
        if (defaultPasswordSetting == null) {
            return "";
        }
        String json = defaultPasswordSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return "";
        }
        SysSettingDTO.DefaultPasswordSetting passwordSetting = JsonUtils.toObject(json, SysSettingDTO.DefaultPasswordSetting.class);
        return passwordSetting.getDefaultPassword();
    }

    // 缓存ip黑名单（缓存监控页触发的事件入口）
    @EventListener
    public void cacheIpBlackList(CacheBlackIp cacheBlackIp) {
        cacheIpBlackList();
    }

    // 重建ip黑名单：清除本地与 Redis 缓存后按当前配置回填
    private void cacheIpBlackList() {
        // 清除本地缓存
        redisPublisher.send(RedisTopicEnum.INVALIDATE_LOCAL_CACHE.getValue(), IP_BLACKLIST_KEY);

        redisCacheManager.delete(IP_BLACKLIST_KEY);
        // 系统中配置的禁止访问ip
        SysSetting restrictAccessIpSetting = getSysSettingByKey(SysSettingEnum.RESTRICT_ACCESS_IP.getKey());
        // 没有此配置项直接返回
        if (restrictAccessIpSetting == null) {
            return;
        }
        String json = restrictAccessIpSetting.getJson();
        if (!StringUtils.hasText(json)) {
            return;
        }
        SysSettingDTO.RestrictAccessIpSetting ipSetting = JsonUtils.toObject(json, SysSettingDTO.RestrictAccessIpSetting.class);
        // 未开启配置直接返回
        if (!ipSetting.isEnable()) {
            return;
        }
        // 过滤空白项（关闭态保存的空占位串不应进入黑名单）
        List<String> ipList = ipSetting.getIpList() == null ? List.of()
                : ipSetting.getIpList().stream().filter(StringUtils::hasText).toList();
        if (!ipList.isEmpty()) {
            redisCacheManager.setCacheList(IP_BLACKLIST_KEY, ipList);
        }
    }

    /**
     * 根据Key获取配置信息
     */
    private SysSetting queryByKey(String key) {
        return lambdaQuery().select(SysSetting::getSettingKey, SysSetting::getJson).eq(SysSetting::getSettingKey, key).one();
    }
}
