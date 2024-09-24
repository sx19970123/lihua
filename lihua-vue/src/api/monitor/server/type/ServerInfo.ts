import type {CpuMonitor} from "@/api/monitor/server/type/CpuMonitor.ts";
import type {MemoryMonitor} from "@/api/monitor/server/type/MemoryMonitor.ts";
import type {JvmMonitor} from "@/api/monitor/server/type/JvmMonitor.ts";
import type {DiskMonitor} from "@/api/monitor/server/type/DiskMonitor.ts";

export interface ServerInfo {
    cpuMonitor: CpuMonitor;
    memoryMonitor: MemoryMonitor;
    jvmMonitor: JvmMonitor;
    diskMonitor: DiskMonitor;
}