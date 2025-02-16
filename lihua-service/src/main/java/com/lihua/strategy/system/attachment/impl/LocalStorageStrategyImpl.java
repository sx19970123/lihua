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
        // 创建最后输出的附件
        File file = new File(fullFilePath);
        // 初始化md5计算器
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        try (FileOutputStream fos = new FileOutputStream(file, true);
             FileChannel outputChannel = fos.getChannel()) {
            for (int i = 1; i <= total; i++) {
                // 循环读取临时附件
                String temporaryFilePath = Paths.get(TEMPORARY_PATH, uploadId, String.valueOf(i)).toString();
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
                        // 写入附件
                        outputChannel.write(buffer);
                        buffer.clear();
                    }
                }
                // 删除分片
                chunkFile.delete();
            }
            // 删除对应临时附件夹
            Files.delete(Paths.get(TEMPORARY_PATH, uploadId));

            if (!bytesToHex(messageDigest.digest()).equals(md5)) {
                throw new FileException("合并附件损坏");
            }
        } catch (IOException e) {
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
        return "/system/attachment/download?key=" + URLEncoder.encode(key, StandardCharsets.UTF_8) + "&originName=" + URLEncoder.encode(originName, StandardCharsets.UTF_8);
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
