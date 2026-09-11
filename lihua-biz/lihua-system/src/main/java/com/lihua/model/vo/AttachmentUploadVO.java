package com.lihua.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 附件上传统一响应（单管线上传 / 秒传命中 / 分片合并）
 */
@Data
@Accessors(chain = true)
public class AttachmentUploadVO {

    /**
     * 附件id：业务附件引用句柄
     */
    private String id;

    /**
     * 对象键（存储坐标，不含 host/存储模式/时效）：头像引用句柄
     */
    private String path;

    /**
     * 行级公开标记：写入即物化，不可变
     */
    private Boolean isPublic;

    /**
     * 首次访问链接，拿来即用（公开=永久链，私密=时效签名链）；链接可能过期，过期需重新获取
     */
    private String url;

    /**
     * 原文件名
     */
    private String originalName;

    /**
     * MIME 类型
     */
    private String type;
}
