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
    EDITOR_SUCCESS(0,"成功"),
    AUTHENTICATION_EXPIRED(401,"身份验证过期，请重新登陆"),
    PARAMS_MISSING(402,"参数缺失或不完整"),
    ACCESS_ERROR (403,"用户权限不足"),
    RESOURCE_NOT_FOUND_ERROR(404,"请求的资源不存在"),
    PARAMS_ERROR(405,"参数格式异常"),
    ACCESS_EXPIRED_ERROR (406,"请求资源权限过期"),
    IP_ILLEGAL_ERROR(407, "暂时无法为该地区提供服务"),
    ERROR (500,"业务异常"),
    FILE_ERROR (501,"文件处理异常"),
    RATE_LIMITER_ERROR (502,"系统繁忙，请稍后再试"),
    SERVER_UNAVAILABLE (503,"服务器维护中");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 默认 msg
     */
    private final String defaultMsg;


    /**
     * 通过枚举返回默认异常信息
     * @param resultCodeEnum
     * @return
     */
    public static String getDefaultExceptionMessage(ResultCodeEnum resultCodeEnum) {
        return resultCodeEnum.getCode() + "_" + resultCodeEnum.getDefaultMsg();
    }

}
