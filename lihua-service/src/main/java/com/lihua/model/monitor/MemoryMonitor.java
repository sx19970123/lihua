package com.lihua.model.monitor;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MemoryMonitor {

    /**
     * 总内存大小
     */
    private String total;

    /**
     * 已使用大小
     */
    private String used;

    /**
     * 空闲大小
     */
    private String available;

    /**
     * 使用率
     */
    private String usagePercentage;

}
