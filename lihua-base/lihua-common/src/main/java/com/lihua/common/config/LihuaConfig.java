package com.lihua.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 全局通用配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "lihua")
public class LihuaConfig {

    /**
     * 系统版本
     */
    private String version;

    /**
     * redis 中 token 过期时间
     */
    private Long tokenExpireTime;

    /**
     * token 刷新阈值
     */
    private Integer refreshThreshold;

}
