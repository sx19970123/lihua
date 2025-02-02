package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.file.FileUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component("LOCAL")
public class LocalStorageStrategyImpl implements AttachmentStorageStrategy {

    @Resource
    private LihuaConfig lihuaConfig;

    @Override
    public String uploadFile(MultipartFile file, String businessCode) {
        return FileUtils.upload(file, businessCode);
    }

    @Override
    public String uploadFile(String url, String businessCode) {
        return FileUtils.upload(url, businessCode);
    }

    @Override
    public void uploadTempFile(MultipartFile file, String fullFilePath) {
        try {
            FileUtils.upload(file.getInputStream(), fullFilePath);
        } catch (IOException e) {
            throw new FileException("临时文件上传异常");
        }
    }

    @Override
    public List<Integer> getTempChunksIndex(String md5) {
        String uploadFilePath = lihuaConfig.getUploadFilePath();
        try(Stream<Path> paths = Files.walk(Paths.get(uploadFilePath, "temporary", md5))) {
            return paths.filter(Files::isRegularFile)
                    // 确保保存的文件名为纯索引
                    .map(file -> Integer.valueOf(file.getFileName().toString()))
                    .toList();
        } catch (NoSuchFileException e) {
            // 没有找到对应文件
            return new ArrayList<>();
        } catch (IOException e) {
            throw new FileException("获取分片临时文件出错");
        }
    }

    @Override
    public void delete(String path) {
        FileUtils.delete(path);
    }

    @Override
    public String getDownloadURL(String path, long expiryInMinutes) {
        // 获取过期时间
        long expirationTime = DateUtils.timeStamp(LocalDateTime.now().plusMinutes(expiryInMinutes));
        // 文件路径和过期时间
        String params = path + "::" + expirationTime;
        // 对链接参数进行加密
        String key = AesUtils.encrypt(params, SysBaseEnum.ATTACHMENT_URL_KEY.getValue());
        // 返回文件url后缀
        return "/system/attachment/download?key=" + URLEncoder.encode(key, StandardCharsets.UTF_8);
    }

    @Override
    public File download(String path, String originalName) {
        return new File(path, originalName);
    }
}
