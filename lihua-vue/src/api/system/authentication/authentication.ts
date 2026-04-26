import request from "@/utils/request.ts";

// 用户登录
export const login = (username: string, password: string, captchaVerification: string) => {
    return request<string>({
        url: '/system/auth/login',
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

// 用户注册
export const register = (username: string, password: string, confirmPassword: string, captchaVerification: string) => {
    return request<string>({
        url: '/system/auth/register',
        method: 'post',
        data: {
            username: username,
            password: password,
            confirmPassword: confirmPassword,
            captchaVerification: captchaVerification
        }
    })
}

// 重新加载数据
export const reloadData = () => {
    return request({
        url: '/system/auth/reloadData',
        method: 'post'
    })
}

// 获取一次性令牌
export const getOnceToken = () => {
    return request<string>({
        url: '/system/auth/onceToken',
        method: 'get'
    })
}
