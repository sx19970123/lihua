package com.lihua.attachment.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "attachment")
public class AttachmentProperties {
    /**
     * 附件上传服务类型
     */
    private String uploadFileModel;

    /**
     * 附件上传路径
     */
    private String uploadFilePath;

    /**
     * 允许上传的附件扩展名（小写、不带点，如 jpg、pdf；为空则不限制，由使用方按部署场景决定）
     */
    private List<String> uploadAllowExtensions;

    /**
     * 下载链接签名密钥（HMAC-SHA256）
     */
    private String downloadSignKey;

    /**
     * 附件入口 URL 反代前缀（部署带反代前缀如 /api 时配置，拼在下载入口前；默认空）
     */
    private String urlBasePath = "";

    /**
     * 下载链接默认时效（缺省 1 小时，调大调小由部署按场景决定；支持带单位写法如 1h/60m，裸数字按分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration downloadExpireTime = Duration.ofHours(1);

    /**
     * 下载链接最大时效（缺省 30 天，调大调小由部署按场景决定；支持带单位写法如 30d/720h，裸数字按分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration downloadMaxExpireTime = Duration.ofDays(30);

    // 签名密钥缺失或过短时启动失败：链接签发/验签不可回退默认值；时效配置非法同样拒绝启动
    @PostConstruct
    void checkRequiredProperties() {
        if (!StringUtils.hasText(downloadSignKey)) {
            throw new IllegalStateException("attachment.download-sign-key 未配置（附件下载链接签名密钥），禁止启动");
        }
        if (downloadSignKey.trim().length() < 16) {
            throw new IllegalStateException("attachment.download-sign-key 长度不足（至少 16 字符，建议 32+ 随机串，如 openssl rand -hex 32）");
        }
        if (downloadExpireTime.isZero() || downloadExpireTime.isNegative()
                || downloadMaxExpireTime.isZero() || downloadMaxExpireTime.isNegative()
                || downloadExpireTime.compareTo(downloadMaxExpireTime) > 0) {
            throw new IllegalStateException("attachment.download-expire-time / download-max-expire-time 配置非法（须为正时长，且默认时效不大于最大时效）");
        }
    }
}
