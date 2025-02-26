package com.lihua.utils.crypt;

import com.lihua.exception.ServiceException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用hash加密数据
 */
public class HashUtils {

    // 生成 MD5 哈希值
    public static String generateMD5(String input) {
        try {
            // 获取 MD5 消息摘要实例
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 将字节数组转换为十六进制字符串
            return bytesToHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("SHA-256 转换出错，转换字符为：" + input);
        }
    }

    // 生成 SHA-256 哈希值
    public static String generateSHA256(String input) {
        try {
            // 获取 SHA-256 消息摘要实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // 将字节数组转换为十六进制字符串
            return bytesToHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("SHA-256 转换出错，转换字符为：" + input);
        }
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}