package com.lihua.attachment.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
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
     * 桶名称
     */
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    /**
     * 向 bean 中加入 oss 客户端
     */
    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
