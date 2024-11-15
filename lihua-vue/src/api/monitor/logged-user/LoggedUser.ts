import request from "@/utils/Request.ts"
import type {LoggedUserType} from "@/api/monitor/logged-user/type/LoggedUserType.ts";

// 查询登录用户列表
export const queryList = (username?: string, nickname?: string) => {
    return request<LoggedUserType[]>({
        url: "monitor/loggedUser",
        method: "GET",
        params: {
            username: username,
            nickname: nickname
        }
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
