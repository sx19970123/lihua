package com.lihua.web.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * 上下文复制任务装饰器：提交任务时快照 MDC 与 SecurityContext，执行时恢复、结束后清理
 * （traceId、token/IP 归属等上下文随异步任务传播，且池化执行器复用线程时不串任务、无残留）
 * <p>
 * 这是异步线程获取调用方上下文的唯一通道——不经 executor 的线程（手动 new Thread、
 * commonPool 等）明确无上下文，需要时经 executor 提交或显式传参，勿依赖线程继承
 */
public class ContextCopyTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> mdcSnapshot = MDC.getCopyOfContextMap();
        SecurityContext securitySnapshot = SecurityContextHolder.getContext();
        return () -> {
            if (mdcSnapshot != null) {
                MDC.setContextMap(mdcSnapshot);
            }
            SecurityContextHolder.setContext(securitySnapshot);
            try {
                runnable.run();
            } finally {
                MDC.clear();
                SecurityContextHolder.clearContext();
            }
        };
    }
}
