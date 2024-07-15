package com.lihua.utils.file;

import com.lihua.exception.FileException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtils {

    /**
     * 通过网络url 地址获取文件名称
     * @param fileUrl 网络url地址
     * @param useRandomUUID 是否生成随机字符串
     * @return
     */
    public static String getFileNameByURL(String fileUrl, boolean useRandomUUID) {
        URL url = null;
        try {
            url = new URL(fileUrl);
        } catch (MalformedURLException e) {
            throw new FileException("获取文件名称失败，请检查URL地址是否包含文件名称");
        }

        String fileName = Paths.get(url.getPath()).getFileName().toString();

        if (useRandomUUID) {
            String uuid = UUID.randomUUID().toString().replace("-","");
            return uuid + "_" + fileName;
        }

        return fileName;
    }
}
