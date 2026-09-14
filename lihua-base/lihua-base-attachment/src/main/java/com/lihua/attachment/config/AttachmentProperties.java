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
     * 下载链接默认时效（缺省 1 小时，调大调小由部署按场景决定；支持带单位写法如 1h/60m，裸数字按分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration downloadExpireTime = Duration.ofHours(1);

    // 签名密钥缺失或过短时启动失败：链接签发/验签不可回退默认值；默认时效非法同样拒绝启动。
    // 仅对声明了附件存储角色的服务（upload-file-model 非空）校验；仅消费 URL 组装等纯函数、不承担存储的服务不配置存储参数
    @PostConstruct
    void checkRequiredProperties() {
        if (!StringUtils.hasText(uploadFileModel)) {
            return;
        }
        if (!StringUtils.hasText(downloadSignKey)) {
            throw new IllegalStateException("attachment.download-sign-key 未配置（附件下载链接签名密钥），禁止启动");
        }
        if (downloadSignKey.trim().length() < 16) {
            throw new IllegalStateException("attachment.download-sign-key 长度不足（至少 16 字符，建议 32+ 随机串，如 openssl rand -hex 32）");
        }
        if (downloadExpireTime.isZero() || downloadExpireTime.isNegative()) {
            throw new IllegalStateException("attachment.download-expire-time 配置非法（须为正时长）");
        }
    }
}
