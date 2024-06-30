package com.lihua.system.controller;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.model.web.BaseController;
import com.lihua.utils.file.FileDownloadUtils;
import com.lihua.utils.file.FileUploadUtils;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("system/file")
public class SysFileController extends BaseController {

    @Resource
    private RedisCache redisCache;

    @Resource
    private LihuaConfig lihuaConfig;

    /**
     * 附件下载
     * 该接口无需springSecurity鉴权即可访问
     * 其他需要鉴权的接口请注意不要设置接口地址为/system/file/download/**
     * @param filePath 文件路径，可以是被 split 分割的多个文件（多文件将打包下载）
     * @return ResponseEntity 包含 StreamingResponseBody，用于文件下载
     */
    @GetMapping("download")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam(name = "filePath") String filePath, @RequestParam(name = "split", defaultValue = ",") String split) {
        // 验证请求的文件是否允许下载
        FileDownloadUtils.isDownloadable(filePath, split);

        String[] filePathArray = filePath.split(split);

        // 单文件直接调用下载
        if (filePathArray.length == 1) {
            return success(new File(filePathArray[0]));
        }

        // 多文件创建文件集合
        List<File> files = new ArrayList<>();
        for (String path : filePathArray) {
            files.add(new File(path));
        }

        return success(files);
    }

    /**
     * 单文件上传
     * @param file
     * @return
     */
    @PostMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return success(FileUploadUtils.upload(file));
    }

    /**
     * 多文件上传
     * @param files
     * @return
     */
    @PostMapping("uploads")
    public String uploads(@RequestParam("files") MultipartFile[] files) {
        return success(FileUploadUtils.upload(files));
    }
}
