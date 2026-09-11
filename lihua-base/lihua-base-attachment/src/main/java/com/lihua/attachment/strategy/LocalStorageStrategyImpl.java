package com.lihua.attachment.strategy;

import com.lihua.attachment.config.AttachmentProperties;
import com.lihua.attachment.exception.AttachmentException;
import com.lihua.attachment.utils.FileUtils;
import com.lihua.attachment.utils.SignedUrlUtils;
import com.lihua.common.utils.date.DateUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component("LOCAL")
public class LocalStorageStrategyImpl implements AttachmentStorageStrategy {

    @Resource
    private AttachmentProperties attachmentProperties;

    private String TEMPORARY_PATH;

    @Override
    public void uploadFile(MultipartFile file, String fullFilePath) {
        FileUtils.upload(file, fullFilePath);
    }

    @Override
    public boolean isExists(String fullFilePath) {
        return FileUtils.isExists(fullFilePath);
    }

    @Override
    public String getUploadId(String tempUploadFilePath) {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<Integer> getUploadedChunksIndex(String tempUploadFilePath, String uploadId) {
        try(Stream<Path> paths = Files.walk(Paths.get(TEMPORARY_PATH , uploadId))) {
            return paths.filter(Files::isRegularFile)
                    // 确保保存的附件名为纯索引
                    .map(file -> Integer.valueOf(file.getFileName().toString()))
                    .toList();
        } catch (NoSuchFileException e) {
            // 没有找到对应附件
            return new ArrayList<>();
        } catch (IOException e) {
            throw new AttachmentException("获取分片临时附件出错");
        }
    }

    @Override
    public void chunksUploadFile(MultipartFile file, String fullFilePath, Integer index, String uploadId) {
        FileUtils.upload(file, Paths.get(TEMPORARY_PATH, uploadId, String.valueOf(index)).toString());
    }

    @Override
    public void chunksMerge(String fullFilePath, String md5, String uploadId, Integer total) {

        Path tempDir = Paths.get(TEMPORARY_PATH, uploadId);
        Path targetPath = Paths.get(fullFilePath);
        // 先合并到临时名再移动到目标，目标文件只会以完整形态出现；
        // 临时名带随机串避免并发合并互踩，放 temporary 根下而非 tempDir 内，以免混入分片索引枚举
        Path mergedTemp = Paths.get(TEMPORARY_PATH, uploadId + "." + UUID.randomUUID() + ".merging");

        try {
            // 1. 校验临时目录是否存在
            if (!Files.exists(tempDir)) {
                throw new AttachmentException("临时目录不存在");
            }

            // 2. 校验分片完整性（强校验）
            Set<String> fileNames;
            try (Stream<Path> stream = Files.list(tempDir)) {
                fileNames = stream
                        .map(p -> p.getFileName().toString())
                        .collect(Collectors.toSet());
            }

            if (fileNames.size() != total) {
                throw new AttachmentException("附件合并失败，分片数量不一致");
            }

            for (int i = 1; i <= total; i++) {
                if (!fileNames.contains(String.valueOf(i))) {
                    throw new AttachmentException("附件合并失败，缺少分片：" + i);
                }
            }

            // 3. 创建目标目录
            Path parent = targetPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            // 4. 合并到临时文件（零拷贝 + 循环保证完整；分片保留至成功，失败可直接重试）
            try (FileChannel outChannel = FileChannel.open(
                    mergedTemp,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )) {

                for (int i = 1; i <= total; i++) {
                    Path partPath = tempDir.resolve(String.valueOf(i));

                    try (FileChannel inChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {

                        long size = inChannel.size();
                        long position = 0;

                        while (position < size) {
                            long transferred = inChannel.transferTo(position, size - position, outChannel);
                            if (transferred <= 0) {
                                break; // 防止死循环
                            }
                            position += transferred;
                        }
                    }
                }
            }

            // 5. MD5 校验（强一致性，通过前不触碰目标）
            if (md5 != null && !md5.isEmpty()) {
                String fileMd5;
                try (InputStream is = Files.newInputStream(mergedTemp)) {
                    fileMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
                }

                if (!md5.equalsIgnoreCase(fileMd5)) {
                    throw new AttachmentException("文件校验失败，MD5不一致");
                }
            }

            // 6. 原子移动到目标（并发另一路已落同内容目标时为等价覆盖）
            try {
                Files.move(mergedTemp, targetPath, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(mergedTemp, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 7. 删除临时分片目录（递归）
            deleteDirectory(tempDir);

        } catch (Exception e) {
            // 异常只清理本次合并的临时文件，不删目标——目标若已存在必为并发另一路的成功产物
            try {
                Files.deleteIfExists(mergedTemp);
            } catch (IOException ex) {
                log.warn("删除合并临时文件异常: {}", mergedTemp, ex);
            }

            log.error("附件合并失败", e);
            throw new AttachmentException("附件合并失败");
        }
    }

    @Override
    public void cleanChunks(String fullFilePath, String uploadId) {
        // 本地分片即临时目录内容，递归删除（deleteDirectory 幂等，目录不存在直接返回）
        deleteDirectory(Paths.get(TEMPORARY_PATH, uploadId));
    }

    @Override
    public void delete(String path) {
        FileUtils.delete(path);
    }

    @Override
    public String getDownloadURL(String fullFilePath, String originName, int expiryInMinutes) {
        // 获取过期时间
        long expirationTime = DateUtils.timeStamp(DateUtils.now().plusMinutes(expiryInMinutes));
        // 路径与时效明文携带 + HMAC-SHA256 签名防伪造防篡改（密钥外置 attachment.download-sign-key）
        String key = SignedUrlUtils.sign(fullFilePath, expirationTime, attachmentProperties.getDownloadSignKey());
        // 返回附件url后缀
        return "/system/attachment/storage/download?key=" + URLEncoder.encode(key, StandardCharsets.UTF_8) + "&originName=" + URLEncoder.encode(originName, StandardCharsets.UTF_8);
    }

    @Override
    public InputStream download(String fullFilePath) {
        try {
            // 路径检查
            if (FileUtils.checkPath(fullFilePath, attachmentProperties.getUploadFilePath())) {
                return Files.newInputStream(Path.of(fullFilePath));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        throw new AttachmentException("获取附件失败");
    }

    // 项目启动时将TEMPORARY_PATH初始化
    @PostConstruct
    void initTemporaryPath() {
        TEMPORARY_PATH = Paths.get(attachmentProperties.getUploadFilePath(),"temporary").toString();
    }

    // 删除目录
    private void deleteDirectory(Path path) {
        if (!Files.exists(path)) {
            return;
        }

        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException e) {
                            log.warn("删除失败: {}", p, e);
                        }
                    });
        } catch (IOException e) {
            log.warn("删除目录失败: {}", path, e);
        }
    }
}
