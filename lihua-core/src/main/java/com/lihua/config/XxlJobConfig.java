package com.lihua.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XxlJobConfig {

    @Value("${xxl-job.admin.address}")
    private String adminAddresses;
    
    @Value("${xxl-job.accessToken}")
    private String accessToken;
    
    @Value("${xxl-job.executor.appName}")
    private String appName;
    
    @Value("${xxl-job.executor.address}")
    private String address;
    
    @Value("${xxl-job.executor.ip}")
    private String ip;
    
    @Value("${xxl-job.executor.port}")
    private int port;
    
    @Value("${xxl-job.executor.logPath}")
    private String logPath;
    
    @Value("${xxl-job.executor.logRetentionDays}")
    private int logRetentionDays;
    
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
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
