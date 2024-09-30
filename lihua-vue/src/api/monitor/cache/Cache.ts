import request from "@/utils/Request.ts"
import type {CacheMonitor} from "@/api/monitor/cache/type/CacheMonitor.ts";

// 内存占用
export const memoryInfo = () => {
    return request<string>({
        url: 'monitor/cache/memory'
    })
}
// 内存占用
export const cacheKeyGroups = () => {
    return request<CacheMonitor[]>({
        url: 'monitor/cache/group'
    })
}
// 根据key前缀获取所有相关 key
export const cacheKeys = (keyPrefix: string) => {
    return request<string[]>({
        url: 'monitor/cache/prefix/' + keyPrefix,
    })
}
// 根据key获取缓存信息
export const cacheInfo = (key: string) => {
    return request<CacheMonitor>({
        url: 'monitor/cache/info',
        method: 'post',
        data: {
            key: key
        }
    })
}

// 删除缓存
export const remove = (key: string) => {
    return request<CacheMonitor>({
        url: 'monitor/cache/key',
        method: 'delete',
        data: {
            key: key
        }
    })
}
