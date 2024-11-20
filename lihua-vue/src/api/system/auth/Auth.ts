import request from "@/utils/Request.ts";
import type {AuthInfoType} from "@/api/system/auth/type/AuthInfoType.ts";

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
// 获取公钥
export const getPublicKey = (requestKey: string) => {
    return request<string>({
        url: '/system/publicKey/' + requestKey,
        method: 'get'
    })
}