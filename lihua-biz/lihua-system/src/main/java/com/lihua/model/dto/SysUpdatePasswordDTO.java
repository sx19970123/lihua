package com.lihua.model.dto;

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
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    private String newPassword;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;

}
