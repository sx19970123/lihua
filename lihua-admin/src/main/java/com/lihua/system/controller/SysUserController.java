package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.LoginUser;
import com.lihua.model.web.ControllerResult;
import com.lihua.system.entity.SysUser;
import com.lihua.system.service.SysUserService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/user")
public class SysUserController extends ControllerResult {

    @Resource
    private SysUserService sysUserService;

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     * @return
     */
    @GetMapping("info")
    public String getUserInfo() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        loginUser.getSysUserVO().setPassword(null);
        return success(loginUser);
    }

    /**
     * 保存基础信息
     * @param sysUser
     * @return
     */
    @PostMapping("basics")
    public String saveBasics(@RequestBody SysUser sysUser) {
        return success(sysUserService.saveBasics(sysUser));
    }

    /**
     * 保存主题
     * @param sysUser
     * @return
     */
    @PostMapping("theme")
    public String saveTheme(@RequestBody SysUser sysUser) {
        return success(sysUserService.saveTheme(sysUser.getTheme()));
    }

    @PostMapping("password")
    public String updatePassword(String oldPassword, String newPassword) {
        if (!SecurityUtils.matchesPassword(oldPassword,LoginUserContext.getSysUser().getPassword())) {
            return error(ResultCodeEnum.ERROR,"旧密码输入错误");
        }
        if (SecurityUtils.matchesPassword(newPassword,LoginUserContext.getSysUser().getPassword())) {
            return error(ResultCodeEnum.ERROR,"新密码不能与旧密码相同");
        }

        return success(sysUserService.updatePassword(newPassword));
    }
}
