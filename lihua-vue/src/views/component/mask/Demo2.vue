<template>
  <div>
    <a-typography-title :level="4">基于指令的全屏spin</a-typography-title>
    <a-button @click="openSpin" style="width: 120px">打开Spin</a-button>
  </div>
</template>

<script setup lang="ts">
import Spin from '@/components/spin';
// 打开全屏加载并在1.5s后关闭
const openSpin = () => {
  const spin = Spin.service({
    tip: '1.5秒后关闭',
  })
  setTimeout(() => {
    spin.close()
  },1500)
}
</script>
