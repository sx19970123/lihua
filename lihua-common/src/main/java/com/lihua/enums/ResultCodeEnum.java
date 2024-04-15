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
    PARAMS_ERROR(402,"参数缺失或不完整"),
    ACCESS_ERROR (403,"访问权限不足"),
    RESOURCE_NOT_FOUND_ERROR(404,"请求的资源不存在"),

    ERROR (500,"业务异常"),
    SERVER_UNAVAILABLE (503,"服务器维护中"),

    @Deprecated
    PRIMARY_KEY_IS_EMPTY(507,"主键为空"),
    @Deprecated
    PRIMARY_KEY_COLLECTION_IS_EMPTY(508,"集合元素为空");


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
