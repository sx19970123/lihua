package com.lihua.system.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 重置密码接受实体
 */
@Data
public class SysRegisterDTO {

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
     * 密码解密的 requestKey
     */
    private String passwordRequestKey;

    /**
     * 确认密码
     */
    @NotNull(message = "请再次输入密码")
    private String confirmPassword;

    /**
     * 确认密码解密的 requestKey
     */
    private String confirmPasswordRequestKey;

    /**
     * 验证码
     */
    private String captchaVerification;
}
