package com.lihua.system.controller;

import com.lihua.system.entity.SysUser;
import com.lihua.system.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/login")
public class SystemLoginController {
    @Resource
    private SysUserService sysUserService;
    @PostMapping
    public String login(@RequestBody SysUser sysUser) {
        String login = sysUserService.login(sysUser);
        return login;
    }
}
