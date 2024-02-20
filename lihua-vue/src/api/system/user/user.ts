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

export const saveAvatarFile = (file: Blob) => {
    const formData = new FormData()
    formData.append('avatarFile',file)

    return request({
        url: '/system/user/avatar',
        data: formData,
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

export const getImg = (path) => {
    return request({
        url: path,
        method: 'get'
    })
}