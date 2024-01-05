package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定义 controller 统一返回code 和 默认msg
 */
@AllArgsConstructor
@Getter
public enum AxiosResultCodeEnum {

    SUCCESS (200,"成功"),
    ERROR (500,"失败"),
    LOGIN_ERROR (501,"用户名或密码错误"),
    ACCESS_ERROR (502,"权限不足");


    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 默认 msg
     */
    private final String defaultMsg;
}
