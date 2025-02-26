package com.lihua.strategy.system.attachment.impl;

import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.strategy.system.attachment.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.file.FileUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component("LOCAL")
public class LocalStorageStrategyImpl implements AttachmentStorageStrategy {

    @Resource
    private LihuaConfig lihuaConfig;

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
            throw new FileException("获取分片临时附件出错");
        }
    }

    @Override
    public void chunksUploadFile(MultipartFile file, String fullFilePath, Integer index, String uploadId) {
        FileUtils.upload(file, TEMPORARY_PATH + uploadId + "/" + index);
    }

    @SneakyThrows
    @Override
    public void chunksMerge(String fullFilePath, String md5, String uploadId, Integer total) {
        Path temporaryPath = Paths.get(TEMPORARY_PATH, uploadId);

        // 校验临时文件数量
        try (Stream<Path> temporaryListStream = Files.list(temporaryPath)) {
            if (temporaryListStream.count() != total) {
                throw new FileException("附件合并失败，缺少数据项");
            }
        }

        // 数据合并
        try (FileChannel outPutChannel = new FileOutputStream(fullFilePath).getChannel()) {
            for (int i = 1; i <= total ; i++) {
                String temporaryFilePath = Paths.get(TEMPORARY_PATH, uploadId, String.valueOf(i)).toString();
                File temporaryFile = new File(temporaryFilePath);
                try(FileChannel inputChannel = new FileInputStream(temporaryFile).getChannel()) {
                    // 通过transferFrom实现零拷贝合并数据
                    inputChannel.transferTo(0, temporaryFile.length(), outPutChannel);
                }
                // 删除临时文件
                temporaryFile.delete();
            }
            // 删除文件夹
            Files.delete(temporaryPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("附件合并失败");
        }
    }

    @Override
    public void delete(String path) {
        FileUtils.delete(path);
    }

    @Override
    public String getDownloadURL(String fullFilePath, String originName, int expiryInMinutes) {
        // 获取过期时间
        long expirationTime = DateUtils.timeStamp(LocalDateTime.now().plusMinutes(expiryInMinutes));
        // 附件路径和过期时间
        String params = fullFilePath + "::" + expirationTime;
        // 对链接参数进行加密
        String key = AesUtils.encrypt(params, SysBaseEnum.ATTACHMENT_URL_KEY.getValue());
        // 返回附件url后缀
        return "/system/attachment/storage/download?key=" + URLEncoder.encode(key, StandardCharsets.UTF_8) + "&originName=" + URLEncoder.encode(originName, StandardCharsets.UTF_8);
    }

    @Override
    public InputStream download(String fullFilePath) {
        try {
            return Files.newInputStream(Path.of(fullFilePath));
        } catch (IOException e) {
            throw new FileException("获取附件失败");
        }
    }

    // 项目启动时将TEMPORARY_PATH初始化
    @PostConstruct
    void initTemporaryPath() {
        TEMPORARY_PATH = lihuaConfig.getUploadFilePath() + "temporary/";
    }
}
