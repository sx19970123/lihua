package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定义 controller 统一返回code 和 默认msg
 */
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {

    SUCCESS (200,"成功"),
    ERROR (500,"失败"),
    LOGIN_ERROR (501,"用户名或密码错误"),
    AUTHENTICATION_EXPIRED(502,"用户认证过期，请重新登陆"),
    TOKEN_ERROR(503,"token解析异常"),
    ACCESS_ERROR (504,"访问权限不足");


    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 默认 msg
     */
    private final String defaultMsg;
}
