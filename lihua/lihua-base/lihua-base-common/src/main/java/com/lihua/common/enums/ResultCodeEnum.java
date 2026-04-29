package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定义 controller 统一返回code 和 默认msg
 */
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {

    // 访问成功
    SUCCESS (200,"成功"),
    // 请求参数/权限/资源异常，4xx
    PARAMS_ERROR(400,"参数异常"),
    AUTHENTICATION_EXPIRED(401,"身份验证过期，请重新登录"),
    PARAMS_MISSING(402,"参数缺失或不完整"),
    ACCESS_ERROR (403,"用户权限不足"),
    RESOURCE_NOT_FOUND_ERROR(404,"请求的资源不存在"),
    REQUEST_METHOD_ERROR(405,"请求方法异常"),
    ACCESS_EXPIRED_ERROR (406,"请求资源权限过期"),
    IP_ILLEGAL_ERROR(407, "暂时无法为该地区提供服务"),
    // 后台业务异常，5xx开始
    ERROR (500,"业务异常"),
    SYSTEM_ERROR (501,"系统异常"),
    SERVER_BAD_ERROR (502,"服务不可用"),
    BAD_GATEWAY_ERROR (503,"网关异常"),
    SERVER_UNAVAILABLE (504,"服务器维护中"),
    FILE_ERROR (505,"附件处理异常"),
    MAX_UPLOAD_SIZE_EXCEEDED_ERROR (506,"上传的附件超过了允许的最大大小限制"),
    CAPTCHA_ERROR(507,"验证码错误"),
    SENSITIVE_ERROR(508,"数据脱敏异常"),
    EXCEL_IMPORT_ERROR (509,"Excel导入异常"),
    EXCEL_EXPORT_ERROR (510,"Excel导出异常");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 默认 msg
     */
    private final String defaultMsg;

}
