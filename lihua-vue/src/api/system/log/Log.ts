import request from "@/utils/Request.ts";
import type {SysLog, SysLogDTO} from "@/api/system/log/type/SysLog.ts";
import type {PageResponseType} from "@/api/global/Type.ts";

// 获取日志类型选项
export const getLogTypeOption = () => {
    return request<Array<{ value: string, label: string }>>({
        url: "system/log/option",
        method: "GET",
    })
}

// 分页查询日志
export const findPage = (data: SysLogDTO) => {
    return request<PageResponseType<SysLog>>({
        url: 'system/log/page',
        method: "POST",
        data: data
    })
}