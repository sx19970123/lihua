import request from "@/utils/Request.ts";

/**
 * 默认下载url，拼接filePath可下载对应文件
 */
export const defaultDownloadURL = import.meta.env.VITE_APP_BASE_API + '/system/file/download?filePath='

/**
 * 单文件上传
 * @param file
 */
export const upload = (file: Blob) => {
    const formData = new FormData();
    formData.append('file', file)
    return request<string>({
        url: 'system/file/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}