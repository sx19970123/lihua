import CryptoJS from 'crypto-js';
import {JSEncrypt} from "jsencrypt";
const KEY:string = '$2a$10$lz30WMTlgnfsnrJcrwpJTeaaHYoQeqpeJO2vAHk2lGAXJvw1LDWUG'

// 数据加密
export const encrypt = (data: string):string => {
  return CryptoJS.AES.encrypt(data, KEY,{mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7}).toString()
}

// 数据解密
export const decrypt = (data: string):string => {
  return CryptoJS.AES.decrypt(data,KEY,{mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7}).toString(CryptoJS.enc.Utf8)
}

// 使用rsa进行数据加密
export const rsaEncrypt = (input: string, publicKey: string): string | false => {
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);
  return encryptor.encrypt(input);
}