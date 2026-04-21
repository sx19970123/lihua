import {useStorage} from "@vueuse/core";

const TOKEN_KEY: string = "lihua_token"
const LOGIN_SETTING_COMPLETE_KEY: string = "lihua_login_setting_complete"

const tokenStorage = useStorage<null | string>(TOKEN_KEY, null);
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
    getLoginSettingResult,
    setLoginSettingResult,
    removeLoginSettingResult
}
