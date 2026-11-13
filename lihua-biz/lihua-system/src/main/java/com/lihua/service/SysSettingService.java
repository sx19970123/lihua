package com.lihua.service;

import com.lihua.entity.SysSetting;
import com.lihua.model.dto.SysSettingDTO;

import java.util.List;

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
     * 当前请求端是否启用验证码（app 族客户端走 app 端配置，其余走 web 端）
     */
    boolean enableCaptcha();

    /**
     * 当前请求端的验证码随机类型池（未配置或空池返回空列表，由调用方兜底）
     */
    List<String> getCaptchaTypes();

    /**
     * 是否开启灰色模式
     */
    boolean enableGrayMode();

    /**
     * 是否开启自助注册
     */
    boolean enableSignUp();

    /**
     * 获取同时登录最大数量配置
     * @return -1 无限制
     */
    int getMaxConcurrentLogins();

    /**
     * 获取自助注册配置
     */
    SysSettingDTO.SignUpSetting getSignUpSetting();

    /**
     * 获取定期修改密码配置
     */
    SysSettingDTO.IntervalUpdatePasswordSetting getIntervalUpdatePasswordSetting();

    /**
     * 获取默认密码
     */
    String getDefaultPassword();
}
