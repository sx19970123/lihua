package com.lihua.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码接收实体
 */
@Data
public class SysUpdatePasswordDTO {
    /**
     * 旧密码（不设长度下限：历史密码或默认密码可能短于新密码规则）
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度为6-30字符")
    private String newPassword;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度为6-30字符")
    private String confirmPassword;

}
