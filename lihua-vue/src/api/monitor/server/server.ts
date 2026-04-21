import request from "@/utils/request.ts"
import type {ServerInfo} from "@/api/monitor/server/type/server-info.ts";

// 获取服务器信息
export const serverInfo = () => {
    return request<ServerInfo>({
        url: 'monitor/server',
        method: 'get',
    })
}