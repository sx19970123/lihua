package com.lihua.system.controller;

import com.lihua.model.ControllerResult;
import com.lihua.utils.security.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/user")
public class SystemUser extends ControllerResult {

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     * @return
     */
    @GetMapping("/info")
    public String getUserInfo() {
        return success(SecurityUtils.getLoginUser());
    }

}
