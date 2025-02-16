package com.lihua.strategy.system.authentication;

import com.lihua.model.system.dto.SysSettingDTO;


/**
 * 保存注册用户关联数据
 */
public interface SaveRegisterUserAssociatedStrategy {

    /**
     * 用户注册保存用户关联表信息
     * @param userId 新用户id
     * @param signInSetting 注册配置类
     */
    void saveRegisterUserAssociated(String userId, SysSettingDTO.SignInSetting signInSetting);
}
