package com.lihua.system.controller;

import com.lihua.model.SysUser;
import com.lihua.system.service.SysAuthenticationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system")
public class SystemAuthenticationController {
    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @PostMapping("login")
    public String login(@RequestBody SysUser sysUser) {
        return sysAuthenticationService.login(sysUser);
    }

    @PostMapping("logout")
    public String logout() {
        sysAuthenticationService.logout();
        return null;
    }
}
