package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.service.SysFileService;
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
