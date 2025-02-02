import request from "@/utils/Request.ts";
import type {SysAttachment} from "@/api/system/attachment/type/SysAttachment.ts";

// 根据路径查询文件信息，用于附件组件数据回显
export const queryAttachmentInfoByPathList = (pathList: Array<string>) => {
    return request<Array<SysAttachment>>({
        url: "system/attachment/info",
        method: "post",
        data: pathList
    })
}

// 获取文件下载链接
export const getDownloadURL = (path: string) => {
    return request<string>({
        url: "system/attachment/url",
        method: "get",
        params: {
            path: path
        }
    })
}

// 删除附件
export const deleteAttachment = (path: string) => {
    return request({
        url: "system/attachment",
        method: "delete",
        params: {
            path: path
        }
    })
}

// 通过 md5值获取已上传分片附件的索引值
export const chunksUploadedIndex = (md5: string) => {
    return request<number[]>({
        url: `system/attachment/chunk/uploadedIndex/${md5}`,
        method: "get",
    })
}

// 保存附件信息（分片上传）
export const chunksSave = (data: SysAttachment) => {
    return request({
        url: "system/attachment/chunk/save",
        method: "post",
        data: data
    })
}

// 分片文件上传
export const chunksUpload = (file: Blob, md5: string, index: number, callback: Function) => {
    const formData = new FormData();
    formData.append('file', file)

    return request({
        url: `system/attachment/chunk/upload/${md5}/${index}`,
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (progressEvent) => {
            callback(progressEvent.bytes)
        }
    })
}