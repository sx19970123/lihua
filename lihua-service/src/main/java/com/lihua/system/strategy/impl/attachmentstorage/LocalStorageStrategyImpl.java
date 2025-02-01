package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.enums.SysBaseEnum;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component("LOCAL")
public class LocalStorageStrategyImpl implements AttachmentStorageStrategy {

    @Override
    public String uploadFile(MultipartFile file, String businessCode) {
        return FileUtils.upload(file, businessCode);
    }

    @Override
    public String uploadFile(String url, String businessCode) {
        return FileUtils.upload(url, businessCode);
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
