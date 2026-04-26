package com.lihua.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求上下文信息
 */
@Data
@AllArgsConstructor
public class RequestContext {

    /**
     * 请求ip
     */
    private String ipAddress;

    /**
     * 请求客户端类型
     */
    private String clientType;

    /**
     * 请求token
     */
    private String token;

}
