package com.lihua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局通用配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "lihua")
public class LihuaConfig {

    /**
     * redis 中 token 过期时间
     */
    private Long expireTime;

}
