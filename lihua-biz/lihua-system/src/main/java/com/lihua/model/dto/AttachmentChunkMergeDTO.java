package com.lihua.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 分片上传合并参数（总分片数 total 维持路径传参）
 */
@Data
public class AttachmentChunkMergeDTO {

    /**
     * 分片上传id
     */
    @NotBlank(message = "uploadId为空")
    private String uploadId;

    /**
     * 文件 md5（合并后完整性核对）
     */
    @NotBlank(message = "md5值不能为空")
    private String md5;

    /**
     * 原文件名
     */
    @NotBlank(message = "文件名称不能为空")
    private String originalName;
}
