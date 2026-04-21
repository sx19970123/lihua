import type {CpuMonitor} from "@/api/monitor/server/type/cpu-monitor.ts";
import type {MemoryMonitor} from "@/api/monitor/server/type/memory-monitor.ts";
import type {JvmMonitor} from "@/api/monitor/server/type/jvm-monitor.ts";
import type {DiskMonitor} from "@/api/monitor/server/type/disk-monitor.ts";

export interface ServerInfo {
    cpuMonitor: CpuMonitor;
    memoryMonitor: MemoryMonitor;
    jvmMonitor: JvmMonitor;
    diskMonitor: DiskMonitor;
}