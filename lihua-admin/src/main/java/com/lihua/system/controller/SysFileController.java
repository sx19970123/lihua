package com.lihua.system.controller;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.FileException;
import com.lihua.model.web.BaseController;
import com.lihua.system.service.SysFileService;
import com.lihua.utils.security.TemporaryTokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("system/file")
public class SysFileController extends BaseController {

    @Resource(name = "sysFileServiceImpl")
    private SysFileService sysFileService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private LihuaConfig lihuaConfig;

    /**
     * 通过临时token下载附件
     * 该接口无需springSecurity鉴权即可访问
     * 其他需要鉴权的接口请注意不要设置接口地址为/system/file/download/**
     * @param temporaryToken
     * @return
     */
    @GetMapping("download/{temporaryToken}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable("temporaryToken") String temporaryToken) {
        String fileFullPathByTemporaryToken = TemporaryTokenUtils.getFileFullPathByTemporaryToken(temporaryToken);
        return success(new File(fileFullPathByTemporaryToken));
    }

    @PostMapping("avatar")
    public String uploadAvatar(@RequestParam("avatarFile") MultipartFile file) {
        String fileName = sysFileService.uploadAvatar(file);
        return success(fileName);
    }

    @SneakyThrows
    @GetMapping(value = "imagePreview/{fileName}")
    public ResponseEntity<StreamingResponseBody> imagePreview(@PathVariable String fileName, HttpServletResponse response) {
        File file = sysFileService.imagePreview(fileName);
        return success(file);
    }
}
