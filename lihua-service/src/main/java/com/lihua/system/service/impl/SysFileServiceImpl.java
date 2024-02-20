package com.lihua.system.service.impl;

import cn.hutool.core.io.FileUtil;
import com.lihua.config.LihuaConfig;
import com.lihua.system.service.SysFileService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SysFileServiceImpl implements SysFileService {
    @Resource
    private LihuaConfig lihuaConfig;

    @Override
    public String upload(MultipartFile file) {
        return null;
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
}
