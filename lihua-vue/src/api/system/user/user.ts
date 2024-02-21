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

export const saveUserInfo = (userInfo: UserInfo) => {
    return request({
        url: '/system/user/save',
        data: userInfo,
        method: 'post'
    })
}
