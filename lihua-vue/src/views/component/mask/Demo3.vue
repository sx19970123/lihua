<template>
  <div>
    <a-typography-title :level="4">自定义加载图标</a-typography-title>
    <a-button @click="openSpin" style="width: 120px">打开Spin</a-button>
  </div>
</template>

<script setup lang="ts">
import Spin from '@/components/spin';
import {LoadingOutlined} from "@ant-design/icons-vue";
import { h } from 'vue';
const indicator = h(LoadingOutlined, {
  style: {
    fontSize: '24px',
  },
  spin: true,
});
// 打开全屏加载并在1.5s后关闭
const openSpin = () => {
  const spin = Spin.service({
    tip: '1.5秒后关闭',
    indicator: indicator,
  })
  setTimeout(() => {
    spin.close()
  },1500)
}
</script>
