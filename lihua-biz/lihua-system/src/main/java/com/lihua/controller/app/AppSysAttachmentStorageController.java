package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.entity.SysAttachment;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.validation.AttachmentValidation;
import com.lihua.service.SysAttachmentStorageService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("app/system/attachment/storage")
public class AppSysAttachmentStorageController extends ApiResponseController {

    @Resource
    private SysAttachmentStorageService sysAttachmentStorageService;

    @PostMapping("info")
    public ApiResponseModel<List<SysAttachment>> queryAttachmentInfoByIds(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        return success(sysAttachmentStorageService.queryAttachmentInfoByIds(ids));
    }

    @DeleteMapping("business")
    @Log(description = "附件删除（业务）", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteFromBusiness(@RequestBody @NotEmpty(message = "附件id不存在") List<String> ids) {
        sysAttachmentStorageService.deleteFromBusiness(ids);
        return success();
    }

    @PostMapping("exists")
    public ApiResponseModel<Boolean> existsAttachmentByMd5(@RequestBody @Validated(AttachmentValidation.AttachmentCheckMd5Validation.class) SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.existsAttachmentByMd5(sysAttachment.getMd5(), sysAttachment.getOriginalName()));
    }

    @PostMapping("upload")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> upload(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.uploadAttachment(file, sysAttachment));
    }

    @PostMapping("fast/upload")
    @Log(description = "附件上传（秒传）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> fastUpload(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.fastUpload(sysAttachment));
    }

    @GetMapping("download")
    @Log(description = "附件下载（临时）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> download(String key, String originName) {
        return sysAttachmentStorageService.localDownload(key, originName);
    }

}
