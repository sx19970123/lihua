package com.lihua.system.controller;

import com.lihua.model.web.ControllerResult;
import com.lihua.utils.security.LoginUserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/user")
public class SysUser extends ControllerResult {

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     * @return
     */
    @GetMapping("/info")
    public String getUserInfo() {
        return success(LoginUserContext.getLoginUser());
    }

    @PostMapping("/theme1")
    public String saveTheme(String theme) {
        System.out.println(theme);
        return success();
    }

}
