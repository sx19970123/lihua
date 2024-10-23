import Cookies from "js-cookie"
import {encrypt,decrypt} from "./Crypto.ts"


const TOKEN_KEY: string = "lihua_token"

const USERNAME_KEY: string = "lihua_username"

const PASSWORD_KEY: string = "lihua_password"

const REMEMBER_ME_KEY: string = "lihua_rememberMe"

const LOGIN_SETTING_COMPLETE_KEY: string = "lihua_login_setting_complete"

// token
const getToken = ():string => {
    return Cookies.get(TOKEN_KEY)
}

const setToken = (token: string):void => {
    Cookies.set(TOKEN_KEY,token)
}

const removeToken = () => {
    Cookies.remove(TOKEN_KEY)
}

// username
const getUsername = ():string => {
    return Cookies.get(USERNAME_KEY)
}

const setUsername = (username:string, expires: number):void => {
    Cookies.set(USERNAME_KEY,username, { expires: expires })
}

const removeUsername = () => {
    Cookies.remove(USERNAME_KEY)
}

// password
const getPassword = (): string => {
    const pwd = Cookies.get(PASSWORD_KEY)
    if (pwd) {
        try {
            return decrypt(pwd)
        } catch (e) {
            forgetMe()
            console.error("cookie密码解密异常",e)
        }
    }
    return pwd;
}

const setPassword = (password:string, expires: number): void => {
    Cookies.set(PASSWORD_KEY,encrypt(password), { expires: expires })
}

const removePassword = () => {
    Cookies.remove(PASSWORD_KEY)
}

const enableRememberMe = ():boolean => {
    return Cookies.get(REMEMBER_ME_KEY) === 'true' || Cookies.get(REMEMBER_ME_KEY)
}

// 记住我
const rememberMe = (username:string, password:string) => {
    // 记住我过期时间设置为30天
    const expires = 30
    Cookies.set(REMEMBER_ME_KEY,true, { expires: expires })
    setUsername(username, expires)
    setPassword(password, expires)
}

// 忘记我
const forgetMe = () => {
    Cookies.remove(REMEMBER_ME_KEY)
    removeUsername()
    removePassword()
}

// 获取账号密码
const getUsernamePassword = () => {
    return {
        username: getUsername(),
        password: getPassword()
    }
}

// 获取登陆后设置结果
const getLoginSettingResult = (): boolean | undefined => {
    return Cookies.get(LOGIN_SETTING_COMPLETE_KEY) as boolean | undefined
}
// 登录设置完成后记录结果
const setLoginSettingResult = () => {
    Cookies.set(LOGIN_SETTING_COMPLETE_KEY,true)
}
// 删除登陆后设置信息
const removeLoginSettingResult = () => {
    Cookies.remove(LOGIN_SETTING_COMPLETE_KEY)
}

export default {
    getToken,
    setToken,
    removeToken,
    rememberMe,
    forgetMe,
    getUsernamePassword,
    enableRememberMe,
    getLoginSettingResult,
    setLoginSettingResult,
    removeLoginSettingResult
}
