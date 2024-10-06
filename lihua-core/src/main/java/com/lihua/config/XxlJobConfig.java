package com.lihua.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import groovy.util.logging.Slf4j;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@lombok.extern.slf4j.Slf4j
@Configuration
@Data
@Slf4j
@ConfigurationProperties(prefix = "xxl-job")
public class XxlJobConfig {

    private boolean enable;

    private String adminAddress;

    private String accessToken;
    
    private String appName;
    
    private String address;
    
    private String ip;
    
    private int port;
    
    private String logPath;
    
    private int logRetentionDays;
    
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() {
        if (!enable) {
            log.info("xxl-job 定时任务未开启");
            return null;
        }
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddress);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }
}
