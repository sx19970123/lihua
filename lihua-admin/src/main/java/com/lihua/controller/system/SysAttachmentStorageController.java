package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.entity.system.SysAttachment;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.system.validation.AttachmentValidation;
import com.lihua.model.system.vo.SysAttachmentChunkVO;
import com.lihua.model.system.vo.SysAttachmentUrlVO;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.system.attachment.SysAttachmentStorageService;
import com.lihua.utils.json.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "附件操作")
@Slf4j
@RestController
@RequestMapping("system/attachment/storage")
public class SysAttachmentStorageController extends ApiResponseController {

    @Resource
    private SysAttachmentStorageService sysAttachmentStorageService;

    @Operation(summary = "根据路径查询附件信息，用于附件组件数据回显")
    @PostMapping("info")
    public ApiResponseModel<List<SysAttachment>> queryAttachmentInfoByIds(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        return success(sysAttachmentStorageService.queryAttachmentInfoByIds(ids));
    }

    @Operation(summary = "附件是否存在（md5和原附件名均相同）")
    @GetMapping("exists/{md5}/{originFileName}")
    public ApiResponseModel<Boolean> existsAttachmentByMd5(@PathVariable("md5") String md5, @PathVariable("originFileName") String originFileName) {
        return success(sysAttachmentStorageService.existsAttachmentByMd5(md5, originFileName));
    }

    @Operation(summary = "上传附件")
    @PostMapping("upload")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> upload(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.uploadAttachment(file, sysAttachment));
    }

    @Operation(summary = "附件上传（批量）")
    @PostMapping("multiple/upload")
    @Log(description = "附件上传（批量）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<List<String>> upload(@RequestParam("files") MultipartFile[] files,
                         @RequestParam("sysAttachmentJsonList") String sysAttachmentJsonList) {

        List<SysAttachment> attachmentList = JsonUtils.toArrayObject(sysAttachmentJsonList, SysAttachment.class);
        // 校验数量是否匹配
        if (files.length != attachmentList.size()) {
            return error(ResultCodeEnum.FILE_ERROR, "附件数量与附件信息数量不匹配");
        }

        return success(sysAttachmentStorageService.batchUploadAttachment(files, attachmentList));
    }

    @Operation(summary = "附件上传（URL）")
    @PostMapping("url/upload/{businessCode}/{businessName}")
    @Log(description = "附件上传（URL）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<SysAttachmentUrlVO> urlUpload(@RequestBody @Validated(AttachmentValidation.AttachmentUrlUploadValidation.class) SysAttachment sysAttachment,
                                                          @PathVariable("businessCode") String businessCode,
                                                          @PathVariable("businessName") String businessName) {
        sysAttachment.setUploadMode("3").setBusinessCode(businessCode).setBusinessName(businessName);
        return success(sysAttachmentStorageService.urlUploadAttachment(sysAttachment));
    }

    @Operation(summary = "附件上传（秒传）")
    @PostMapping("fast/upload")
    @Log(description = "附件上传（秒传）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> fastUpload(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.fastUpload(sysAttachment));
    }

    @Operation(summary = "附件上传（分片）")
    @PostMapping("chunk/start")
    @Log(description = "附件上传（分片）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<SysAttachmentChunkVO> chunksUploadStart(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentStorageService.chunksUploadAttachmentStart(sysAttachment));
    }

    @Operation(summary = "通过 uploadId值获取已上传分片附件的索引值")
    @GetMapping("chunk/uploadedIndex/{uploadId}")
    public ApiResponseModel<List<Integer>> chunksUploadedIndex(@PathVariable("uploadId") String uploadId) {
        return success(sysAttachmentStorageService.chunksUploadedIndex(uploadId));
    }

    @Operation(summary = "分片上传")
    @PostMapping("chunk/upload/{uploadId}/{index}")
    public ApiResponseModel<String> chunksUpload(@RequestParam("file") MultipartFile file,
                                                 @PathVariable("uploadId") String uploadId,
                                                 @PathVariable("index") String index) {
        sysAttachmentStorageService.chunksUpload(file, uploadId, index);
        return success();
    }

    @Operation(summary = "分片合并")
    @PostMapping("chunk/merge/{total}")
    public ApiResponseModel<String> chunksMerge(@RequestBody @Validated(AttachmentValidation.AttachmentChunksMergeUploadValidation.class) SysAttachment sysAttachment,
                                                @PathVariable("total") Integer total) {
        return success(sysAttachmentStorageService.chunksMerge(sysAttachment, total));
    }

    @Operation(summary = "附件删除（业务）")
    @DeleteMapping("business/{id}")
    @Log(description = "附件删除（业务）", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteFromBusiness(@PathVariable("id") String id) {
        sysAttachmentStorageService.deleteFromBusiness(id);
        return success();
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

    @Operation(summary = "附件下载（导出）")
    @GetMapping("download/e")
    @Log(description = "附件下载（导出）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> exportDownload(@NotNull(message = "导出附件路径为空") String path, String fileName) {
        return sysAttachmentStorageService.exportDownload(path, fileName);
    }
}
