export interface MemoryMonitor {
    /** 总内存大小 */
    total: string;

    /** 已使用大小 */
    used: string;

    /** 空闲大小 */
    available: string;

    /** 使用率 */
    usagePercentage: string;
}