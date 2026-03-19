package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定义 controller 统一返回code 和 默认msg
 */
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {

    SUCCESS (200,"成功"),
    PARAMS_ERROR(400,"参数异常"),
    AUTHENTICATION_EXPIRED(401,"身份验证过期，请重新登录"),
    PARAMS_MISSING(402,"参数缺失或不完整"),
    ACCESS_ERROR (403,"用户权限不足"),
    RESOURCE_NOT_FOUND_ERROR(404,"请求的资源不存在"),
    REQUEST_METHOD_ERROR(405,"请求方法异常"),
    ACCESS_EXPIRED_ERROR (406,"请求资源权限过期"),
    IP_ILLEGAL_ERROR(407, "暂时无法为该地区提供服务"),
    ERROR (500,"业务异常"),
    FILE_ERROR (501,"附件处理异常"),
    SERVER_BAD_ERROR (503,"服务不可用"),
    MAX_UPLOAD_SIZE_EXCEEDED_ERROR (504,"上传的附件超过了允许的最大大小限制"),
    SERVER_UNAVAILABLE (505,"服务器维护中"),
    CAPTCHA_ERROR(506,"验证码错误"),
    SENSITIVE_ERROR(507,"数据脱敏异常"),
    WEBSOCKET_SEND_MSG_ERROR (509,"websocket发送消息异常"),
    EXCEL_IMPORT_ERROR (510,"Excel导入异常");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 默认 msg
     */
    private final String defaultMsg;

}
