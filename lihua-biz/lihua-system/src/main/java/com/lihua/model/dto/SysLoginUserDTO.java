package com.lihua.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户登录DTO
 */
@Data
public class SysLoginUserDTO {

    /**
     * 用户名
     */
    @NotNull(message = "请输入用户名")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "请输入密码")
    private String password;

    /**
     * 验证码
     */
    private String captchaVerification;
}
