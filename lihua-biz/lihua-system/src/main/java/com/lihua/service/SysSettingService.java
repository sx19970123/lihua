package com.lihua.service;

import com.lihua.entity.SysSetting;
import com.lihua.model.dto.SysSettingDTO;

public interface SysSettingService {

    /**
     * 保存设置
     */
    String saveSetting(SysSetting sysSetting);

    /**
     * 根据组件名称获取配置
     */
    SysSetting getSysSettingByKey(String key);

    /**
     * 是否启用验证码
     */
    boolean enableCaptcha();
    /**
     * 获取自助注册配置
     */
    SysSettingDTO.SignInSetting getSignInSetting();

    /**
     * 获取默认密码（已解密）
     */
    String getDefaultPassword();
}
