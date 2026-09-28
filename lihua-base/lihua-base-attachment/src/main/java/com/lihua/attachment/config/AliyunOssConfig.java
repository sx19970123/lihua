package com.lihua.attachment.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class AliyunOssConfig {

    /**
     * 地址（默认空：非 OSS 模式的服务不强制配置 aliyun.oss 键）
     */
    @Value("${aliyun.oss.endpoint:}")
    private String endpoint;

    /**
     * id
     */
    @Value("${aliyun.oss.access-key-id:}")
    private String accessKeyId;

    /**
     * 密钥
     */
    @Value("${aliyun.oss.access-key-secret:}")
    private String accessKeySecret;

    /**
     * 附件配置
     */
    @Resource
    private AttachmentProperties attachmentProperties;

    /**
     * 向 bean 中加入 oss 客户端
     */
    @Bean
    public OSS ossClient() {
        // 仅启用ALIYUN-OSS下加载OSS客户端
        if (!"ALIYUN-OSS".equals(attachmentProperties.getUploadFileModel())) {
            return null;
        }
        // OSS 模式下三要素必配，缺失启动失败（fail-fast 从 @Value 解析移到 OSS 分支，非 OSS 服务不受牵连）
        if (!StringUtils.hasText(endpoint) || !StringUtils.hasText(accessKeyId) || !StringUtils.hasText(accessKeySecret)) {
            throw new IllegalStateException("attachment.upload-file-model 为 ALIYUN-OSS 时，aliyun.oss 的 endpoint/access-key-id/access-key-secret 必须配置");
        }
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
