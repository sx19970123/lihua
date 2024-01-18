import request from "@/utils/request";

export const getUserInfo = () => {
    return request({
        url: '/system/user/info',
        method: 'get'
    })
}
