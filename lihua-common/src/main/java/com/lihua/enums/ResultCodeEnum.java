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
    RESOURCE_NOT_FOUND_ERROR(404,"接口资源未找到"),
    ERROR (500,"业务异常"),
    LOGIN_ERROR (501,"用户名或密码错误"),
    AUTHENTICATION_EXPIRED(502,"用户认证过期，请重新登陆"),
    TOKEN_ERROR(503,"token解析异常"),
    ACCESS_ERROR (504,"访问权限不足"),
    NO_ACCESS_ERROR (505,"请联系管理员分配菜单"),
    DB_ERROR(506,"数据库异常");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 默认 msg
     */
    private final String defaultMsg;

    /**
     * 通过编码获取状态信息
     * @param code 整数
     * @return
     */
    public static ResultCodeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }

        for (ResultCodeEnum rce : values()) {
            if (code.equals(rce.getCode())) {
                return rce;
            }
        }

        return null;
    }

    /**
     * 通过编码获取状态信息
     * @param code 字符串
     * @return
     */
    public static ResultCodeEnum getByCode(String code) {
        return code == null ? null : getByCode(Integer.valueOf(code));
    }

    /**
     * 通过枚举返回默认异常信息
     * @param resultCodeEnum
     * @return
     */
    public static String getDefaultExceptionMessage(ResultCodeEnum resultCodeEnum) {
        return resultCodeEnum.getCode() + "_" + resultCodeEnum.getDefaultMsg();
    }

    /**
     * 通过默认异常信息返回枚举
     * @param defaultMsg
     * @return
     */
    public static ResultCodeEnum getByDefaultExceptionMessage(String defaultMsg) {
        return getByCode(defaultMsg.split("_")[0]);
    }
}
