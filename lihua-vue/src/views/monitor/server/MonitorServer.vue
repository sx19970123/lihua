<template>
  <a-flex vertical :gap="16">
    <a-card :style="{border: 'none'}">
      <a-descriptions title="中央处理器" bordered >
        <a-descriptions-item label="物理核心数">{{info?.cpuMonitor.physicalCores}}</a-descriptions-item>
        <a-descriptions-item label="逻辑核心数">{{info?.cpuMonitor.logicalCores}}</a-descriptions-item>
        <a-descriptions-item label="系统使用率">{{info?.cpuMonitor.sysUse ? info?.cpuMonitor.sysUse + '%' : ''}}</a-descriptions-item>
        <a-descriptions-item label="用户使用率">{{info?.cpuMonitor.userUse ? info?.cpuMonitor.userUse + '%' : ''}}</a-descriptions-item>
        <a-descriptions-item label="等待率">{{info?.cpuMonitor.await ? info?.cpuMonitor.await + '%' : ''}}</a-descriptions-item>
        <a-descriptions-item label="空闲率">{{info?.cpuMonitor.free ? info?.cpuMonitor.free + '%' : ''}}</a-descriptions-item>
      </a-descriptions>
    </a-card>
    <a-card :style="{border: 'none'}">
      <a-descriptions title="Java 虚拟机" bordered>
        <a-descriptions-item label="Java名称">{{info?.jvmMonitor.name}}</a-descriptions-item>
        <a-descriptions-item label="Java版本">{{info?.jvmMonitor.version}}</a-descriptions-item>
        <a-descriptions-item label="供应商">{{info?.jvmMonitor.vendor}}</a-descriptions-item>
        <a-descriptions-item label="启动时间">{{dayjs(info?.jvmMonitor.startTime).format('YYYY-MM-DD HH:mm')}}</a-descriptions-item>
        <a-descriptions-item label="运行时长" :span="2">{{info?.jvmMonitor.runningTime}}</a-descriptions-item>
        <a-descriptions-item label="运行参数" :span="3" :contentStyle="{width: '85%'}">{{info?.jvmMonitor.inputArguments}}</a-descriptions-item>
      </a-descriptions>
    </a-card>
    <a-card :style="{border: 'none'}">
      <a-descriptions title="系统内存" bordered>
        <a-descriptions-item label="内存大小">{{info?.memoryMonitor.total ? info?.memoryMonitor.total + 'GB' : ''}}</a-descriptions-item>
        <a-descriptions-item label="已使用">{{info?.memoryMonitor.used ? info?.memoryMonitor.used + 'GB' : ''}}</a-descriptions-item>
        <a-descriptions-item label="未使用">{{info?.memoryMonitor.available ? info?.memoryMonitor.available + 'GB' : ''}}</a-descriptions-item>
        <a-descriptions-item label="使用率">{{info?.memoryMonitor.usagePercentage ? info?.memoryMonitor.usagePercentage + '%' :''}}</a-descriptions-item>
      </a-descriptions>
    </a-card>
    <a-card :style="{border: 'none'}">
      <a-descriptions title="系统硬盘" bordered>
        <a-descriptions-item label="硬盘大小">{{info?.diskMonitor.total ? info?.diskMonitor.total + 'GB' : ''}}</a-descriptions-item>
        <a-descriptions-item label="已使用">{{info?.diskMonitor.used ? info?.diskMonitor.used + 'GB' : ''}}</a-descriptions-item>
        <a-descriptions-item label="未使用">{{info?.diskMonitor.free  ? info?.diskMonitor.free + 'GB' : ''}}</a-descriptions-item>
        <a-descriptions-item label="使用率">{{info?.diskMonitor.usagePercentage  ? info?.diskMonitor.usagePercentage + '%' : ''}}</a-descriptions-item>
      </a-descriptions>
    </a-card>
  </a-flex>
</template>

<script setup lang="ts">
import {message} from "ant-design-vue";
import type {ServerInfo} from "@/api/monitor/server/type/ServerInfo.ts";
import {onMounted, ref} from "vue";
import {serverInfo} from "@/api/monitor/server/Server.ts";
import dayjs from "dayjs";
import Spin from "@/components/spin";
import {ResponseError} from "@/api/global/Type.ts";
const info = ref<ServerInfo>()
// 初始化服务器信息
const init = async () => {
  const spinInstance = Spin.service({
    tip: '服务数据加载中...'
  });

 try {
   const resp = await serverInfo()

   if (resp.code === 200) {
     info.value = resp.data
   } else {
     message.error(resp.msg)
   }
 } catch (e) {
   if (e instanceof ResponseError) {
     message.error(e.msg)
   } else {
     console.error(e)
   }
 } finally {
   spinInstance.close()
 }
}

onMounted(() => {
  init()
})
</script>


<style scoped>

</style>
