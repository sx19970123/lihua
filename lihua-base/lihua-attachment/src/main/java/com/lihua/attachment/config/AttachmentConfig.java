package com.lihua.attachment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "attachment")
public class AttachmentConfig {
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
}
