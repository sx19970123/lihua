import request from "@/utils/request";

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

export const updatePassword = (oldPassword: string,newPassword: string) => {
    return request({
        url: '/system/user/password',
        params: {
            oldPassword: oldPassword,
            newPassword: newPassword
        },
        method: 'post'
    })
}
