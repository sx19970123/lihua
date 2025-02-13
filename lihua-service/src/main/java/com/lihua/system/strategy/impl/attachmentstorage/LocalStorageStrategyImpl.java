package com.lihua.system.strategy.impl.attachmentstorage;

import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.file.FileUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component("LOCAL")
public class LocalStorageStrategyImpl implements AttachmentStorageStrategy {

    @Resource
    private LihuaConfig lihuaConfig;

    @Override
    public String uploadFile(MultipartFile file) {
        return FileUtils.upload(file);
    }

    @Override
    public String uploadFile(String url) {
        return FileUtils.upload(url);
    }

    @Override
    public boolean isExists(String path) {
        return FileUtils.isExists(path);
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
    public List<Integer> getTempChunksIndex(String uploadId) {
        try(Stream<Path> paths = Files.walk(Paths.get(lihuaConfig.getChunkTempUploadFilePath(), uploadId))) {
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

    @SneakyThrows
    @Override
    public String chunksMerge(String fileName, String md5, String uploadId, Integer total) {
        // 创建最后输出的文件
        File file = new File(FileUtils.generateFullFilePath(fileName));
        // 初始化md5计算器
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        try (FileOutputStream fos = new FileOutputStream(file, true);
             FileChannel outputChannel = fos.getChannel()) {
            for (int i = 0; i < total; i++) {
                // 循环读取临时文件
                String temporaryFilePath = Paths.get(lihuaConfig.getChunkTempUploadFilePath(), uploadId, String.valueOf(i)).toString();
                File chunkFile = new File(temporaryFilePath);
                try (FileInputStream fis = new FileInputStream(chunkFile);
                     FileChannel inputChannel = fis.getChannel()) {
                    ByteBuffer buffer = ByteBuffer.allocate(64 * 1024);
                    while (inputChannel.read(buffer) > 0) {
                        buffer.flip();
                        buffer.mark();
                        // 更新MD5
                        messageDigest.update(buffer);
                        buffer.reset();
                        // 写入文件
                        outputChannel.write(buffer);
                        buffer.clear();
                    }
                }
                // 删除分片
                chunkFile.delete();
            }
            // 删除对应临时文件夹
            Files.delete(Paths.get(lihuaConfig.getChunkTempUploadFilePath(), uploadId));

            if (!bytesToHex(messageDigest.digest()).equals(md5)) {
                throw new FileException("合并文件损坏");
            }

            return file.getAbsolutePath();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException("文件合并失败");
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
    public InputStream download(String path) {
        try {
            return Files.newInputStream(Path.of(path));
        } catch (IOException e) {
            throw new FileException("获取文件失败");
        }
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
