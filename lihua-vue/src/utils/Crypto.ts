import CryptoJS from 'crypto-js';
import {JSEncrypt} from "jsencrypt";
import {createBrowserId} from "@/utils/BrowserId.ts";
import {getPublicKey} from "@/api/system/auth/Auth.ts";
import { v4 as uuidv4 } from 'uuid';
import {ResponseError} from "@/api/global/Type.ts";
const DEFAULT_PASSWORD_KEY = 'lihuaLIHUALIhuam';
// 16 字节的 Key
const KEY = CryptoJS.enc.Utf8.parse("A1B2C3D4E5F6G7H8")
// 16 字节的 IV
const IV = CryptoJS.enc.Utf8.parse("H8G7F6E5D4C3B2A1")

// 加密
export const encrypt = (data: string): string => {
  const encrypted = CryptoJS.AES.encrypt(data, KEY, { iv: IV, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 })
  return encrypted.toString()
}

// 解密
export const decrypt = (data: string): string => {
  const decrypted = CryptoJS.AES.decrypt(data, KEY, { iv: IV, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 })
  return decrypted.toString(CryptoJS.enc.Utf8)
}

// 默认密码数据加密
export const defaultPasswordEncrypt = (defaultPassword: string) => {
  const key = CryptoJS.enc.Utf8.parse(DEFAULT_PASSWORD_KEY);
  const encrypted = CryptoJS.AES.encrypt(defaultPassword, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });
  return encrypted.toString();
}
// 默认密码解密
export const defaultPasswordDecrypt = (encryptedPassword: string) => {
  const key = CryptoJS.enc.Utf8.parse(DEFAULT_PASSWORD_KEY);
  // 解密
  const decrypted = CryptoJS.AES.decrypt(encryptedPassword, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });

  // 返回解密后的明文字符串
  return decrypted.toString(CryptoJS.enc.Utf8);
}

// 密码加密
export const rasEncryptPassword = (password: string): Promise<{ciphertext:string,requestKey:string}> => {
  return new Promise(async (resolve, reject) => {
    // 生成 requestKey
    const browserId = await createBrowserId();
    const requestKey = browserId + ':' + uuidv4();

    // 获取公钥
    const publicKeyResp = await getPublicKey(requestKey);
    if (publicKeyResp.code !== 200) {
      reject(new ResponseError(500, "业务异常"))
    }

    const publicKey = publicKeyResp.data
    // 进行密码加密
    const ciphertext = rsaEncrypt(password, publicKey);
    if (!ciphertext) {
      reject(new ResponseError(500, "业务异常"))
    }

    resolve({
      ciphertext: ciphertext as string,
      requestKey: requestKey
    })
  })
}

// 使用rsa公钥进行数据加密
const rsaEncrypt = (input: string, publicKey: string): string | false => {
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);
  return encryptor.encrypt(input);
}