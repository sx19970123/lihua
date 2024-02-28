import request from "@/utils/request";

/**
 * 是否启用验证码
 */
export const enable = () => {
    return request({
        url: '/captcha/enable',
        method: 'get'
    })
}