package com.lihua.system.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysAttachment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 文件MD5值
     */
    private String md5;

    /**
     * 文件存储名
     */
    private String storedName;

    /**
     * 文件原名称
     */
    private String originalName;

    /**
     * 文件扩展名
     */
    private String extensionName;

    /**
     * 文件保存路径
     */
    private String path;

    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 业务名称，关联文件所属的业务模块
     */
    private String businessName;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 上传状态 初始化、上传中、上传成功、上传失败
     */
    private char uploadStatus;

    /**
     * 上传模式 单文件、分片
     */
    private String uploadMode;

    /**
     * 分片详情
     */
    private String chunkDetails;

    /**
     * 文件存储位置 如：本地、云存储等
     */
    private String storageLocation;

    /**
     * 上传人id
     */
    private String createId;

    /**
     * 上传时间
     */
    private LocalDateTime createTime;

    /**
     * 删除标识
     */
    private char delFlag;
}
