package com.lihua.monitor.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CpuMonitor {
    /**
     * 逻辑核心数
     */
    private String logicalCores;

    /**
     * 使用率
     */
    private String usage;

    /**
     * 空闲率
     */
    private String free;
}
