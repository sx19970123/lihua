package com.lihua.system.controller;

import com.lihua.model.web.ControllerResult;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.service.SysAuthenticationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system")
public class SysAuthenticationController extends ControllerResult {
    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @PostMapping("login")
    public String login(@RequestBody SysUserVO sysUserVO) {
        return success(sysAuthenticationService.login(sysUserVO));
    }
}
