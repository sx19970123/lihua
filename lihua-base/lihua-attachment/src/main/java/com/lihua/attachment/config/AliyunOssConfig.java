package com.lihua.attachment.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOssConfig {

    /**
     * 地址
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * id
     */
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    /**
     * 密钥
     */
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    /**
     * 附件配置
     */
    @Resource
    private AttachmentConfig attachmentConfig;

    /**
     * 向 bean 中加入 oss 客户端
     */
    @Bean
    public OSS ossClient() {
        // 仅启用ALIYUN-OSS下加载OSS客户端
        if (!"ALIYUN-OSS".equals(attachmentConfig.getUploadFileModel())) {
            return null;
        }

        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
