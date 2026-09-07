package com.lihua.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 6, max = 30, message = "密码长度6-30位")
    private String password;

    /**
     * 用户密码请求key
     */
    private String passwordRequestKey;

}
