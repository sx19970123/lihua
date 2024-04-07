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
    AUTHENTICATION_EXPIRED(401,"请求未经授权，需要身份验证"),
    TOKEN_ERROR(402,"token解析异常"),
    ACCESS_ERROR (403,"访问权限不足"),
    RESOURCE_NOT_FOUND_ERROR(404,"请求的资源不存在"),
    ERROR (500,"业务异常"),
    LOGIN_ERROR (501,"用户名或密码错误"),
    NO_ACCESS_ERROR (502,"请联系管理员分配菜单权限"),
    NO_ROLE_ERROR (503,"请联系管理员分配角色"),
    NO_PERMISSION_ERROR (504,"请联系管理员分配角色或菜单权限"),
    MAINTAIN_ERROR(505,"服务器维护中"),
    DB_ERROR(506,"数据库异常"),
    PRIMARY_KEY_IS_EMPTY(507,"主键为空"),
    PRIMARY_KEY_COLLECTION_IS_EMPTY(508,"集合元素为空"),
    PARAMS_IS_EMPTY(509,"参数不存在");

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
