export interface IntervalUpdatePassword {
    /**
     * 是否启用
     */
    enable: boolean;

    /**
     * 周期
     */
    interval?: number;

    /**
     * 周期单位
     */
    unit?: 'day' | 'week' | 'month' | 'year';
}
