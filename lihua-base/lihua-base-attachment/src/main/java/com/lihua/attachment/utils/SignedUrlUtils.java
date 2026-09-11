package com.lihua.attachment.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 附件下载链接签名工具（HMAC-SHA256）
 * <p>令牌形态：{@code <过期毫秒时间戳>.<Base64Url(附件路径)>.<HMAC-SHA256Hex(路径::过期时间戳)>}
 * ——路径与时效明文携带，签名防伪造防篡改；签名密钥由配置注入（attachment.download-sign-key），不出服务器
 */
public class SignedUrlUtils {

    private static final String SIGN_SEPARATOR = "::";

    /**
     * 签发下载链接令牌
     * @param path 附件全路径
     * @param expireTimeMillis 过期毫秒时间戳
     * @param secretKey 签名密钥
     * @return 令牌字符串
     */
    public static String sign(String path, long expireTimeMillis, String secretKey) {
        String expire = String.valueOf(expireTimeMillis);
        String mac = HmacUtils.hmacSha256Hex(secretKey.getBytes(StandardCharsets.UTF_8), signedContent(path, expire).getBytes(StandardCharsets.UTF_8));
        String encodedPath = Base64.getUrlEncoder().withoutPadding().encodeToString(path.getBytes(StandardCharsets.UTF_8));
        return expire + "." + encodedPath + "." + mac;
    }

    /**
     * 校验令牌并还原签发内容
     * @param token 令牌字符串
     * @param secretKey 签名密钥
     * @return 格式与签名均合法时返回签发内容，否则返回 null（调用方按非法链接处理）
     */
    public static SignedToken verify(String token, String secretKey) {
        if (token == null || token.isBlank()) {
            return null;
        }
        // 三段结构：过期时间戳.路径.签名
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return null;
        }
        long expireTimeMillis;
        try {
            expireTimeMillis = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            return null;
        }
        // HMAC-SHA256 输出固定 64 位小写十六进制
        if (!parts[2].matches("[0-9a-f]{64}")) {
            return null;
        }
        String path;
        try {
            path = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null;
        }
        String expected = HmacUtils.hmacSha256Hex(secretKey.getBytes(StandardCharsets.UTF_8), signedContent(path, parts[0]).getBytes(StandardCharsets.UTF_8));
        // 常量时间比较，防时序侧信道
        if (!MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
            return null;
        }
        return new SignedToken(path, expireTimeMillis);
    }

    private static String signedContent(String path, String expire) {
        return path + SIGN_SEPARATOR + expire;
    }

    /**
     * 令牌还原的签发内容
     */
    @Getter
    @AllArgsConstructor
    public static class SignedToken {
        private final String path;
        private final long expireTimeMillis;
    }
}
