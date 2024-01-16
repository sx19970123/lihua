package com.lihua.system.controller;

import com.lihua.model.ControllerResult;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController extends ControllerResult {

    @Resource
    private SysMenuService sysMenuService;

    @GetMapping
    String test() {
        return success(sysMenuService.selectSysMenuByLoginUser());
    }
}
