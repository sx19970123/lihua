package com.lihua.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

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
}
