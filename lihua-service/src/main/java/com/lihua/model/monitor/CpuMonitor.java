package com.lihua.model.monitor;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CpuMonitor {
    /**
     * 物理核心数
     */
    private String physicalCores;

    /**
     * 逻辑核心数
     */
    private String logicalCores;

    /**
     * 系统使用率
     */
    private String sysUse;

    /**
     * 用户使用率
     */
    private String userUse;

    /**
     * 等待率
     */
    private String await;

    /**
     * 空闲率
     */
    private String free;
}
