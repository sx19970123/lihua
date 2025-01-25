package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.system.strategy.AttachmentStorageStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@Component("MINIO")
public class MinioStorageStrategyImpl implements AttachmentStorageStrategy {


    @Override
    public String uploadFile(MultipartFile file) {
        return "";
    }

    @Override
    public String uploadFile(String url) {
        return "";
    }

    @Override
    public int uploadChunk(MultipartFile file, String MD5, int index, int total) {
        return 0;
    }

    @Override
    public String mergeChunks(String MD5, int total) {
        return "";
    }

    @Override
    public boolean delete(String path) {
        return false;
    }

    @Override
    public String getDownloadURL(String path, long expiryInMinutes) {
        return "";
    }

    @Override
    public File download(String path) {
        return null;
    }
}
