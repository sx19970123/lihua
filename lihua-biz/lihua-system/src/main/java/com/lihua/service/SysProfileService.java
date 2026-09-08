package com.lihua.service;

import com.lihua.model.dto.SysCheckPasswordDTO;
import com.lihua.model.dto.SysProfileBasicDTO;
import com.lihua.model.dto.SysUpdatePasswordDTO;
import java.util.List;

public interface SysProfileService {

    /**
     * 保存主题
     */
    String saveTheme(String theme);

    /**
     * 保存基础信息
     */
    String saveBasics(SysProfileBasicDTO sysProfileBasicDTO);

    /**
     * 修改密码（含旧密码与新密码规则校验，失败抛 ServiceException）
     */
    String updatePassword(SysUpdatePasswordDTO sysUpdatePasswordDTO);

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

    /**
     * 登录后用户数据校验
     * 返回用于完善信息的前端组件名称
     */
    List<String> postLoginCheck();
}
