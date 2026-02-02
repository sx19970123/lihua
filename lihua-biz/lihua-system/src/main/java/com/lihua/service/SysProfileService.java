package com.lihua.service;

import com.lihua.entity.SysUser;
import com.lihua.model.dto.SysCheckPasswordDTO;

public interface SysProfileService {

    /**
     * 保存主题
     */
    String saveTheme(String theme);

    /**
     * 保存基础信息
     */
    String saveBasics(SysUser sysUser);

    /**
     * 修改密码
     */
    String updatePassword(String newPassword);

    /**
     * 获取用户密码
     */
    String getPassword();

    /**
     * 用户注销
     */
    void accountDeactivate();

    /**
     * 校验密码
     */
    Boolean checkPassword(SysCheckPasswordDTO sysCheckPasswordDTO);
}
