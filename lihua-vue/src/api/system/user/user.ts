import request from "@/utils/request";

export const getUserInfo = () => {
    return request({
        url: '/system/user/info',
        method: 'get'
    })
}


export const getImage = () => {
    return request({
        url: '/image',
        method: 'get',
        responseType: 'blob'
    })
}
