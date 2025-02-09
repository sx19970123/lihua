package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysAttachment;
import com.lihua.system.model.dto.SysAttachmentDTO;
import com.lihua.system.service.SysAttachmentService;
import com.lihua.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("system/attachment")
public class SysAttachmentController extends BaseController {

    @Resource
    private SysAttachmentService sysAttachmentService;

    @Resource
    private LihuaConfig lihuaConfig;

    @PostMapping("page")
    public String queryPage(@RequestBody SysAttachmentDTO sysAttachmentDTO) {
        return success(sysAttachmentService.queryPage(sysAttachmentDTO));
    }

    @GetMapping("{id}")
    public String queryById(@PathVariable("id") String id) {
        return success(sysAttachmentService.queryById(id));
    }

    @PostMapping("info")
    public String queryAttachmentInfoByIds(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        return success(sysAttachmentService.queryAttachmentInfoByIds(ids));
    }

    @PostMapping("upload")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public String upload(@RequestParam("file") MultipartFile file,
                         @ModelAttribute SysAttachment sysAttachment) {
        try {
            String path = sysAttachmentService.upload(file);
            sysAttachment.setPath(path).setStatus("0");
            return success(sysAttachmentService.saveAttachment(sysAttachment));
        } catch (Exception e) {
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            sysAttachmentService.saveAttachment(sysAttachment);
            return error(ResultCodeEnum.FILE_ERROR, "附件上传失败");
        }
    }

    @PostMapping("multiple/upload")
    @Log(description = "附件上传（批量）", type = LogTypeEnum.UPLOAD)
    public String upload(@RequestParam("files") MultipartFile[] files,
                         @RequestParam("sysAttachmentJsonList") String sysAttachmentJsonList) {

        List<SysAttachment> attachmentList = JsonUtils.toArrayObject(sysAttachmentJsonList, SysAttachment.class);
        // 校验数量是否匹配
        if (files.length != attachmentList.size()) {
            return error(ResultCodeEnum.FILE_ERROR, "文件数量与附件信息数量不匹配");
        }

        // 遍历上传文件
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            SysAttachment sysAttachment = attachmentList.get(i);

            try {
                // 执行上传，返回路径
                String path = sysAttachmentService.upload(file);
                sysAttachment.setPath(path).setStatus("0"); // 设置成功状态
            } catch (Exception e) {
                sysAttachment.setStatus("1").setErrorMsg(e.getMessage()); // 记录错误状态
            }
        }

        return success(sysAttachmentService.batchSaveAttachment(attachmentList));
    }

    @PostMapping("url/upload/{businessCode}/{businessName}")
    @Log(description = "附件上传（URL）", type = LogTypeEnum.UPLOAD)
    public String urlUpload(@RequestBody SysAttachment sysAttachment,
                            @PathVariable("businessCode") String businessCode,
                            @PathVariable("businessName") String businessName) {
        sysAttachment
                .setUploadMode("3")
                .setBusinessCode(businessCode)
                .setBusinessName(businessName);
        try {
            // 通过url上传并返回服务器存储路径
            String path = sysAttachmentService.urlUpload(sysAttachment.getUrl());
            Path filePath = Path.of(path);
            sysAttachment
                    .setPath(path)
                    .setStatus("0")
                    .setOriginalName(filePath.getFileName().toString())
                    .setSize(String.valueOf(Files.size(filePath)))
                    .setType(Files.probeContentType(filePath));
            // 保存附件返回id
            String id = sysAttachmentService.saveAttachment(sysAttachment);
            // 返回原路径和id
            return success(Map.of("originalURL", sysAttachment.getUrl(), "id", id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            sysAttachmentService.saveAttachment(sysAttachment);
            // 异常情况下返回原url
            return error(ResultCodeEnum.FILE_ERROR, sysAttachment.getUrl());
        }
    }

    @PostMapping("fast/upload")
    @Log(description = "附件上传（秒传）", type = LogTypeEnum.UPLOAD)
    public String fastUpload(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentService.fastUpload(sysAttachment));
    }

    @PostMapping("chunk/upload/{uploadId}")
    @Log(description = "附件上传（分片）", type = LogTypeEnum.UPLOAD)
    public String chunkSave(@RequestBody SysAttachment sysAttachment, @PathVariable("uploadId") String uploadId) {
        sysAttachment
                .setStatus("2")
                .setTemporaryPath(lihuaConfig.getChunkTempUploadFilePath() + uploadId);
        return success(sysAttachmentService.saveAttachment(sysAttachment));
    }

    @PostMapping("chunk/upload/{uploadId}/{index}")
    public String chunksUpload(@RequestParam("file") MultipartFile file,
                               @PathVariable("uploadId") String uploadId,
                               @PathVariable("index") String index) {
        sysAttachmentService.chunksUpload(file, uploadId, index);
        return success();
    }

    @PostMapping("chunk/merge/{uploadId}/{total}")
    public String chunksMerge(@RequestBody SysAttachment sysAttachment,
                              @PathVariable("uploadId") String uploadId,
                              @PathVariable("total") Integer total) {
        try {
            String path = sysAttachmentService.chunksMerge(sysAttachment, uploadId, total);
            sysAttachment.setId(sysAttachment.getId()).setPath(path).setStatus("0");
            return success(sysAttachmentService.saveAttachment(sysAttachment));
        } catch (Exception e) {
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            sysAttachmentService.saveAttachment(sysAttachment);
            return error(ResultCodeEnum.FILE_ERROR, "附件合并失败");
        }
    }

    @GetMapping("chunk/uploadedIndex/{md5}")
    public String chunksUploadedIndex(@PathVariable("md5") String md5) {
        return success(sysAttachmentService.chunksUploadedIndex(md5));
    }

    @GetMapping("exists/{md5}/{originFileName}")
    public String existsAttachmentByMd5(@PathVariable("md5") String md5, @PathVariable("originFileName") String originFileName) {
        return success(sysAttachmentService.existsAttachmentByMd5(md5, originFileName));
    }

    @DeleteMapping
    @Log(description = "附件删除", type = LogTypeEnum.DELETE)
    public String delete(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        sysAttachmentService.deleteByIds(ids);
        return success();
    }

    @DeleteMapping("business/{id}")
    @Log(description = "附件删除（业务）", type = LogTypeEnum.DELETE)
    public String deleteFromBusiness(@PathVariable("id") String id) {
        sysAttachmentService.deleteFromBusiness(id);
        return success();
    }

    @GetMapping("url/{id}")
    public String getDownloadURL(@PathVariable("id") String id) {
        return success(sysAttachmentService.getDownloadURL(id));
    }

    @GetMapping("download")
    @Log(description = "附件下载（临时）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> download(String key) {
        File file = sysAttachmentService.localDownload(key);
        String originFileName = sysAttachmentService.queryOriginFileName(file.getAbsolutePath());
        return success(file, originFileName);
    }

    @GetMapping("download/p/{id}")
    @Log(description = "附件下载（公开）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> publicDownload(@PathVariable("id") String id) {
        return sysAttachmentService.publicDownload(id);
    }

    @GetMapping("download/e")
    public ResponseEntity<StreamingResponseBody> exportDownload(@NotNull(message = "导出文件路径为空") String path, String fileName) {
        if (path.contains(lihuaConfig.getExportFilePath())) {
            return success(new File(path), fileName, true);
        }
        return null;
    }
}
