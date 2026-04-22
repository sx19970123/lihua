import {decrypt, encrypt} from "@/utils/crypto.ts"
import {useStorage} from "@vueuse/core";

const USERNAME_KEY: string = "lihua_username"
const PASSWORD_KEY: string = "lihua_password"
const REMEMBER_ME_KEY: string = "lihua_remember_me"

const usernameStorage = useStorage<null | string>(USERNAME_KEY, null);
const passwordStorage = useStorage<null | string>(PASSWORD_KEY, null);
const rememberMeStorage = useStorage<null | boolean>(REMEMBER_ME_KEY, null);

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

export default {
    rememberMe,
    forgetMe,
    getUsernamePassword,
    enableRememberMe,
}
