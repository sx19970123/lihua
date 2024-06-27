package com.lihua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局通用配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "lihua")
public class LihuaConfig {

    /**
     * redis 中 token 过期时间
     */
    private Long tokenExpireTime;

    /**
     * token 刷新阈值
     */
    private Integer refreshThreshold;

    /**
     * 下载附件临时 token 过期时间
     */
    private Integer temporaryTokenExpireTime;

    /**
     * 允许跨域最长时间
     */
    private Integer corsMaxAge;

    /**
     * 文件上传路径
     */
    private String uploadFilePath;

    /**
     * 文件导出路径
     */
    private String exportFilePath;

    /**
     * 启用验证码
     */
    private Boolean enableVerificationCode;


    /**
     * 判断 exportFilePath 结尾是否为 /
     */
    public String getExportFilePath() {
        return exportFilePath.lastIndexOf("/") != -1 ? exportFilePath : exportFilePath + "/";
    }

    /**
     * 判断 uploadFilePath 结尾是否为 /
     */
    public String getUploadFilePath() {
        return uploadFilePath.lastIndexOf("/") != -1 ? uploadFilePath : uploadFilePath + "/";
    }
}
