import request, {attachmentUpload} from "@/utils/request";
import type {SysAttachment} from "@/api/system/attachment/type/sys-attachment";

// 根据路径查询文件信息，用于附件组件数据回显
export const queryAttachmentInfoByIds = (ids: string[]) => {
    return request<Array<SysAttachment>>({
        url: "app/system/attachment/storage/info",
        method: "POST",
        data: ids
    })
}

//  附件上传，返回路径
export const publicUpload = (filePath: string, businessCode: string) => {
    return attachmentUpload<string>({
        url: "app/system/attachment/storage/public/upload",
        filePath: filePath,
        name: 'file',
        formData: {
            businessCode
        },
        header: {'Content-Type': 'multipart/form-data'}
    })
}

//  附件上传，返回附件表id
export const upload = (filePath: string, businessCode: string, businessName: string, md5?: string) => {
	return attachmentUpload<string>({
		url: "app/system/attachment/storage/upload",
		filePath: filePath,
		name: 'file',
		formData: {
			md5,
			businessCode,
			businessName,
			uploadMode: "0"
		},
		header: {'Content-Type': 'multipart/form-data'}
	})
}

// 文件秒传
export const fastUpload = (originalName: string, businessCode: string, businessName: string, size: number, md5?: string) => {
    return request<string>({
        url: "app/system/attachment/storage/fast/upload",
        method: "POST",
        data: {
			originalName,
			md5,
			size,
			businessCode,
			businessName,
			uploadMode: "2"
		}
    })
}

// 根据md5查询附件是否存在
export const existsAttachmentByMd5 = (md5: string, originFileName?: string) => {
    return request<boolean>({
        url: `app/system/attachment/storage/exists`,
        method: "POST",
        data: {
            md5,
            originFileName,
        }
    })
}

// 附件业务删除
export const deleteFromBusiness = (ids: string[]) => {
    return request({
        url: `app/system/attachment/storage/business`,
        method: "DELETE",
		data: ids
    })
}