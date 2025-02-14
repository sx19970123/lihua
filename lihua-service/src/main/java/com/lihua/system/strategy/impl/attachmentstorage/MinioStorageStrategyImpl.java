package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.config.LihuaConfig;
import com.lihua.exception.FileException;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import com.lihua.utils.file.FileUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Component("MINIO")
@Slf4j
public class MinioStorageStrategyImpl implements AttachmentStorageStrategy {

    @Resource
    private MinioClient minioClient;

    @Resource
    private LihuaConfig lihuaConfig;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String fullPath = lihuaConfig.getUploadFilePath() + FileUtils.generateUUIDFileName(file.getOriginalFilename());
        try {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(fullPath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return fullPath;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("附件上传失败");
        }
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
    public InputStream download(String path) {
        return null;
    }


    @PostConstruct
    // 项目启动时判断目标桶是否创建
    private void initBucket() {
        if (!"MINIO".equals(lihuaConfig.getUploadFileModel())) {
            return;
        }

        if (!bucketExists()) {
            createBucket();
        }
    }

    // 判断桶是否存在
    private boolean bucketExists() {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("获取桶是否存在失败");
        }
    }

    // 创建桶
    private void createBucket() {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("获取桶数据失败");
        }
    }


}
