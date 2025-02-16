package com.lihua.model.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[a-zA-Z0-9@.]+$", message = "用户名只允许大小写英文、数字、@、.")
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
