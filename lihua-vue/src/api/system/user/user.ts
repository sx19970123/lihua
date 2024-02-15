import request from "@/utils/request";

export const getUserInfo = () => {
    return request({
        url: '/system/user/info',
        method: 'get'
    })
}

export const saveTheme = (themeJson: string) => {
    return request({
        url: '/system2/user/theme',
        data: {
          theme: themeJson
        },
        method: 'post'
    })
}

export const getImage = () => {
    return request({
        url: '/image',
        method: 'get',
        responseType: 'blob'
    })
}
