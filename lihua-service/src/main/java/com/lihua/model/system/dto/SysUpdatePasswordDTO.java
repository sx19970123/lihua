package com.lihua.model.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改密码接收实体
 */
@Data
public class SysUpdatePasswordDTO {
    /**
     * 旧密码
     */
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 旧密码请求key
     */
    private String oldPasswordRequestKey;
    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    private String newPassword;
    /**
     * 新密码请求key
     */
    private String newPasswordRequestKey;
    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;
    /**
     * 确认密码请求key
     */
    private String confirmPasswordRequestKey;
}
