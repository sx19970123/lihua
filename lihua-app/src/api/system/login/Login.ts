import request from "@/utils/Request";
import type { LoginType } from "@/api/system/login/type/LoginType"; 
import type { RegisterType } from "@/api/system/login/type/RegisterType"; 

// 用户登录
export const login = (data: LoginType) => {
    return request<string>({
        url: 'app/system/login',
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
        url: 'app/system/checkUserName/' + username,
        method: 'POST'
    })
}

// 用户注册
export const register = (data: RegisterType) => {
    return request<string>({
        url: 'app/system/register',
        method: 'POST',
        data: data
    })
}

