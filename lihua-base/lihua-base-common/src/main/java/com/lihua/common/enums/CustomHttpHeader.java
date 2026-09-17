package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义http请求头（与 cloud 版同名类同构，值集为 mono 实际消费的子集——
 * cloud 版另含 IP/SIGN/TIMESTAMP 三个网关与内部 RPC 专属头，mono 无对应机制不引入）
 */
@AllArgsConstructor
@Getter
public enum CustomHttpHeader {

    /**
     * 请求类型
     */
    CLIENT_TYPE("Client-Type"),

    /**
     * 链路追踪 id（入口过滤器生成并回写响应头；MDC 键名见 TraceIdUtils.MDC_KEY）
     */
    TRACE_ID("Trace-Id"),

    /**
     * Token
     */
    TOKEN(TokenEnum.TOKEN_KEY.getValue());

    private final String value;

}
