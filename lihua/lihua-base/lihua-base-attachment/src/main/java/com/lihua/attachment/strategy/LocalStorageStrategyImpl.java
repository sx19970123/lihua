package com.lihua.attachment.strategy;

import com.lihua.attachment.config.AttachmentProperties;
import com.lihua.attachment.enums.AttachmentEnum;
import com.lihua.attachment.exception.AttachmentException;
import com.lihua.attachment.utils.FileUtils;
import com.lihua.common.utils.crypt.AesUtils;
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

            // 4. 合并文件（零拷贝 + 循环保证完整）
            try (FileChannel outChannel = FileChannel.open(
                    targetPath,
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

                    // 删除分片文件
                    Files.deleteIfExists(partPath);
                }
            }

            // 5. MD5 校验（强一致性）
            if (md5 != null && !md5.isEmpty()) {
                String fileMd5;
                try (InputStream is = Files.newInputStream(targetPath)) {
                    fileMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
                }

                if (!md5.equalsIgnoreCase(fileMd5)) {
                    Files.deleteIfExists(targetPath);
                    throw new AttachmentException("文件校验失败，MD5不一致");
                }
            }

            // 6. 删除临时目录（递归）
            deleteDirectory(tempDir);

        } catch (Exception e) {
            // 7. 异常回滚（删除目标文件）
            try {
                Files.deleteIfExists(targetPath);
            } catch (IOException ex) {
                log.warn("删除失败文件异常: {}", targetPath, ex);
            }

            log.error("附件合并失败", e);
            throw new AttachmentException("附件合并失败");
        }
    }

    @Override
    public void delete(String path) {
        FileUtils.delete(path);
    }

    @Override
    public String getDownloadURL(String fullFilePath, String originName, int expiryInMinutes) {
        // 获取过期时间
        long expirationTime = DateUtils.timeStamp(DateUtils.now().plusMinutes(expiryInMinutes));
        // 附件路径和过期时间
        String params = fullFilePath + "::" + expirationTime;
        // 对链接参数进行加密
        String key = AesUtils.encrypt(params, AttachmentEnum.ATTACHMENT_URL_KEY.getValue());
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
