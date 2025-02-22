package com.lihua.strategy.system.attachment.impl;

import com.lihua.config.LihuaConfig;
import com.lihua.exception.FileException;
import com.lihua.strategy.system.attachment.AttachmentStorageStrategy;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component("MINIO")
@Slf4j
@ConditionalOnProperty(name = "lihua.uploadFileModel", havingValue = "MINIO")
public class MinioStorageStrategyImpl implements AttachmentStorageStrategy {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioAsyncClient minioAsyncClient;

    @Resource
    private LihuaConfig lihuaConfig;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Override
    public void uploadFile(MultipartFile file, String fullFilePath) {
        try {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(fullFilePath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("附件上传失败" + e.getMessage());
        }
    }

    @Override
    public boolean isExists(String fullFilePath) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fullFilePath).build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUploadId(String tempUploadFilePath) {
        try {
            CompletableFuture<CreateMultipartUploadResponse> multipartUploadAsync = minioAsyncClient.createMultipartUploadAsync(bucketName, null, tempUploadFilePath, null, null);
            return multipartUploadAsync.get().result().uploadId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("获取uploadId异常" + e.getMessage());
        }
    }

    @Override
    public List<Integer> getUploadedChunksIndex(String fullFilePath, String uploadId) {
        List<Part> partList = getPartList(uploadId, fullFilePath);
        return partList.stream().map(Part::partNumber).toList();
    }

    @Override
    public void chunksUploadFile(MultipartFile file, String fullFilePath, Integer index, String uploadId) {
        try {
            minioAsyncClient.uploadPartAsync(bucketName, null, fullFilePath, file.getInputStream(), file.getSize(), uploadId, index, null, null).get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("分片上传失败" + e.getMessage());
        }
    }

    @Override
    public void chunksMerge(String fullFilePath, String md5, String uploadId, Integer total) {
        List<Part> partList = getPartList(uploadId, fullFilePath);
        try {
            minioAsyncClient.completeMultipartUploadAsync(bucketName, null, fullFilePath, uploadId, partList.toArray(new Part[]{}), null, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("附件合并失败" + e.getMessage());
        }
    }

    @Override
    public void delete(String fullFilePath) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fullFilePath).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("附件删除失败" + e.getMessage());
        }
    }

    @Override
    public String getDownloadURL(String fullFilePath, String originName, int expiryInMinutes) {
        try {
            return minioClient
                    .getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(fullFilePath)
                            .method(Method.GET)
                            .expiry(expiryInMinutes * 60)
                            .extraQueryParams(Map.of("response-content-disposition", "attachment; filename=" + originName))
                            .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("获取附件url失败" + e.getMessage());
        }
    }

    @Override
    public InputStream download(String fullFilePath) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fullFilePath).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("附件下载失败" + e.getMessage());
        }
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

    // 获取Part集合
    private List<Part> getPartList(String uploadId, String fullFilePath) {
        try {
            ListPartsResponse listPartsResponse = minioAsyncClient.listPartsAsync(bucketName, null, fullFilePath, null, null, uploadId, null, null).get();
            return listPartsResponse.result().partList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("获取分片临时附件出错");
        }
    }
}
