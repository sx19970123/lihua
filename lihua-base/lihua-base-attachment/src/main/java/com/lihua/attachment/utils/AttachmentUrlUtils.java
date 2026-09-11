package com.lihua.attachment.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 附件访问 URL 解析（纯函数，无查库；公开链的行级 is_public 校验由 download 端点在请求时执行）
 */
public class AttachmentUrlUtils {

    /**
     * 下载入口相对路径（entry URL 前缀，LOCAL/OSS 同形；App 版经网关加 /app 前缀）
     */
    public static final String DOWNLOAD_URL_PREFIX = "/system/attachment/storage/download";

    /**
     * 公开链解析：[urlBasePath] + download?fullPath=<URL 编码>
     * @param path 附件对象键
     * @param urlBasePath 反代前缀（默认空，见 attachment.url-base-path）
     * @return 可直接访问的入口 URL
     */
    public static String resolvePublicUrl(String path, String urlBasePath) {
        return (urlBasePath == null ? "" : urlBasePath) + DOWNLOAD_URL_PREFIX + "?fullPath=" + URLEncoder.encode(path, StandardCharsets.UTF_8);
    }
}
