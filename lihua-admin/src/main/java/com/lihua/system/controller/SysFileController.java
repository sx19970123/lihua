package com.lihua.system.controller;

import com.lihua.model.web.ControllerResult;
import com.lihua.system.service.SysFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/file")
public class SysFileController extends ControllerResult {

    @Resource(name = "sysFileServiceImpl")
    private SysFileService sysFileService;

    @GetMapping("imagePreview/{fileName}")
    public void imagePreview(@PathVariable String fileName, HttpServletResponse response) {
        fileSuccess(response, sysFileService.imagePreview(fileName));
    }
}
