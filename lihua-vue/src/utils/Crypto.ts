import CryptoJS from 'crypto-js';
import {JSEncrypt} from "jsencrypt";
import {createBrowserId} from "@/utils/BrowserId.ts";
import {getPublicKey} from "@/api/system/auth/Auth.ts";
import { v4 as uuidv4 } from 'uuid';
import {ResponseError} from "@/api/global/Type.ts";
// token 和 默认密码加密的密钥。后端需保持一致
const TOKEN_KEY:string = await createBrowserId()
const DEFAULT_PASSWORD_KEY = 'lihuaLIHUALIhuam';
// token数据加密
export const encrypt = (data: string):string => {
  return CryptoJS.AES.encrypt(data, TOKEN_KEY,{mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7}).toString()
}

// token数据解密
export const decrypt = (data: string):string => {
  return CryptoJS.AES.decrypt(data,TOKEN_KEY,{mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7}).toString(CryptoJS.enc.Utf8)
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