export interface DiskMonitor {
    /** 总数 */
    total: string;

    /** 已使用 */
    used: string;

    /** 空闲 */
    free: string;

    /** 使用率 */
    usagePercentage: string;
}