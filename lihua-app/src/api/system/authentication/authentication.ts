import request from "@/utils/request";
import type {LoginType} from "@/api/system/authentication/type/login-type";
import type {RegisterType} from "@/api/system/authentication/type/register-type";

// 用户登录
export const login = (data: LoginType) => {
    return request<string>({
        url: 'app/system/auth/login',
        method: 'POST',
        data: data
    })
}

// 退出登录
export const logout = () => {
    return request({
        url: "logout",
        method: 'POST'
    })
}

// 检查用户名是否可用
export const checkUserName = (username: string) => {
    return request<boolean>({
        url: 'app/system/user/checkUserName/' + username,
        method: 'POST'
    })
}

// 用户注册
export const register = (data: RegisterType) => {
    return request<string>({
        url: 'app/system/auth/register',
        method: 'POST',
        data: data
    })
}

// 刷新用户数据
export const reloadData = () => {
    return request({
        url: 'app/system/auth/reloadData',
        method: 'POST'
    })
}

// 获取一次性令牌
export const getOnceToken = () => {
    return request<string>({
        url: 'app/system/auth/onceToken',
        method: 'GET'
    })
}