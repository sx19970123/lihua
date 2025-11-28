package com.lihua.controller.app;

import com.lihua.annotation.Log;
import com.lihua.entity.system.SysAttachment;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.system.attachment.SysAttachmentStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "上传附件")
    @PostMapping("upload")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> upload(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.uploadAttachment(file, sysAttachment));
    }

    @Operation(summary = "附件下载（公开）")
    @GetMapping("download/p/{id}")
    @Log(description = "附件下载（公开）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> publicDownload(@PathVariable("id") String id, String fileName) {
        return sysAttachmentStorageService.publicDownload(id, fileName);
    }

}
