import request from "@/utils/request";
import type {UserInfo} from "@/api/system/user/type/user";

export const getUserInfo = () => {
    return request({
        url: '/system/user/info',
        method: 'get'
    })
}

export const saveTheme = (themeJson: string) => {
    return request({
        url: '/system/user/theme',
        data: {
          theme: themeJson
        },
        method: 'post'
    })
}

export const saveBasics = (userInfo: {avatar: string,nickname: string,gender:string,email:string,phoneNumber:string}) => {
    return request({
        url: '/system/user/basics',
        data: userInfo,
        method: 'post'
    })
}
