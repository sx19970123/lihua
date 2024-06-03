import request from "@/utils/request";
import type {AuthInfoType} from "@/api/type.ts";

export const getProfileInfo = () => {
    return request<AuthInfoType>({
        url: '/system/profile/info',
        method: 'get'
    })
}

export const saveTheme = (themeJson: string) => {
    return request({
        url: '/system/profile/theme',
        data: {
          theme: themeJson
        },
        method: 'post'
    })
}

export const saveBasics = (userInfo: {avatar: string,nickname: string,gender:string,email:string,phoneNumber:string}) => {
    return request({
        url: '/system/profile/basics',
        data: userInfo,
        method: 'post'
    })
}

export const updatePassword = (oldPassword: string,newPassword: string) => {
    return request({
        url: '/system/profile/password',
        params: {
            oldPassword: oldPassword,
            newPassword: newPassword
        },
        method: 'post'
    })
}
