package com.lihua.utils.file;

import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.FileException;
import com.lihua.model.attachment.AttachmentStreamAndInfoModel;
import com.lihua.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *  附件上传工具类
 */
@Slf4j
public class FileUtils {

    private static final LihuaConfig lihuaConfig;

    static {
        lihuaConfig = SpringUtils.getBean(LihuaConfig.class);
    }

    /**
     * 单附件上传
     */
    public static String upload(MultipartFile file) {
        String fullFilePath = generateFullFilePath(file.getOriginalFilename());
        return upload(file, fullFilePath);
    }

    /**
     * 单附件上传
     * @param file 附件
     * @param fullFilePath 保存地址
     * @return 保存全路径名
     */
    public static String upload(MultipartFile file, String fullFilePath) {
        try {
            return upload(file.getInputStream(), fullFilePath);
        } catch (IOException e) {
            throw new FileException("附件上传异常");
        }
    }

    /**
     * 多附件上传
     */
    public static List<String> upload(MultipartFile[] files) {
        List<String> fileFullPathList = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            fileFullPathList.add(upload(file));
        }
        return fileFullPathList;
    }

    /**
     * 附件上传
     * @param inputStream 输入流
     * @param fullFilePath 完整路径
     * @return 附件路径
     */
    public static String upload(InputStream inputStream, String fullFilePath) {
        try {
            Path targetPath = Path.of(fullFilePath);
            // 获取目标文件的父目录路径
            Path parentDir = targetPath.getParent();
            // 创建目录
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            // 附件保存
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException("保存附件失败");
        }
        return fullFilePath;
    }

    /**
     * 获取UUID附件名
     * @param originFileName 原附件名称
     * @return 以uuid命名的新附件名称
     */
    public static String generateUUIDFileName(String originFileName) {
        // 通过随机uuid重新命名数据库中保存的附件名称
        String fileName = UUID.randomUUID().toString().replace("-", "");

        if (StringUtils.hasText(originFileName)) {
            String extensionNameByFileName = getExtensionNameByFileName(originFileName);
            fileName = fileName + extensionNameByFileName;
        }

        if (!fileName.contains(".")) {
            log.error("附件【{}】名称获取后缀名异常", fileName);
        }

        return fileName;
    }

    /**
     * 获取附件全路径名称
     * @param originFileName 附件名称
     * @return 附件路径+名称
     */
    public static String generateFullFilePath(String originFileName) {
        String fileName = generateUUIDFileName(originFileName);
        return generateFilePath(fileName);
    }


    /**
     * 附件下载
     * @param file 附件
     */
    public static ResponseEntity<StreamingResponseBody> download(File file, String fileName, boolean autoDelete) {
        if (file == null || !file.exists()) {
            throw new FileException(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return download(fileInputStream, StringUtils.hasText(fileName) ? fileName : file.getName(), String.valueOf(file.length()), autoDelete ? file : null);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new FileException(e.getMessage());
        }
    }

    /**
     * 附件打包下载
     */
    public static ResponseEntity<StreamingResponseBody> download(List<AttachmentStreamAndInfoModel> fileAndNameList) {
        if (fileAndNameList == null || fileAndNameList.isEmpty()) {
            throw new FileException("附件集合为空");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        long size = 0;
        try {
            // 将附件循环打包到ZIP输入流
            for (AttachmentStreamAndInfoModel attachmentStreamAndInfoModel : fileAndNameList) {
                size = size + attachmentStreamAndInfoModel.getSize();
                addToZipFile(attachmentStreamAndInfoModel.getInputStream(), attachmentStreamAndInfoModel.getOriginName(), zipOutputStream);
            }

            // 关闭 ZIP 输出流
            zipOutputStream.finish();
            zipOutputStream.close();

            // 创建输入流资源
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

            return download(inputStreamResource.getInputStream(), fileAndNameList.get(0).getOriginName() + "等" + fileAndNameList.size() + "个附件.zip", size != 0 ? String.valueOf(size) : null,null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException(e.getMessage());
        }
    }

    public static ResponseEntity<StreamingResponseBody> download(InputStream inputStream, String fileName, String size){
        return download(inputStream, fileName, size,null);
    }

    public static ResponseEntity<StreamingResponseBody> download(InputStream inputStream, String fileName){
        return download(inputStream, fileName, null,null);
    }

    /**
     * 附件下载
     */
    private static ResponseEntity<StreamingResponseBody> download(InputStream inputStream, String fileName, String size, File deleteFile) {
        if (!StringUtils.hasText(fileName)) {
            throw new FileException("请指定下载附件的名称");
        }
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        StreamingResponseBody stream = out -> {
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new FileException(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                    // 最后传入的file不为null，将在下载完成后删除file
                    if (deleteFile != null) {
                        deleteFile.delete();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        };

        // 构建返回对象
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=UTF-8''" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM);
        // 附件大小
        if (StringUtils.hasText(size)) {
            bodyBuilder.header("Content-Length", size);
        }

        return bodyBuilder.body(stream);
    }


    /**
     * 获取附件名称
     * @param fullPath 附件全路径
     * @return 附件名称
     */
    public static String getFileNameByPath(String fullPath) {
        if (!StringUtils.hasText(fullPath)) {
            return null;
        }

        String[] pathSplit = fullPath.split("\\\\");
        if (pathSplit.length == 0) {
            return null;
        }

        return pathSplit[pathSplit.length - 1];
    }

    /**
     * 获取附件后缀名
     * @param fileName 附件名称
     * @return 附件后缀名
     */
    public static String getExtensionNameByFileName(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return null;
        }

        String[] pathSplit = fileName.split("\\.");
        if (pathSplit.length == 0) {
            return null;
        }

        return "." + pathSplit[pathSplit.length - 1];
    }


    /**
     * 删除附件
     * @param fileFullPath 附件全路径
     */
    public static void delete(String fileFullPath) {
        Path path = Paths.get(fileFullPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException("附件 " + path + " 删除失败");
        }
    }

    /**
     * 判断附件是否存在
     * @param path 附件全路径
     * @return 附件是否存在
     */
    public static boolean isExists(String path) {
        return Files.exists(Path.of(path));
    }

    /**
     * 附件打包到zip
     * @param inputStream 附件流
     * @param zipOutputStream zip输出流
     * @throws IOException io异常
     */
    private static void addToZipFile(InputStream inputStream, String fileOriginName,ZipOutputStream zipOutputStream) throws IOException {
        // 创建 ZIP 附件条目
        zipOutputStream.putNextEntry(new ZipEntry(fileOriginName));

        // 将附件内容写入 ZIP 附件条目中
        byte[] buffer = new byte[4096];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length);
        }

        // 关闭当前 ZIP 附件条目
        zipOutputStream.closeEntry();
        inputStream.close();
    }

    // 生成附件路径，与附件名拼接
    private static String generateFilePath(String fileName) {
        // 业务编码为空时直接拼接日期和附件名
        return Paths.get(lihuaConfig.getUploadFilePath(), fileName).toString();
    }
}
