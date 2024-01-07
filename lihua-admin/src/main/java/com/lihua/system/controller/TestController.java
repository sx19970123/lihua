package com.lihua.system.controller;

import com.lihua.model.ControllerResult;
import com.lihua.utils.security.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController extends ControllerResult {

    @GetMapping("getLoginUser")
    public String test() {
        throw new RuntimeException("当前账户已过期");
    }

    @GetMapping("getLoginUser2")
    @PreAuthorize("hasAuthority('insert2')")
    public String test2() {
        String username = SecurityUtils.getUsername();
        return success(username);
    }
}
