package com.lihua.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    /**
     * manager 中 token 过期时间（缺省 1 小时；Duration 带单位写法如 1h/30m，裸数字按分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration tokenExpireTime = Duration.ofMinutes(60);

    /**
     * token 刷新阈值（距过期不足该阈值时新请求触发刷新；Duration 带单位写法，裸数字按分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration refreshThreshold = Duration.ofMinutes(15);

    /**
     * JWT 签发/验签密钥（HMAC-SHA256；换值=存量 token 全部失效需重新登录。建议 32+ 随机字符，如 openssl rand -hex 32）
     */
    private String tokenSecret;

    @PostConstruct
    public void checkRequiredProperties() {
        if (!StringUtils.hasText(tokenSecret) || tokenSecret.trim().length() < 32) {
            throw new IllegalStateException("token.tokenSecret 未配置或长度不足（最少 32 字符）：JWT 密钥必须显式配置，禁止默认值回退");
        }
    }
}
