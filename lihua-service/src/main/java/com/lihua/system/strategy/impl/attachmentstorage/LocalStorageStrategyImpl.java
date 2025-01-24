package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.enums.SysBaseEnum;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

@Component("LOCAL")
public class LocalStorageStrategyImpl implements AttachmentStorageStrategy {

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
        // 获取过期时间
        long expirationTime = DateUtils.timeStamp(LocalDateTime.now().plusMinutes(expiryInMinutes));
        // 文件路径和过期时间
        String params = path + "::" + expirationTime;
        // 对链接参数进行加密
        String key = AesUtils.encrypt(params, SysBaseEnum.ATTACHMENT_URL_KEY.getValue());
        // 返回文件url后缀
        return "/system/attachment/download/" + key;
    }

    @Override
    public InputStream download(String path) {
        return null;
    }
}
