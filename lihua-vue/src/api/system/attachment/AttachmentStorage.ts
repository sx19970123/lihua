import request from "@/utils/Request.ts";
import type {SysAttachment} from "@/api/system/attachment/type/SysAttachment.ts";


// 根据md5查询附件是否存在
export const existsAttachmentByMd5 = (md5: string, originFileName: string) => {
    return request<string>({
        url: `system/attachment/storage/exists/${md5}/${encodeURIComponent(originFileName)}`,
        method: "get"
    })
}

// 根据路径查询文件信息，用于附件组件数据回显
export const queryAttachmentInfoByIds = (ids: string[]) => {
    return request<Array<SysAttachment>>({
        url: "system/attachment/storage/info",
        method: "post",
        data: ids
    })
}

// 获取文件下载链接
export const getDownloadURL = (id: string, expireTime?: string) => {
    return request<string>({
        url: `system/attachment/storage/url/${id}`,
        method: "get",
        params: {
            expireTime: expireTime
        }
    })
}

// 附件业务删除
export const deleteFromBusiness = (id: string) => {
    return request({
        url: `system/attachment/storage/business/${id}`,
        method: "delete"
    })
}

// 开始分片上传（返回uploadId）
export const chunksUploadStart = (data: SysAttachment) => {
    return request<{uploadId: string, attachmentId: string}>({
        url: `system/attachment/storage/chunk/start`,
        method: "post",
        data: data
    })
}

// 通过 uploadId值获取已上传分片附件的索引值
export const chunksUploadedIndex = (uploadId: string) => {
    return request<number[]>({
        url: `system/attachment/storage/chunk/uploadedIndex/${uploadId}`,
        method: "get",
    })
}

//  附件上传
export const upload = (file: File, businessCode: string, businessName: string) => {
    const formData = new FormData();
    formData.append('file', file)
    formData.append('businessCode', businessCode)
    formData.append('businessName', businessName)
    formData.append('originalName', file.name)
    formData.append('uploadMode', "0")
    formData.append('size', file.size.toString())
    formData.append('type', file.type)
    return request<string>({
        url: "system/attachment/storage/upload",
        method: "post",
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
    })
}

// 文件秒传
export const fastUpload = (data: SysAttachment) => {
    return request<string>({
        url: "system/attachment/storage/fast/upload",
        method: "post",
        data: data
    })
}

// 分片文件上传
export const chunksUpload = (file: Blob, uploadId: string, md5: string, index: number, callback: Function) => {
    const formData = new FormData();
    formData.append('file', file)

    return request({
        url: `system/attachment/storage/chunk/upload/${uploadId}/${index}?lh+attachment_md5=${md5}`,
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (progressEvent) => {
            callback(progressEvent.bytes)
        },
    })
}

// 文件合并
export const chunksMerge = (data: SysAttachment, index: number) => {
    return request<string>({
        url: `system/attachment/storage/chunk/merge/${index}`,
        method: 'post',
        data: data
    })
}

// 公开附件下载
export const publicAttachmentDownload = (id: string) => {
    return request<Blob>({
        url: `system/attachment/storage/download/p/${id}`,
        method: 'get',
        responseType: 'blob'
    })
}