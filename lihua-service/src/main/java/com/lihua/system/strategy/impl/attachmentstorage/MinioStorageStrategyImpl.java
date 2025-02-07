package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.system.strategy.AttachmentStorageStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

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
    public boolean isExists(String path) {
        return false;
    }

    @Override
    public void uploadTempFile(MultipartFile file, String fullFilePath) {
    }

    @Override
    public List<Integer> getTempChunksIndex(String uploadId) {
        return List.of();
    }

    @Override
    public String chunksMerge(String fileName, String md5, String uploadId, Integer total) {
        return null;
    }

    @Override
    public void delete(String path) {}

    @Override
    public String getDownloadURL(String path, long expiryInMinutes) {
        return "";
    }

    @Override
    public File download(String path) {
        return null;
    }
}
