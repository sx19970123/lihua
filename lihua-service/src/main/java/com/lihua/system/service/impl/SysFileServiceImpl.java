package com.lihua.system.service.impl;

import cn.hutool.core.io.FileUtil;
import com.lihua.config.LihuaConfig;
import com.lihua.system.service.SysFileService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SysFileServiceImpl implements SysFileService {
    @Resource
    private LihuaConfig lihuaConfig;

    @SneakyThrows
    @Override
    public String uploadAvatar(MultipartFile file) {
        String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
        FileUtil.writeBytes(file.getBytes(),lihuaConfig.getUploadFilePath() + fileName);
        return fileName;
    }

    @Override
    public String upload(MultipartFile[] files) {
        return null;
    }

    @Override
    public void download(String fileName) {

    }

    @Override
    public void download(List<String> fileNames) {

    }

    @Override
    public byte[] imagePreview(String fileName) {
        return FileUtil.readBytes(lihuaConfig.getUploadFilePath() + fileName);
    }

    @Override
    public String imagePreviewByPath(String fileName) {
        return null;
    }
}
