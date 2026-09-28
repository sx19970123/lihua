package com.lihua.web.filter;

import com.lihua.common.enums.CustomHttpHeader;
import com.lihua.common.utils.trace.TraceIdUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 链路追踪过滤器：请求到达即生成 traceId 写入 MDC 供日志 pattern 输出。
 * 不读取入站 Trace-Id 头——mono 单进程链路无上游生成点，入站头纯属外部不可信输入
 * （外部可携带含换行的任意值伪造日志行）；traceId 恒为本系统产物
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceId = TraceIdUtils.generateTraceId();
        MDC.put(TraceIdUtils.MDC_KEY, traceId);
        // 响应头回写（前端可拿到本次请求的 traceId 便于排障定位）
        response.setHeader(CustomHttpHeader.TRACE_ID.getValue(), traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceIdUtils.MDC_KEY);
        }
    }
}
