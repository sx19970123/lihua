<template>
  <!-- mask 打开时背景蒙版 -->
  <Teleport to="body">
    <div class="lihua-mask" :style="{zIndex: zIndex}" v-if="showMask" @click="handleClickMask($event)"></div>
  </Teleport>
</template>

<script setup lang="ts">
import {onBeforeUnmount, watch} from "vue";
import {hiddenOverflowY, showOverflowY} from "@/utils/Scrollbar.ts";
// 控制遮罩开关
const {showMask, zIndex = 1000} = defineProps<{
  showMask: boolean,
  zIndex?: number
}>()
const emits = defineEmits(['click'])
// 遮罩点击事件
const handleClickMask = (event: KeyboardEvent | MouseEvent) => {
  emits('click', event, 'mask')
}

// 组件卸载时处理显示滚动条
onBeforeUnmount(() => {
  showOverflowY()
})

// 判断遮罩是否打开，打开时调用隐藏Y轴滚动条，关闭时显示滚动条
watch(() => showMask, (value) => {
  if (value) {
    hiddenOverflowY()
  } else {
    showOverflowY()
  }
})
</script>

<style scoped>
.lihua-mask {
  position: fixed;
  background: rgba(0, 0, 0, 0.45);
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}
</style>

<style>
[data-ground-glass = glass] {
  .lihua-mask {
    backdrop-filter: blur(6px);
  }
}
</style>
