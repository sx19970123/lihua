package com.lihua.system.service;

import com.lihua.system.entity.SysUser;

public interface SysUserService {

    /**
     * 保存主题
     * @param theme
     * @return
     */
    String saveTheme(String theme);

    /**
     * 保存基础信息
     * @param sysUser
     * @return
     */
    String saveBasics(SysUser sysUser);

    /**
     * 修改密码
     * @param newPassword
     * @return
     */
    String updatePassword(String newPassword);
}
