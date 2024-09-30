export interface CacheMonitor {
    /**
     * 缓存键值前缀
     */
    keyPrefix: string
    /**
     * 缓存标签
     */
    label: string
    /**
     * 缓存值
     */
    value: string
    /**
     * 过期时间
     */
    expireMinutes: number
}
