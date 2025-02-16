package com.lihua.entity.system;

import com.lihua.model.system.validation.AttachmentValidation;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "uploadId为空", groups = { AttachmentValidation.AttachmentChunksMergeUploadValidation.class })
    private String uploadId;

    /**
     * 业务编码（默认附件上传时所在的路由名称）
     */
    private String businessCode;

    /**
     * 业务名称（默认附件上传时所在的菜单名称）
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
     * 上传方式 一般上传、分片上传、附件秒传
     */
    private String uploadMode;

    /**
     * 状态 上传成功、上传失败、分片上传中、业务删除
     */
    private String status;

    /**
     * 附件存储位置 如：本地、云存储等
     */
    private String storageLocation;

    /**
     * md5值
     */
    private String md5;

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

    /**
     * 原url（通过url上传有该字段）
     */
    @NotNull(message = "原URL为空", groups = { AttachmentValidation.AttachmentUrlUploadValidation.class })
    private String url;
}
