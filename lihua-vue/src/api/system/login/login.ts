import request from "@/utils/request";
interface LoginType {
    username: string,
    password: string,
}
// 用户登录
export const login =  (username: string,
                      password: string,
                       captchaVerification: string) => {
    const data: LoginType = {
        username,
        password
    }
    return request({
        url: '/system/login',
        method: 'post',
        data: data,
        params: {
            captchaVerification: captchaVerification
        }
    })
}


export const logout = () => {
    return request({
        url: "/logout",
        method: 'post'
    })
}
