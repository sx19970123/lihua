package com.lihua.system.controller;

import com.lihua.system.entity.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("getLoginUser")
    public LoginUser test() {
        LoginUser context = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return context;
    }
}
