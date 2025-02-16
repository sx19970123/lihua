package com.lihua.service.system.profile;

import com.lihua.entity.system.SysUser;

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
}
