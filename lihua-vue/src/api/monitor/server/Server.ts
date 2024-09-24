import request from "@/utils/Request.ts"
import type {ServerInfo} from "@/api/monitor/server/type/ServerInfo.ts";

// 获取服务器信息
export const serverInfo = () => {
    return request<ServerInfo>({
        url: 'monitor/server',
        method: 'get',
    })
}