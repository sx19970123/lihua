package com.lihua.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 分片上传启动参数
 */
@Data
public class AttachmentChunkStartDTO {

    /**
     * 断点续传判存键
     */
    @NotBlank(message = "md5值不能为空")
    private String md5;

    /**
     * 客户端声明的原文件名
     */
    @NotBlank(message = "文件名称不能为空")
    private String originalName;

    /**
     * 客户端声明的文件总字节数（分片校验与进度计算）
     */
    @NotNull(message = "文件大小不能为空")
    private Long size;

    /**
     * 附件是否公开访问：true=公开内容（免登录永久链），false=私密附件（时效链）
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Boolean isPublic = false;

    /**
     * 业务编码：纯业务标签（筛选与路径组织），拼入落盘路径，禁止路径穿越形态
     */
    @NotBlank(message = "业务编码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9_-]{1,32}$", message = "业务编码仅支持字母、数字、下划线、中划线，长度1-32")
    private String businessCode;

    /**
     * 业务回显名，缺省回显 businessCode
     */
    @Size(max = 64, message = "业务名称长度不能超过64")
    private String businessName;

    // public 为 Java 关键字，契约参数名无法直接作字段名，以 isPublic 字段承载、以 public 属性名绑定
    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}
