package com.lihua.system.service.impl;

import com.lihua.system.service.SysFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class SysMinioFileServiceImpl implements SysFileService {
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
        return null;
    }
}
