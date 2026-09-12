package com.lihua.controller;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.AttachmentChunkMergeDTO;
import com.lihua.model.dto.AttachmentChunkStartDTO;
import com.lihua.model.vo.AttachmentUploadVO;
import com.lihua.model.vo.SysAttachmentChunkVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "附件存储")
@RestController
@RequestMapping("system/attachment/storage")
public class SysAttachmentStorageController extends BaseSysAttachmentStorageController {

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
    @Log(description = "附件上传（分片单元）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<String> chunksUpload(@RequestParam("file") MultipartFile file,
                                                 @PathVariable("uploadId") String uploadId,
                                                 @PathVariable("index") Integer index) {
        sysAttachmentStorageService.chunksUpload(file, uploadId, index);
        return success();
    }

    @Operation(summary = "合并分片")
    @PostMapping("chunk/merge/{total}")
    @Log(description = "附件上传（分片合并）", type = LogTypeEnum.UPLOAD)
    public ApiResponseModel<AttachmentUploadVO> chunksMerge(@RequestBody @Validated AttachmentChunkMergeDTO chunkMergeDTO,
                                                @PathVariable("total") Integer total) {
        return success(sysAttachmentStorageService.chunksMerge(chunkMergeDTO, total));
    }
}
