import request from "@/utils/request";
interface LoginType {
    username: string,
    password: string,
    code?: string
}
// 用户登录
export const login =  (username: string,
                      password: string,
                      code?: string) => {
    const data: LoginType = {
        username,
        password,
        code
    }
    return request({
        url: '/system/login',
        method: 'post',
        data: data
    })
}


export const logout = () => {
    return request({
        url: "/logout",
        method: 'post'
    })
}