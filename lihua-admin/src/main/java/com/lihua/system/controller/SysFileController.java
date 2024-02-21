package com.lihua.system.controller;

import com.lihua.model.web.ControllerResult;
import com.lihua.system.service.SysFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("system/file")
public class SysFileController extends ControllerResult {

    @Resource(name = "sysFileServiceImpl")
    private SysFileService sysFileService;

    @PostMapping("avatar")
    public String uploadAvatar(@RequestParam("avatarFile") MultipartFile file) {
        String fileName = sysFileService.uploadAvatar(file);
        return success(fileName);
    }

    @GetMapping("imagePreview/{fileName}")
    public void imagePreview(@PathVariable String fileName, HttpServletResponse response) {
        fileSuccess(response, sysFileService.imagePreview(fileName));
    }
}
