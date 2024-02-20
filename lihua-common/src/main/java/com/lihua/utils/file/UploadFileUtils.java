package com.lihua.utils.file;

import cn.hutool.core.io.FileUtil;
import com.lihua.config.LihuaConfig;
import com.lihua.utils.spring.SpringUtils;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public class UploadFileUtils {
    /**
     * 保存上传的文件，返回文件路径
     * @param file 文件
     * @return 文件路径
     */
    @SneakyThrows
    public static String upload(MultipartFile file) {
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);
        String fullPath = lihuaConfig.getUploadFilePath() + UUID.randomUUID() + "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
        FileUtil.writeBytes(file.getBytes(),fullPath);
        return fullPath;
    }
}
