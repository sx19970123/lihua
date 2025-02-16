package com.lihua.model.monitor;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class JvmMonitor {

    /**
     * java名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 供应商
     */
    private String vendor;

    /**
     * 启动时间
     */
    private LocalDateTime startTime;

    /**
     * 运行时长
     */
    private String RunningTime;

    /**
     * 运行参数
     */
    private List<String> inputArguments;
}
