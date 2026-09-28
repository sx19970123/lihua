package com.lihua.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册接受实体
 */
@Data
public class SysRegisterDTO {

    /**
     * 用户名
     */
    @NotNull(message = "请输入用户名")
    @Pattern(regexp = "^[a-zA-Z0-9@.]+$", message = "用户名只允许大小写英文、数字、@、.")
    @Size(max = 30, message = "用户名长度不能超过30位")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "请输入密码")
    @Size(min = 6, max = 30, message = "密码长度6-30位")
    private String password;

    /**
     * 确认密码
     */
    @NotNull(message = "请再次输入密码")
    private String confirmPassword;

    /**
     * 验证码
     */
    private String captchaVerification;
}
