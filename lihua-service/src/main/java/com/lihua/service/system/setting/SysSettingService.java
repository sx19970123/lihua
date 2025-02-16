package com.lihua.service.system.setting;

import com.lihua.entity.system.SysSetting;

import java.util.List;

public interface SysSettingService {

    /**
     * 插入设置
     */
    String insert(SysSetting sysSetting);

    /**
     * 初始化配置
     */
    void initSetting();

    /**
     * 根据组件名称获取配置
     */
    SysSetting getSysSettingByComponentName(String componentName);

    /**
     * 是否启用验证码
     */
    boolean enableCaptcha();

    /**
     * 获取ip黑名单
     */
    List<String> getIpBlackList();

    /**
     * 获取默认密码（已解密）
     */
    String getDefaultPassword();

    /**
     * 缓存ip黑名单
     */
    void cacheIpBlackList();
}
