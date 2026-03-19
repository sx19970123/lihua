package com.lihua.model.validation;

public class AttachmentValidation {
    // url上传
    public interface AttachmentUrlUploadValidation {}
    // 分片上传合并
    public interface AttachmentChunksMergeUploadValidation {}
    // 检查md5
    public interface AttachmentCheckMd5Validation {}
}
