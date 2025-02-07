package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysAttachment;
import com.lihua.system.model.dto.SysAttachmentDTO;
import com.lihua.system.service.SysAttachmentService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.util.List;

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
}
