package com.lihua.model.monitor;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务器信息
 */
@Data
@Accessors(chain = true)
public class ServerInfo {

    /**
     * cpu信息
     */
    private CpuMonitor cpuMonitor;

    /**
     * 内存信息
     */
    private MemoryMonitor memoryMonitor;

    /**
     * java虚拟机信息
     */
    private JvmMonitor jvmMonitor;

    /**
     * 服务器硬盘信息
     */
    private DiskMonitor diskMonitor;

}
