package com.lihua.model.cryption;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.SecretKey;

@Data
@AllArgsConstructor
public class RasModel {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 私钥的密钥
     */
    private SecretKey privateKeySecretKey;
}
