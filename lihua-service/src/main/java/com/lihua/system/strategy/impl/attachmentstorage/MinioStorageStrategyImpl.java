package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.system.strategy.AttachmentStorageStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component("MINIO")
public class MinioStorageStrategyImpl implements AttachmentStorageStrategy {


    @Override
    public String uploadFile(MultipartFile file, String businessCode) {
        return "";
    }

    @Override
    public String uploadFile(String url, String businessCode) {
        return "";
    }

    @Override
    public void delete(String path) {}

    @Override
    public String getDownloadURL(String path, long expiryInMinutes) {
        return "";
    }

    @Override
    public File download(String path, String originalName) {
        return null;
    }
}
