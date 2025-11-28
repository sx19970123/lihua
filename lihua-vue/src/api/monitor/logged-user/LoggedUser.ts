import request from "@/utils/Request.ts"
import type {LoggedUserQueryParams, LoggedUserType} from "@/api/monitor/logged-user/type/LoggedUserType.ts";

// 查询登录用户列表
export const queryList = (params: LoggedUserQueryParams) => {
    return request<LoggedUserType[]>({
        url: "monitor/loggedUser",
        method: "GET",
        params: params
    })
}

// 强退用户
export const forceLogout = (cacheKeys: string[]) => {
    return request({
        url: "monitor/loggedUser",
        method: "DELETE",
        data: cacheKeys
    })
}
