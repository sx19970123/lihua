package com.lihua.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 附件管理展示模型（对外字段白名单：内部字段 delFlag/createId/updateId/updateTime/url 不下发）
 */
@Data
@Accessors(chain = true)
public class SysAttachmentVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 附件存储名
     */
    private String storageName;

    /**
     * 附件原名称
     */
    private String originalName;

    /**
     * 附件扩展名
     */
    private String extensionName;

    /**
     * 附件保存路径
     */
    private String path;

    /**
     * 分片上传id
     */
    private String uploadId;

    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 附件大小
     */
    private String size;

    /**
     * 附件类型
     */
    private String type;

    /**
     * 上传方式
     */
    private String uploadMode;

    /**
     * 上传状态
     */
    private String status;

    /**
     * 存储位置
     */
    private String storageLocation;

    /**
     * 文件md5值
     */
    private String md5;

    /**
     * 上传失败原因
     */
    private String errorMsg;

    /**
     * 是否公开访问
     */
    private Boolean isPublic;

    /**
     * 上传客户端类型
     */
    private String clientType;

    /**
     * 上传时间
     */
    private LocalDateTime createTime;

    /**
     * 上传用户昵称
     */
    private String uploadName;
}
