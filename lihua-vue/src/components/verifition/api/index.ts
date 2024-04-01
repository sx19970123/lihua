import request from "@/utils/request";

//获取验证图片  以及token
export function reqGet(data: any) {
    return request({
        url: '/captcha/get',
        method: 'post',
        data
    })
}

//滑动或者点选验证
export function reqCheck(data: any) {
    return request({
        url: '/captcha/check',
        method: 'post',
        data
    })
}


