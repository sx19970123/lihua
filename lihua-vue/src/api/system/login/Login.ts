import request from "@/utils/Request.ts";
interface LoginType {
    username: string,
    password: string,
    captchaVerification: string,
    requestKey: string
}
// 用户登录
export const login = (username: string,
                      password: string,
                      captchaVerification: string,
                      requestKey: string) => {
    return request<string>({
        url: '/system/login',
        method: 'post',
        data: {
            username,
            password,
            captchaVerification,
            requestKey
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

// 检查用户名是否可用
export const checkUserName = (username: string) => {
    return request<boolean>({
        url: 'system/checkUserName/' + username,
        method: 'post'
    })
}

// 用户注册
export const register = (username: string,
                         password: string,
                         passwordRequestKey: string,
                         confirmPassword: string,
                         confirmPasswordRequestKey: string,
                         captchaVerification: string
                         ) => {
    return request<string>({
        url: '/system/register',
        method: 'post',
        data: {
            username: username,
            password: password,
            passwordRequestKey: passwordRequestKey,
            confirmPassword: confirmPassword,
            confirmPasswordRequestKey: confirmPasswordRequestKey,
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
