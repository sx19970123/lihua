package com.lihua.utils.file;

import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.FileException;
import com.lihua.model.attachment.AttachmentFileAndNameModel;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *  文件上传工具类
 */
@Slf4j
public class FileUtils {

    private static final LihuaConfig lihuaConfig;

    private static final List<String> UPLOADED_URL_SUFFIX = List.of("jpg", "jpeg", "png", "gif");

    static {
        lihuaConfig = SpringUtils.getBean(LihuaConfig.class);
    }

    /**
     * 单文件上传
     */
    public static String upload(MultipartFile file) {
        String fullFilePath = generateFullFilePath(file.getOriginalFilename());
        try {
            return upload(file.getInputStream(), fullFilePath);
        } catch (IOException e) {
            throw new FileException("文件上传异常");
        }
    }

    /**
     * 多文件上传
     */
    public static List<String> upload(MultipartFile[] files) {
        List<String> fileFullPathList = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            fileFullPathList.add(upload(file));
        }
        return fileFullPathList;
    }

    /**
     * url文件上传
     * @param url 文件链接
     */
    public static String upload(String url) {
        // 判断url是否合法
        if (!StringUtils.hasText(url)) {
            throw new FileException("文件URL不存在");
        }
        // 获取文件名
        String fullFilePath = generateFullFilePathByURL(url);

        List<String> list = UPLOADED_URL_SUFFIX.stream().filter(fullFilePath::endsWith).toList();

        if (!list.isEmpty()) {
            // 通过url上传
            return upload(url, fullFilePath);
        }
        throw new FileException("文件格式不在" + UPLOADED_URL_SUFFIX + "中，跳过上传");
    }

    /**
     * 文件上传
     * @param inputStream 输入流
     * @param fullFilePath 完整路径
     * @return 文件路径
     */
    public static String upload(InputStream inputStream, String fullFilePath) {
        File file = new File(fullFilePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new FileException("创建文件夹失败");
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new FileException("上传文件失败" + e.getMessage());
        }

        return file.getAbsolutePath();
    }

    /**
     * 通过 URL 获取文件流
     * @param url 文件链接
     */
    public static String upload(String url, String fullFilePath) {
        // 判断url是否合法
        if (!StringUtils.hasText(url)) {
            throw new FileException("文件URL不存在");
        }

        // 获取inputStream调用上传方法
        try(InputStream inputStream = new URL(url).openStream()) {
            return upload(inputStream, fullFilePath);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 获取文件全路径名称
     * @param originFileName 文件名称
     * @return 文件路径+名称
     */
    public static String generateFullFilePath(String originFileName) {
        // 通过随机uuid重新命名数据库中保存的文件名称
        String fileName = UUID.randomUUID().toString().replace("-", "");

        if (StringUtils.hasText(originFileName)) {
            String extensionNameByFileName = getExtensionNameByFileName(originFileName);
            fileName = fileName + extensionNameByFileName;
        }

        if (!fileName.contains(".")) {
            log.error("文件【{}】名称获取后缀名异常", fileName);
        }

        return generateFilePath(fileName);
    }

    /**
     * 获取url文件名称
     * @param fileUrl 文件url
     * @return 文件名称
     */
    public static String getFileNameByURL(String fileUrl) {
        URL url;
        try {
            url = new URL(fileUrl);
        } catch (MalformedURLException e) {
            throw new FileException("获取文件名称失败，请检查URL地址是否包含文件名称");
        }

        return Paths.get(url.getPath()).getFileName().toString();
    }

    /**
     * 文件下载
     * @param file 文件
     */
    public static ResponseEntity<StreamingResponseBody> download(File file, String fileName, boolean autoDelete) {
        if (file == null || !file.exists()) {
            throw new FileException(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return download(fileInputStream, StringUtils.hasText(fileName) ? fileName : file.getName(), autoDelete ? file : null);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new FileException(e.getMessage());
        }
    }

    /**
     * 文件打包下载
     */
    public static ResponseEntity<StreamingResponseBody> download(List<AttachmentFileAndNameModel> fileAndNameList) {
        if (fileAndNameList == null || fileAndNameList.isEmpty()) {
            throw new FileException("文件集合为空");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        try {
            // 将文件循环打包到ZIP输入流
            for (AttachmentFileAndNameModel fileAndName : fileAndNameList) {
                addToZipFile(fileAndName.getInputStream(), fileAndName.getOriginName(), zipOutputStream);
            }

            // 关闭 ZIP 输出流
            zipOutputStream.finish();
            zipOutputStream.close();

            // 创建输入流资源
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

            return download(inputStreamResource.getInputStream(), fileAndNameList.get(0).getOriginName() + "等" + fileAndNameList.size() + "个文件.zip", null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException(e.getMessage());
        }
    }

    public static ResponseEntity<StreamingResponseBody> download(InputStream inputStream, String fileName){
        return download(inputStream, fileName, null);
    }

    /**
     * 文件下载
     */
    private static ResponseEntity<StreamingResponseBody> download(InputStream inputStream, String fileName, File deleteFile) {
        if (!StringUtils.hasText(fileName)) {
            throw new FileException("请指定下载文件的名称");
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

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=UTF-8''" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }


    /**
     * 获取文件名称
     * @param fullPath 文件全路径
     * @return 文件名称
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
     * 获取文件后缀名
     * @param fileName 文件名称
     * @return 文件后缀名
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
     * 删除文件
     * @param fileFullPath 文件全路径
     */
    public static void delete(String fileFullPath) {
        Path path = Paths.get(fileFullPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException("文件 " + path + " 删除失败");
        }
    }

    /**
     * 判断文件是否存在
     * @param path 文件全路径
     * @return 文件是否存在
     */
    public static boolean isExists(String path) {
        return Files.exists(Path.of(path));
    }

    /**
     * 文件打包到zip
     * @param inputStream 文件流
     * @param zipOutputStream zip输出流
     * @throws IOException io异常
     */
    private static void addToZipFile(InputStream inputStream, String fileOriginName,ZipOutputStream zipOutputStream) throws IOException {
        // 创建 ZIP 文件条目
        zipOutputStream.putNextEntry(new ZipEntry(fileOriginName));

        // 将文件内容写入 ZIP 文件条目中
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length);
        }

        // 关闭当前 ZIP 文件条目
        zipOutputStream.closeEntry();
        inputStream.close();
    }

    // 根据url获取文件名称
    private static String generateFullFilePathByURL(String fileUrl) {
        String fileNameByURL = getFileNameByURL(fileUrl);
        return generateFilePath(fileNameByURL);
    }

    // 生成文件路径，与文件名拼接
    private static String generateFilePath(String fileName) {
        // 业务编码为空时直接拼接日期和文件名
        return Paths.get(lihuaConfig.getUploadFilePath(), fileName).toString();
    }
}
