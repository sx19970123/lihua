package com.lihua.web.config;

import org.springframework.boot.task.SimpleAsyncTaskExecutorCustomizer;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 异步任务基础设施配置
 */
@Configuration
public class AsyncTaskConfig {

    /**
     * @Async 默认线程池（applicationTaskExecutor）追加 MDC 上下文复制——异步日志行保留 traceId
     * （按 Boot 4 customizer 形态两类 executor 各注册一个，池参数仍由自动配置管理）
     */
    @Bean
    public SimpleAsyncTaskExecutorCustomizer mdcSimpleAsyncTaskExecutorCustomizer() {
        return executor -> executor.setTaskDecorator(new MdcTaskDecorator());
    }

    @Bean
    public ThreadPoolTaskExecutorCustomizer mdcThreadPoolTaskExecutorCustomizer() {
        return executor -> executor.setTaskDecorator(new MdcTaskDecorator());
    }
}
