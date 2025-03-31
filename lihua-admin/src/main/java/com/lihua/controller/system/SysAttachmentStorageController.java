package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.entity.system.SysAttachment;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.system.validation.AttachmentValidation;
import com.lihua.model.web.ResponseController;
import com.lihua.service.system.attachment.SysAttachmentStorageService;
import com.lihua.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("system/attachment/storage")
public class SysAttachmentStorageController extends ResponseController {

    @Resource
    private SysAttachmentStorageService sysAttachmentStorageService;

    @PostMapping("info")
    public String queryAttachmentInfoByIds(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        return success(sysAttachmentStorageService.queryAttachmentInfoByIds(ids));
    }

    @GetMapping("exists/{md5}/{originFileName}")
    public String existsAttachmentByMd5(@PathVariable("md5") String md5, @PathVariable("originFileName") String originFileName) {
        return success(sysAttachmentStorageService.existsAttachmentByMd5(md5, originFileName));
    }

    @PostMapping("upload")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public String upload(@RequestParam("file") MultipartFile file,
                         @ModelAttribute SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.uploadAttachment(file, sysAttachment));
    }

    @PostMapping("multiple/upload")
    @Log(description = "附件上传（批量）", type = LogTypeEnum.UPLOAD)
    public String upload(@RequestParam("files") MultipartFile[] files,
                         @RequestParam("sysAttachmentJsonList") String sysAttachmentJsonList) {

        List<SysAttachment> attachmentList = JsonUtils.toArrayObject(sysAttachmentJsonList, SysAttachment.class);
        // 校验数量是否匹配
        if (files.length != attachmentList.size()) {
            return error(ResultCodeEnum.FILE_ERROR, "附件数量与附件信息数量不匹配");
        }

        return success(sysAttachmentStorageService.batchUploadAttachment(files, attachmentList));
    }

    @PostMapping("url/upload/{businessCode}/{businessName}")
    @Log(description = "附件上传（URL）", type = LogTypeEnum.UPLOAD)
    public String urlUpload(@RequestBody @Validated(AttachmentValidation.AttachmentUrlUploadValidation.class) SysAttachment sysAttachment,
                            @PathVariable("businessCode") String businessCode,
                            @PathVariable("businessName") String businessName) {
        sysAttachment.setUploadMode("3").setBusinessCode(businessCode).setBusinessName(businessName);
        return success(sysAttachmentStorageService.urlUploadAttachment(sysAttachment));
    }

    @PostMapping("fast/upload")
    @Log(description = "附件上传（秒传）", type = LogTypeEnum.UPLOAD)
    public String fastUpload(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.fastUpload(sysAttachment));
    }

    @PostMapping("chunk/start")
    @Log(description = "附件上传（分片）", type = LogTypeEnum.UPLOAD)
    public String chunksUploadStart(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.chunksUploadAttachmentStart(sysAttachment));
    }

    @GetMapping("chunk/uploadedIndex/{uploadId}")
    public String chunksUploadedIndex(@PathVariable("uploadId") String uploadId) {
        return success(sysAttachmentStorageService.chunksUploadedIndex(uploadId));
    }

    @PostMapping("chunk/upload/{uploadId}/{index}")
    public String chunksUpload(@RequestParam("file") MultipartFile file,
                               @PathVariable("uploadId") String uploadId,
                               @PathVariable("index") String index) {
        sysAttachmentStorageService.chunksUpload(file, uploadId, index);
        return success();
    }

    @PostMapping("chunk/merge/{total}")
    public String chunksMerge(@RequestBody @Validated(AttachmentValidation.AttachmentChunksMergeUploadValidation.class) SysAttachment sysAttachment,
                              @PathVariable("total") Integer total) {
        return success(sysAttachmentStorageService.chunksMerge(sysAttachment, total));
    }

    @DeleteMapping("business/{id}")
    @Log(description = "附件删除（业务）", type = LogTypeEnum.DELETE)
    public String deleteFromBusiness(@PathVariable("id") String id) {
        sysAttachmentStorageService.deleteFromBusiness(id);
        return success();
    }

    @GetMapping("download")
    @Log(description = "附件下载（临时）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> download(String key, String originName) {
        return sysAttachmentStorageService.localDownload(key, originName);
    }

    @GetMapping("download/p/{id}")
    @Log(description = "附件下载（公开）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> publicDownload(@PathVariable("id") String id, String fileName) {
        return sysAttachmentStorageService.publicDownload(id, fileName);
    }

    @GetMapping("download/e")
    @Log(description = "附件下载（导出）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> exportDownload(@NotNull(message = "导出附件路径为空") String path, String fileName) {
        return sysAttachmentStorageService.exportDownload(path, fileName);
    }
}
