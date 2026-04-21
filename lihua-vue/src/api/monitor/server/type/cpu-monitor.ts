export interface CpuMonitor {
    /** 物理核心数 */
    physicalCores: string;

    /** 逻辑核心数 */
    logicalCores: string;

    /** 系统使用率 */
    sysUse: string;

    /** 用户使用率 */
    userUse: string;

    /** 等待率 */
    await: string;

    /** 空闲率 */
    free: string;
}