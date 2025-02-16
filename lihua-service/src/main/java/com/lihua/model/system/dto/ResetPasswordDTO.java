package com.lihua.model.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDTO {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不存在")
    private String userId;

    /**
     * 用户密码
     */
    @NotNull(message = "用户密码不存在")
    private String password;

    /**
     * 用户密码请求key
     */
    private String passwordRequestKey;

}
