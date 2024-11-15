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
export const queryOperatePage = (data: SysLogDTO) => {
    return request<PageResponseType<SysLog>>({
        url: 'system/log/operate/page',
        method: "POST",
        data: data
    })
}

// 查询操作日志详情
export const queryOperateById = (id: string) => {
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
        url: 'system/log/operate/clear',
        method: "delete"
    })
}

// 导出操作日志
export const excelOperateExport = (data: SysLogDTO) => {
    return request<string>({
        url: 'system/log/operate/export',
        method: 'post',
        data: data
    })
}


// 导出登录日志
export const excelLoginExport = (data: SysLogDTO) => {
    return request<string>({
        url: 'system/log/login/export',
        method: 'post',
        data: data
    })
}


// 清空登录日志
export const clearLoginLog = () => {
    return request<SysLog>({
        url: 'system/log/login/clear',
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
export const queryLoginById = (id: string) => {
    return request<SysLog>({
        url: 'system/log/login/' + id,
        method: "get"
    })
}

// 查询登录日志详情
export const queryLoginByCacheKey = (cacheKey: string) => {
    return request<SysLog>({
        url: 'system/log/login/cacheKey/' + cacheKey,
        method: "get"
    })
}



// 登录日志查询
export const queryLoginPage = (data: SysLogDTO) => {
    return request<PageResponseType<SysLog>>({
        url: 'system/log/login/page',
        method: "POST",
        data: data
    })
}
