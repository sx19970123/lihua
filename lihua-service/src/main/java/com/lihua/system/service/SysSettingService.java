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
     * 获取系统设置
     * @return
     */
    List<SysSetting> findList();

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
}
