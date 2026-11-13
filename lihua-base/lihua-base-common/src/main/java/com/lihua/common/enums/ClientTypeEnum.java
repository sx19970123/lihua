package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 客户端类型（Client-Type 请求头的值域）
 */
@Getter
@AllArgsConstructor
public enum ClientTypeEnum {

    WEB("web"),
    APP("app"),
    APP_H5("app_h5"),
    WECHAT_MP("wechat_mp");

    // 类型值
    private final String value;

    // 根据请求头值解析客户端类型，未携带或未知值按 web 端处理
    public static ClientTypeEnum of(String clientType) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(clientType))
                .findFirst()
                .orElse(WEB);
    }

    // 是否为 app 族客户端
    public boolean isApp() {
        return this != WEB;
    }
}
