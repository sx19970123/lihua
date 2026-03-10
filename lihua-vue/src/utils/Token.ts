import {decrypt, encrypt} from "@/utils/Crypto.ts"
import {useStorage} from "@vueuse/core";

const TOKEN_KEY: string = "lihua_token"

const USERNAME_KEY: string = "lihua_username"

const PASSWORD_KEY: string = "lihua_password"

const REMEMBER_ME_KEY: string = "lihua_rememberMe"

const LOGIN_SETTING_COMPLETE_KEY: string = "lihua_login_setting_complete"

const tokenStorage = useStorage<null | string>(TOKEN_KEY, null);
const usernameStorage = useStorage<null | string>(USERNAME_KEY, null);
const passwordStorage = useStorage<null | string>(PASSWORD_KEY, null);
const rememberMeStorage = useStorage<null | boolean>(REMEMBER_ME_KEY, null);
const loginSettingCompleteStorage = useStorage<null | boolean>(LOGIN_SETTING_COMPLETE_KEY, null);

// token
const getToken = ():string => {
    return tokenStorage.value || ''
}

const setToken = (token: string):void => {
    tokenStorage.value = token
}

const removeToken = () => {
    tokenStorage.value = null
}

// username
const getUsername = ():string => {
    return usernameStorage.value || ''
}

const setUsername = (username:string):void => {
    usernameStorage.value = username
}

const removeUsername = () => {
    usernameStorage.value = null
}

// password
const getPassword = (): string => {
    const pwd = passwordStorage.value
    if (pwd) {
        try {
            return decrypt(pwd)
        } catch (e) {
            forgetMe()
            console.error("cookie密码解密异常",e)
        }
    }
    return pwd || '';
}

const setPassword = (password: string): void => {
    passwordStorage.value = encrypt(password)
}

const removePassword = () => {
    passwordStorage.value = null
}

const enableRememberMe = (): boolean => {
    return !!rememberMeStorage.value
}

// 记住我
const rememberMe = (username:string, password:string) => {
    rememberMeStorage.value = true
    setUsername(username)
    setPassword(password)
}

// 忘记我
const forgetMe = () => {
    rememberMeStorage.value = null
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

// 获取登录后设置结果
const getLoginSettingResult = (): boolean | null => {
    return loginSettingCompleteStorage.value
}
// 登录设置完成后记录结果
const setLoginSettingResult = () => {
    loginSettingCompleteStorage.value = true
}
// 删除登录后设置信息
const removeLoginSettingResult = () => {
    loginSettingCompleteStorage.value = null
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
