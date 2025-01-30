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