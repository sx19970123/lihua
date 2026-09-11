package com.lihua.attachment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "attachment")
public class AttachmentProperties {
    /**
     * 下载附件链接过期时间
     */
    private int fileDownloadExpireTime;

    /**
     * 附件上传服务类型
     */
    private String uploadFileModel;

    /**
     * 可公开下载附件的BusinessCode集合
     */
    private List<String> uploadPublicBusinessCode;

    /**
     * 附件上传路径
     */
    private String uploadFilePath;

    /**
     * 允许上传的附件扩展名（小写、不带点，如 jpg、pdf；为空则不限制，由使用方按部署场景决定）
     */
    private List<String> uploadAllowExtensions;
}
