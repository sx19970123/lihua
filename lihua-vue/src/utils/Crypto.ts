import CryptoJS from 'crypto-js';
import {JSEncrypt} from "jsencrypt";
import {createBrowserId} from "@/utils/BrowserId.ts";
import {getPublicKey} from "@/api/system/auth/Auth.ts";
// token 和 默认密码加密的密钥。后端需保持一致
const KEY:string = "LIHUA-AES-KEY"

// 数据加密
export const encrypt = (data: string):string => {
  return CryptoJS.AES.encrypt(data, KEY,{mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7}).toString()
}

// 数据解密
export const decrypt = (data: string):string => {
  return CryptoJS.AES.decrypt(data,KEY,{mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7}).toString(CryptoJS.enc.Utf8)
}

// 使用rsa公钥进行数据加密
export const rsaEncrypt = (input: string, publicKey: string): string | false => {
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);
  return encryptor.encrypt(input);
}

// 密码加密
export const rasEncryptPassword = (password: string): Promise<{ciphertext:string,requestKey:string}> => {
  return new Promise(async (resolve, reject) => {
    // 生成 requestKey
    const browserId = await createBrowserId();
    const requestKey = browserId + ':' +crypto.randomUUID();

    // 获取公钥
    const publicKeyResp = await getPublicKey(requestKey);
    if (publicKeyResp.code !== 200) {
      reject("业务异常")
    }

    const publicKey = publicKeyResp.data
    // 进行密码加密
    const ciphertext = rsaEncrypt(password, publicKey);
    if (!ciphertext) {
      reject("业务异常")
    }

    resolve({
      ciphertext: ciphertext as string,
      requestKey: requestKey
    })
  })
}