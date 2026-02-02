package com.lihua.controller.app;

import com.lihua.annotation.Log;
import com.lihua.entity.SysAttachment;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.validation.AttachmentValidation;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.SysAttachmentStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@Tag(name = "APP附件操作")
@Slf4j
@RestController
@RequestMapping("app/system/attachment/storage")
public class AppSysAttachmentStorageController extends ApiResponseController {

    @Resource
    private SysAttachmentStorageService sysAttachmentStorageService;

    @Operation(summary = "根据路径查询附件信息，用于附件组件数据回显")
    @PostMapping("info")
    public ApiResponseModel<List<SysAttachment>> queryAttachmentInfoByIds(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        return success(sysAttachmentStorageService.queryAttachmentInfoByIds(ids));
    }

    @Operation(summary = "附件删除（业务）")
    @DeleteMapping("business")
    @Log(description = "附件删除（业务）", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteFromBusiness(@RequestBody @NotEmpty(message = "附件id不存在") List<String> ids) {
        sysAttachmentStorageService.deleteFromBusiness(ids);
        return success();
    }

    @Operation(summary = "附件是否存在（md5值，原文件名可选）")
    @PostMapping("exists")
    public ApiResponseModel<Boolean> existsAttachmentByMd5(@RequestBody @Validated(AttachmentValidation.AttachmentCheckMd5Validation.class) SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.existsAttachmentByMd5(sysAttachment.getMd5(), sysAttachment.getOriginalName()));
    }

    @Operation(summary = "上传附件")
    @PostMapping("upload")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> upload(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.uploadAttachment(file, sysAttachment));
    }

    @Operation(summary = "附件上传（秒传）")
    @PostMapping("fast/upload")
    @Log(description = "附件上传（秒传）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> fastUpload(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.fastUpload(sysAttachment));
    }

    @Operation(summary = "附件下载（临时）")
    @GetMapping("download")
    @Log(description = "附件下载（临时）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> download(String key, String originName) {
        return sysAttachmentStorageService.localDownload(key, originName);
    }

    @Operation(summary = "附件下载（公开）")
    @GetMapping("download/p/{id}")
    @Log(description = "附件下载（公开）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> publicDownload(@PathVariable("id") String id, String fileName) {
        return sysAttachmentStorageService.publicDownload(id, fileName);
    }

}
