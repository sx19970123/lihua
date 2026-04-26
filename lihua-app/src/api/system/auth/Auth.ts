import request from "@/utils/Request";
import type {AuthInfoType} from "@/api/system/auth/type/AuthInfoType";

// 获取用户信息
export const queryAuthInfo = () => {
    return request<AuthInfoType>({
        url: 'app/system/info',
        method: 'GET'
    })
}
// 刷新用户数据
export const reloadData = () => {
    return request({
        url: 'app/system/reloadData',
        method: 'POST'
    })
}

// 获取一次性令牌
export const getOnceToken = () => {
    return request<string>({
        url: 'app/system/onceToken',
        method: 'GET'
    })
}