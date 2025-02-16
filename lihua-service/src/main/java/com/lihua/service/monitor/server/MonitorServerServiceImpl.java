package com.lihua.service.monitor.server;
import com.lihua.model.monitor.*;
import lombok.SneakyThrows;
import oshi.SystemInfo;
import org.springframework.stereotype.Service;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Service
public class MonitorServerServiceImpl implements MonitorServerService {

    private final SystemInfo systemInfo = new SystemInfo();
    private final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private final CentralProcessor processor = hardware.getProcessor();
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
    @SneakyThrows
    private CpuMonitor getCpuInfo() {
        CpuMonitor cpu = new CpuMonitor();
        int physicalCores = processor.getPhysicalProcessorCount();
        int logicalCores = processor.getLogicalProcessorCount();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        TimeUnit.SECONDS.sleep(1);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        cpu.setPhysicalCores(String.valueOf(physicalCores))
            .setLogicalCores(String.valueOf(logicalCores))
            .setSysUse(new DecimalFormat("#.##").format(cSys * 1.0 / totalCpu * 100))
            .setUserUse(new DecimalFormat("#.##").format(user * 1.0 / totalCpu * 100))
            .setAwait(new DecimalFormat("#.##").format(user * 1.0 / totalCpu * 100))
            .setFree(new DecimalFormat("#.##").format(idle * 1.0 / totalCpu * 100));

        return cpu;
    }

    // 获取内存信息
    private MemoryMonitor getMemoryInfo() {
        MemoryMonitor monitor = new MemoryMonitor();
        GlobalMemory memory = hardware.getMemory();

        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
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
