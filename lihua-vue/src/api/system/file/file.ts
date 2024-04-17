import request from "@/utils/request";

/**
 * 上传用户头像
 * @param avatar
 */
export const uploadAvatar = (avatar: Blob) => {
    const formData = new FormData();
    formData.append('avatarFile',avatar)
    return request<string>({
        url: 'system/file/avatar',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

export const imagePreview = (fileName: string) => {
    return request({
        url: `system/file/imagePreview/${fileName}`,
        method: 'get',
        responseType:'blob'
    })
}
