package com.lihua.system.controller;

import com.lihua.system.entity.SysUser;
import com.lihua.system.service.SysLoginService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/login")
public class SystemLoginController {
    @Resource
    private SysLoginService sysLoginService;

    @PostMapping
    public String login(@RequestBody SysUser sysUser) {
        return sysLoginService.login(sysUser);
    }
}
