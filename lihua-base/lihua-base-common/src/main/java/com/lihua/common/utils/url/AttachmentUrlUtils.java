package com.lihua.common.utils.url;

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
     * 公开链解析：download?fullPath=<URL 编码>（相对链；部署反代前缀由各端自行拼接，服务端不感知）
     * @param path 附件对象键
     * @return 下载入口相对 URL
     */
    public static String resolvePublicUrl(String path) {
        // 空值原样返回，不构造链（调用方对空 url 字段已有渲染兜底）
        if (path == null || path.isBlank()) {
            return path;
        }
        // 幂等保护：入参已是访问链（如会话缓存中残留的历史转换结果）时原样返回，避免二次包装
        if (path.startsWith(DOWNLOAD_URL_PREFIX)) {
            return path;
        }
        return DOWNLOAD_URL_PREFIX + "?fullPath=" + URLEncoder.encode(path, StandardCharsets.UTF_8);
    }
}
