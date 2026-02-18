package com.lihua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 全局通用配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "lihua")
public class LihuaConfig {

    /**
     * 系统版本
     */
    private String version;

    /**
     * redis 中 token 过期时间
     */
    private Long tokenExpireTime;

    /**
     * token 刷新阈值
     */
    private Integer refreshThreshold;

    /**
     * 下载附件链接过期时间
     */
    private int fileDownloadExpireTime;

    /**
     * 允许跨域最长时间
     */
    private Integer corsMaxAge;

    /**
     * ServerSentEvents 连接过期时间
     * 过期后客户端在线的情况下会发起自动重连
     */
    private Long sseExpireTime;

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
     * 附件导出路径
     */
    private String exportFilePath;
}
