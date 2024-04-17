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
    AUTHENTICATION_EXPIRED(401,"身份验证过期，请重新登陆"),
    PARAMS_MISSING(402,"参数缺失或不完整"),
    PARAMS_ERROR(403,"参数格式异常"),
    RESOURCE_NOT_FOUND_ERROR(404,"请求的资源不存在"),
    ACCESS_ERROR (405,"访问权限不足"),

    ERROR (500,"业务异常"),
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
