package com.lihua.controller;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.entity.SysAttachment;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.AttachmentChunkMergeDTO;
import com.lihua.model.dto.AttachmentChunkStartDTO;
import com.lihua.model.dto.AttachmentFastUploadDTO;
import com.lihua.model.dto.AttachmentUploadDTO;
import com.lihua.model.vo.AttachmentUploadVO;
import com.lihua.model.vo.FastUploadResultVO;
import com.lihua.model.vo.SysAttachmentChunkVO;
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

@Tag(name = "附件存储")
@Slf4j
@RestController
@RequestMapping("system/attachment/storage")
public class SysAttachmentStorageController extends ApiResponseController {

    @Resource
    private SysAttachmentStorageService sysAttachmentStorageService;

    @Operation(summary = "查询附件信息")
    @PostMapping("info")
    public ApiResponseModel<List<SysAttachment>> queryAttachmentInfoByIds(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        return success(sysAttachmentStorageService.queryAttachmentInfoByIds(ids));
    }

    @Operation(summary = "附件是否存在")
    @GetMapping("exists/{md5}")
    public ApiResponseModel<Boolean> existsAttachmentByMd5(@PathVariable("md5") String md5) {
        return success(sysAttachmentStorageService.existsAttachmentByMd5(md5));
    }

    @Operation(summary = "附件上传")
    @PostMapping("upload")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<AttachmentUploadVO> upload(@ModelAttribute @Validated AttachmentUploadDTO uploadDTO) {
        return success(sysAttachmentStorageService.uploadAttachment(uploadDTO));
    }

    @Operation(summary = "文件秒传")
    @PostMapping("fast/upload")
    @Log(description = "附件上传（秒传）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<FastUploadResultVO> fastUpload(@RequestBody @Validated AttachmentFastUploadDTO fastUploadDTO) {
        return success(sysAttachmentStorageService.fastUpload(fastUploadDTO));
    }

    @Operation(summary = "开始分片上传")
    @PostMapping("chunk/start")
    @Log(description = "附件上传（分片）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<SysAttachmentChunkVO> chunksUploadStart(@RequestBody @Validated AttachmentChunkStartDTO chunkStartDTO) {
        return success(sysAttachmentStorageService.chunksUploadAttachmentStart(chunkStartDTO));
    }

    @Operation(summary = "获取已上传分片的索引值")
    @GetMapping("chunk/uploadedIndex/{uploadId}")
    public ApiResponseModel<List<Integer>> chunksUploadedIndex(@PathVariable("uploadId") String uploadId) {
        return success(sysAttachmentStorageService.chunksUploadedIndex(uploadId));
    }

    @Operation(summary = "上传分片")
    @PostMapping("chunk/upload/{uploadId}/{index}")
    public ApiResponseModel<String> chunksUpload(@RequestParam("file") MultipartFile file,
                                                 @PathVariable("uploadId") String uploadId,
                                                 @PathVariable("index") Integer index) {
        sysAttachmentStorageService.chunksUpload(file, uploadId, index);
        return success();
    }

    @Operation(summary = "合并分片")
    @PostMapping("chunk/merge/{total}")
    public ApiResponseModel<AttachmentUploadVO> chunksMerge(@RequestBody @Validated AttachmentChunkMergeDTO chunkMergeDTO,
                                                @PathVariable("total") Integer total) {
        return success(sysAttachmentStorageService.chunksMerge(chunkMergeDTO, total));
    }

    @Operation(summary = "业务删除")
    @DeleteMapping("business")
    @Log(description = "附件删除（业务）", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteFromBusiness(@RequestBody @NotEmpty(message = "附件id不存在") List<String> ids) {
        sysAttachmentStorageService.deleteFromBusiness(ids);
        return success();
    }

    @Operation(summary = "本地附件下载")
    @GetMapping("download")
    public ResponseEntity<StreamingResponseBody> download(String key, String originName) {
        return sysAttachmentStorageService.localDownload(key, originName);
    }

    @Operation(summary = "根据路径下载附件")
    @GetMapping("download/p")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam("fullPath") String fullPath) {
        return sysAttachmentStorageService.download(fullPath);
    }
}
