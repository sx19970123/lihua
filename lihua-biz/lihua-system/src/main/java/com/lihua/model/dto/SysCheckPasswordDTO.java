package com.lihua.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 检查密码DTO
 */
@Data
public class SysCheckPasswordDTO {
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;
}
