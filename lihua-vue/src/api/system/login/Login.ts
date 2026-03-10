import request from "@/utils/Request.ts";

// 用户登录
export const login = (username: string, password: string, captchaVerification: string) => {
    return request<string>({
        url: '/system/login',
        method: 'post',
        data: {
            username,
            password,
            captchaVerification
        }
    })
}

// 退出登录
export const logout = () => {
    return request({
        url: "/logout",
        method: 'post'
    })
}

// 是否开启用户注册功能
export const enableRegister = () => {
    return request<boolean>({
        url: 'system/enableRegister',
        method: 'get'
    })
}

// 检查用户名是否可用
export const checkUserName = (username: string) => {
    return request<boolean>({
        url: 'system/checkUserName/' + username,
        method: 'post'
    })
}

// 用户注册
export const register = (username: string, password: string, confirmPassword: string, captchaVerification: string) => {
    return request<string>({
        url: '/system/register',
        method: 'post',
        data: {
            username: username,
            password: password,
            confirmPassword: confirmPassword,
            captchaVerification: captchaVerification
        }
    })
}

// 登录后必要信息校验
export const getLoginSetting = () => {
    return request<string[]>({
        url: '/system/checkLoginSetting',
        method: 'get',
    })
}
