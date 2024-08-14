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

// 操作日志查询
export const findOperatePage = (data: SysLogDTO) => {
    return request<PageResponseType<SysLog>>({
        url: 'system/log/operate/page',
        method: "POST",
        data: data
    })
}

// 查询操作日志详情
export const findOperateById = (id: string) => {
    return request<SysLog>({
        url: 'system/log/operate/' + id,
        method: "get"
    })
}

// 删除操作日志
export const deleteOperateByIds = (ids: string[]) => {
    return request<SysLog>({
        url: 'system/log/operate',
        method: "delete",
        data: ids
    })
}

// 清空操作日志
export const clearOperateLog = () => {
    return request<SysLog>({
        url: 'system/log/login/clear',
        method: "delete"
    })
}




// 清空登录日志
export const clearLoginLog = () => {
    return request<SysLog>({
        url: 'system/log/operate/clear',
        method: "delete"
    })
}


// 删除登录日志
export const deleteLoginByIds = (ids: string[]) => {
    return request<SysLog>({
        url: 'system/log/login',
        method: "delete",
        data: ids
    })
}

// 查询登录日志详情
export const findLoginById = (id: string) => {
    return request<SysLog>({
        url: 'system/log/login/' + id,
        method: "get"
    })
}

// 登录日志查询
export const findLoginPage = (data: SysLogDTO) => {
    return request<PageResponseType<SysLog>>({
        url: 'system/log/login/page',
        method: "POST",
        data: data
    })
}

// 删除日志