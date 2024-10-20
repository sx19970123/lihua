package com.lihua.utils.crypt;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES对称加密解密
 */
public class AesUtils {

    /**
     * 生成 AES 密钥
     * @return 密文对象
     */
    @SneakyThrows
    public static SecretKey generateKey() {
        // 生成 AES 密钥
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        aes.init(256);
        return aes.generateKey();
    }

    /**
     * AES 加密
     * @param input 被加密的内容
     * @param secretKey 密钥
     * @return BASE64编码后的密文
     */
    @SneakyThrows
    public static String encrypt(byte[] input, SecretKey secretKey) {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(input));
    }

    /**
     * AES 解密为字符串
     * @param ciphertext 密文
     * @param secretKey 密钥
     * @return 解密字符串
     */
    public static String decryptToString(String ciphertext, SecretKey secretKey) {
        return new String(Base64.getDecoder().decode(decrypt(ciphertext, secretKey)), StandardCharsets.UTF_8);
    }

    /**
     * AES 解密为字节数组
     * @param ciphertext 密文
     * @param secretKey 密钥
     * @return 解密字节数组
     */
    @SneakyThrows
    public static byte[] decrypt(String ciphertext, SecretKey secretKey) {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(Base64.getDecoder().decode(ciphertext));
    }

    @SneakyThrows
    public static String decrypt(String ciphertext, SecretKey secretKey, IvParameterSpec iv) {
        // 初始化 AES 解密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        // 解密数据
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(original);
    }
}
