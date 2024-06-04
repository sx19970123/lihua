import request from "@/utils/Request.ts";

/**
 * 是否启用验证码
 */
export const enable = () => {
    return request<boolean>({
        url: '/captcha/enable',
        method: 'get'
    })
}