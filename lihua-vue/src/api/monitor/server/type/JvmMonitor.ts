export interface JvmMonitor {
    /** java名称 */
    name: string;

    /** 版本 */
    version: string;

    /** 供应商 */
    vendor: string;

    /** 启动时间 */
    startTime: string;

    /** 运行时长 */
    runningTime: string;

    /** 运行参数 */
    inputArguments: string[];
}