package com.lihua.attachment.strategy;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.lihua.attachment.exception.AttachmentException;
import com.lihua.common.utils.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
@Component("ALIYUN-OSS")
public class AliyunStorageStrategyImpl implements AttachmentStorageStrategy {

    @Autowired(required = false)
    private OSS ossClient;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    /**
     * 普通文件上传
     */
    @Override
    public void uploadFile(MultipartFile file, String fullFilePath) {
        try (InputStream inputStream = file.getInputStream()) {
            ossClient.putObject(bucketName, fullFilePath, inputStream);
        } catch (Exception e) {
            throw new AttachmentException("OSS 文件上传失败");
        }
    }

    /**
     * 判断文件是否存在
     */
    @Override
    public boolean isExists(String fullFilePath) {
        return ossClient.doesObjectExist(bucketName, fullFilePath);
    }

    /**
     * 获取分片上传 ID
     */
    @Override
    public String getUploadId(String fullFilePath) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fullFilePath);
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
        return result.getUploadId();
    }

    /**
     * 获取已经上传的分片序号列表
     */
    @Override
    public List<Integer> getUploadedChunksIndex(String fullFilePath, String uploadId) {
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, fullFilePath, uploadId);
        return ossClient.listParts(listPartsRequest)
                .getParts()
                .stream()
                .map(PartSummary::getPartNumber)
                .toList();
    }

    /**
     * 上传单个分片
     */
    @Override
    public void chunksUploadFile(MultipartFile file, String fullFilePath, Integer index, String uploadId) {
        try (InputStream inputStream = file.getInputStream()) {
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(fullFilePath);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(inputStream);
            uploadPartRequest.setPartSize(file.getSize());
            uploadPartRequest.setPartNumber(index);
            ossClient.uploadPart(uploadPartRequest);
        } catch (Exception e) {
            throw new AttachmentException("OSS 分片上传失败");
        }
    }

    /**
     * 合并分片
     */
    @Override
    public void chunksMerge(String fullFilePath, String md5, String uploadId, Integer total) {
        try {
            // 获取已经上传的所有分片
            ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, fullFilePath, uploadId);
            List<PartSummary> parts = ossClient.listParts(listPartsRequest).getParts();

            // 按 partNumber 排序
            parts.sort(Comparator.comparingInt(PartSummary::getPartNumber));

            // 生成可变的 PartETag 列表
            List<PartETag> partETags = new ArrayList<>();
            for (PartSummary p : parts) {
                partETags.add(new PartETag(p.getPartNumber(), p.getETag()));
            }

            CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
                    bucketName, fullFilePath, uploadId, partETags
            );

            ossClient.completeMultipartUpload(completeRequest);
        } catch (Exception e) {
            throw new AttachmentException("OSS 分片合并失败");
        }
    }

    /**
     * 删除文件
     */
    @Override
    public void delete(String fullFilePath) {
        try {
            ossClient.deleteObject(bucketName, fullFilePath);
        } catch (Exception e) {
            throw new AttachmentException("OSS 删除文件失败");
        }
    }

    /**
     * 获取带过期时间的下载 URL
     */
    @Override
    public String getDownloadURL(String fullFilePath, String originName, int expiryInMinutes) {
        Date expiration = new Date(DateUtils.nowTimeStamp() + expiryInMinutes * 60L * 1000L);
        URL url = ossClient.generatePresignedUrl(bucketName, fullFilePath, expiration);
        return url.toString();
    }

    /**
     * 下载文件
     */
    @Override
    public InputStream download(String fullFilePath) {
        try {
            return ossClient.getObject(bucketName, fullFilePath).getObjectContent();
        } catch (Exception e) {
            throw new AttachmentException("OSS 文件下载失败");
        }
    }
}