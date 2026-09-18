package com.lihua.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 登录失败锁定配置（关闭后既不检查锁定也不计失败，验证码作为唯一防线）
 */
@Data
@Component
@ConfigurationProperties(prefix = "login.lock")
public class LoginLockProperties {

    /**
     * 是否启用登录失败锁定
     */
    private boolean enabled = true;

    /**
     * 窗口内失败达该次数即锁定（账号与 ip 双维度各自计数）
     */
    private int failThreshold = 5;

    /**
     * 失败计数窗口（自首次失败起算的固定窗；Duration 带单位写法如 15m，裸数字按分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration failWindow = Duration.ofMinutes(15);

    /**
     * 锁定时长（Duration 带单位写法如 10m，裸数字按分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration lockDuration = Duration.ofMinutes(10);

}
