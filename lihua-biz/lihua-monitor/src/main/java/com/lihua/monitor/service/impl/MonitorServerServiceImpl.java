package com.lihua.monitor.service.impl;

import com.lihua.monitor.model.*;
import com.lihua.monitor.service.MonitorServerService;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class MonitorServerServiceImpl implements MonitorServerService {

    private final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private final double convertConstant = 1024.0 * 1024.0 * 1024.0;

    @Override
    public ServerInfo serverInfo() {
        return new ServerInfo()
                .setCpuMonitor(getCpuInfo())
                .setJvmMonitor(jvmInfo())
                .setDiskMonitor(diskInfo())
                .setMemoryMonitor(getMemoryInfo());
    }

    // 获取cpu信息
    private CpuMonitor getCpuInfo() {
        double cpuLoad = osBean.getCpuLoad();
        // bean 初始化初期或不可用时返回负值/NaN，按 0 处理
        if (Double.isNaN(cpuLoad) || cpuLoad < 0) {
            cpuLoad = 0;
        }

        return new CpuMonitor()
                .setLogicalCores(String.valueOf(osBean.getAvailableProcessors()))
                .setUsage(new DecimalFormat("#.##").format(cpuLoad * 100))
                .setFree(new DecimalFormat("#.##").format((1 - cpuLoad) * 100));
    }

    // 获取内存信息
    private MemoryMonitor getMemoryInfo() {
        MemoryMonitor monitor = new MemoryMonitor();
        long totalMemory = osBean.getTotalMemorySize();
        long availableMemory = osBean.getFreeMemorySize();
        long usedMemory = totalMemory - availableMemory;

        return monitor.setTotal(new DecimalFormat("#.##").format(totalMemory / convertConstant))
                .setAvailable(new DecimalFormat("#.##").format(availableMemory / convertConstant))
                .setUsed(new DecimalFormat("#.##").format(usedMemory / convertConstant))
                .setUsagePercentage(new DecimalFormat("#.##").format(Double.parseDouble(monitor.getUsed())/Double.parseDouble(monitor.getTotal()) * 100));
    }

    // 获取jvm信息
    private JvmMonitor jvmInfo() {
        JvmMonitor jvm = new JvmMonitor();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return jvm.setName(runtimeMXBean.getVmName())
            .setVersion(runtimeMXBean.getVmVersion())
            .setVendor(runtimeMXBean.getVmVendor())
            .setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(runtimeMXBean.getStartTime()), ZoneId.systemDefault()))
            .setRunningTime(convertMsToDHM(runtimeMXBean.getUptime()))
            .setInputArguments(runtimeMXBean.getInputArguments());
    }

    // 获取磁盘空间信息
    private DiskMonitor diskInfo() {
        DiskMonitor diskMonitor = new DiskMonitor();
        File file = new File("/");

        // 获取总空间、可用空间和已用空间
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        return diskMonitor.setTotal(new DecimalFormat("#.##").format(totalSpace / convertConstant))
                .setUsed(new DecimalFormat("#.##").format(usedSpace / convertConstant))
                .setFree(new DecimalFormat("#.##").format(freeSpace / convertConstant))
                .setUsagePercentage(new DecimalFormat("#.##").format(Double.parseDouble(diskMonitor.getUsed())/Double.parseDouble(diskMonitor.getTotal()) * 100));

    }

    // 毫秒数转为 天-时-分
    private String convertMsToDHM(long milliseconds) {
        // 转换为秒
        long seconds = milliseconds / 1000;
        // 计算天数
        long days = seconds / (24 * 3600);
        // 剩余的秒数
        seconds %= (24 * 3600);
        // 计算小时
        long hours = seconds / 3600;
        // 剩余的秒数
        seconds %= 3600;
        // 计算分钟
        long minutes = seconds / 60;

        return String.format("%d天%d小时%d分钟", days, hours, minutes);
    }

}
