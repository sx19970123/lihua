package com.lihua.system.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SysAttachment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 文件存储名
     */
    private String storageName;

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
     * 业务编码（默认文件上传时所在的路由名称）
     */
    private String businessCode;

    /**
     * 业务名称（默认文件上传时所在的菜单名称）
     */
    private String businessName;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 上传状态 上传成功、上传失败
     */
    private String uploadStatus;

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
    private String delFlag;

    /**
     * 上传失败原因
     */
    private String errorMsg;
}
