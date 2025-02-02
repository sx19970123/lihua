package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
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

    @PostMapping("page")
    public String queryPage(@RequestBody SysAttachmentDTO sysAttachmentDTO) {
        return success(sysAttachmentService.queryPage(sysAttachmentDTO));
    }

    @GetMapping("{id}")
    public String queryById(@PathVariable("id") String id) {
        return success(sysAttachmentService.queryById(id));
    }

    @PostMapping("info")
    public String queryAttachmentInfoByPathList(@RequestBody @NotEmpty(message = "附件路径列表为空") List<String> pathList) {
        return success(sysAttachmentService.queryAttachmentInfoByPathList(pathList));
    }

    @PostMapping("upload/{businessCode}/{businessName}")
    @Log(description = "附件上传", type = LogTypeEnum.UPLOAD)
    public String upload(@RequestParam("file") MultipartFile file,
                         @PathVariable("businessCode") String businessCode,
                         @PathVariable("businessName") String businessName) {
        return success(sysAttachmentService.save(file, businessCode, businessName));
    }

    @GetMapping("chunk/uploadedIndex/{md5}")
    public String chunksUploadedIndex(@PathVariable("md5") String md5) {
        return success(sysAttachmentService.chunksUploadedIndex(md5));
    }

    @PostMapping("chunk/save")
    @Log(description = "附件上传（分片）", type = LogTypeEnum.UPLOAD)
    public String chunksSave(@RequestBody SysAttachment sysAttachment) {
        return success(sysAttachmentService.chunksSave(sysAttachment));
    }

    @PostMapping("chunk/upload/{md5}/{index}")
    public String chunksUpload(@RequestParam("file") MultipartFile file,
                               @PathVariable("md5") String md5,
                               @PathVariable("index") String index) {
        sysAttachmentService.chunksUpload(file, md5, index);
        return success();
    }

    @DeleteMapping("multiple")
    @Log(description = "附件删除", type = LogTypeEnum.DELETE)
    public String delete(@RequestBody List<SysAttachment> sysAttachmentList) {
        sysAttachmentService.deleteByIds(sysAttachmentList);
        return success();
    }

    @DeleteMapping
    @Log(description = "附件删除", type = LogTypeEnum.DELETE)
    public String delete(String path) {
        sysAttachmentService.deleteByPath(path);
        return success();
    }

    @GetMapping("url")
    public String getDownloadURL(String path) {
        return success(sysAttachmentService.getDownloadURL(path));
    }

    @GetMapping("download")
    @Log(description = "附件下载（临时）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> download(String key) {
        File file = sysAttachmentService.localDownload(key);
        String originFileName = sysAttachmentService.queryOriginFileName(file.getAbsolutePath());
        return success(file, originFileName);
    }

    @GetMapping("download/p/{path}")
    @Log(description = "附件下载（公开）", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> publicDownload(@PathVariable("path") String path) {
        return success(sysAttachmentService.publicDownload(path));
    }
}
