import request from "@/utils/Request.ts";
import type {SysAttachment, SysAttachmentDTO, SysAttachmentVO} from "@/api/system/attachment/type/SysAttachment.ts";
import type {PageResponseType} from "@/api/global/Type.ts";

// 分页查询附件列表
export const queryPage = (data: SysAttachmentDTO) => {
    return request<PageResponseType<SysAttachment>>({
        url: "/system/attachment/page",
        method: "POST",
        data: data,
    })
}

// 删除数据
export const deleteData = (ids: string[]) => {
    return request({
        url: '/system/attachment',
        data: ids,
        method: 'delete'
    })
}

// 强制删除数据
export const forceDeleteData = (id: string) => {
    return request({
        url: '/system/attachment/force/' + id,
        method: 'delete'
    })
}

// 根据主键查询
export const queryById = (id: string) => {
    return request<SysAttachmentVO>({
        url: `/system/attachment/${id}`,
        method: "get"
    })
}

// 获取文件下载链接
export const getDownloadURL = (id: string, expireTime?: string) => {
    return request<string>({
        url: `system/attachment/url/${id}`,
        method: "get",
        params: {
            expireTime: expireTime
        }
    })
}