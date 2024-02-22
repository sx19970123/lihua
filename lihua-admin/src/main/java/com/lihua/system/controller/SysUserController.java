package com.lihua.system.controller;

import com.lihua.model.web.ControllerResult;
import com.lihua.system.entity.SysUser;
import com.lihua.system.service.SysUserService;
import com.lihua.utils.security.LoginUserContext;
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
        return success(LoginUserContext.getLoginUser());
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
}
