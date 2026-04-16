package com.lihua.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    /**
     * manager 中 token 过期时间
     */
    private Long tokenExpireTime;

    /**
     * token 刷新阈值
     */
    private Integer refreshThreshold;
}
