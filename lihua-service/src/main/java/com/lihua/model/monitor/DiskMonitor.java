package com.lihua.model.monitor;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DiskMonitor {

    /**
     * 总数
     */
    private String total;

    /**
     * 已使用
     */
    private String used;

    /**
     * 空闲
     */
    private String free;

    /**
     * 使用率
     */
    private String usagePercentage;

}
