package com.lihua.utils.crypt;

import com.lihua.model.cryption.RasModel;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RAS非对称加密解密
 */
public class RasUtils {

    /**
     * 生成 RAS 加密公私密钥
     * @return 加密后的公私密钥及私钥的密钥
     */
    @SneakyThrows
    public static RasModel generateKey() {
        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(2048);
        KeyPair keyPair = rsa.generateKeyPair();

        // 获取 AES 密钥
        SecretKey secretKey = AesUtils.generateKey();

        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        return new RasModel(
            Base64.getEncoder().encodeToString(publicKey),
            // 对私钥再次进行加密处理
            AesUtils.encrypt(privateKey, secretKey),
            secretKey
        );
    }


    /**
     * 使用公钥进行AES加密
     * @param input 被加密的内容
     * @param rasModel 私钥 model
     * @return 加密后的数据
     */
    @SneakyThrows
    public static String encrypt(byte[] input, RasModel rasModel) {
        // 将公钥解码
        byte[] publicKeyByte = Base64.getDecoder().decode(rasModel.getPublicKey());
        // 获取公钥字节数组后通过X509Encoded和KeyFactory获取公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // 使用公钥进行加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 加密后的密文使用base64进行编码
        return Base64.getEncoder().encodeToString(cipher.doFinal(input));
    }

    /**
     * 使用私钥进行AES解密
     * @param ciphertext 密文
     * @param rasModel 私钥 model
     * @return 解密后的数据
     */
    @SneakyThrows
    public static String decrypt(String ciphertext, RasModel rasModel) {
        // 使用AES将私钥解密
        byte[] privateKeyByte = AesUtils.decrypt(rasModel.getPrivateKey(), rasModel.getPrivateKeySecretKey());

        // 获取公钥字节数组后通过PKCS8EncodedKeySpec和KeyFactory获取私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // 使用私钥进行解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(keySpec));

        // 获取解密后的字符串
        return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
    }

}
