package com.lihua.system.service;

import com.lihua.system.entity.SysSetting;

import java.util.List;

public interface SysSettingService {

    /**
     * 插入设置
     * @param sysSetting
     * @return
     */
    String insert(SysSetting sysSetting);

    /**
     * 初始化配置
     * @return
     */
    List<SysSetting> initSetting();

    /**
     * 根据组件名称获取配置
     * @param componentName
     * @return
     */
    SysSetting getSysSettingByComponentName(String componentName);

    /**
     * 是否启用验证码
     * @return
     */
    boolean enableCaptcha();

    /**
     * 获取ip黑名单
     * @return
     */
    List<String> getIpBlackList();

    /**
     * 缓存ip黑名单
     */
    void cacheIpBlackList();
}
