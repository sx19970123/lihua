import request from "@/utils/request.ts";
import type {AuthInfoType} from "@/api/system/auth/type/auth-info-type.ts";

// 获取用户信息
export const queryAuthInfo = () => {
    return request<AuthInfoType>({
        url: '/system/info',
        method: 'get'
    })
}
// 刷新用户数据
export const reloadData = () => {
    return request({
        url: '/system/reloadData',
        method: 'post'
    })
}

// 获取一次性令牌
export const getOnceToken = () => {
    return request<string>({
        url: '/system/onceToken',
        method: 'get'
    })
}