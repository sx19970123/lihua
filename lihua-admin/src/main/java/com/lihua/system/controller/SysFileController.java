package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.service.SysFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("imagePreview/{fileName}")
    public void imagePreview(@PathVariable String fileName, HttpServletResponse response) {
        imageFileSuccess(response, sysFileService.imagePreview(fileName));
    }
}
