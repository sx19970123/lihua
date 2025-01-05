<template>
  <div class="color-container">
    <a-tooltip title="亮色">
      <div class="color-block" style="background: #ffffff" @click="handleClickNavColor('light')">
        <CheckOutlined class="color-selected" :style="{color: themeStore.colorPrimary}" v-if="props.modelValue === 'light'"/>
      </div>
    </a-tooltip>
    <a-tooltip title="暗色">
      <div class="color-block" style="background: #021524" @click="handleClickNavColor('dark')">
        <CheckOutlined class="color-selected" style="color: #ffffff" v-if="props.modelValue === 'dark'"/>
      </div>
    </a-tooltip>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import {useThemeStore} from "@/stores/theme.ts";
// 接收全部颜色 items 和 双向绑定的颜色值 modelValue
const props = defineProps<{
  modelValue: string
}>();

// 使用 update:modelValue 定义 更新 v-model 方法
const emits = defineEmits(['update:modelValue','click'])

const themeStore = useThemeStore()

const handleClickNavColor = (key: string) => {
  emits('update:modelValue',key)
  emits('click',key)
}
</script>

<style scoped>
.color-container {
  display: flex;
  flex-wrap: nowrap;
}

.color-block {
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  height: 20px;
  width: 20px;
  border-radius: 8px;
  cursor: pointer;
  margin-right: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
}

.color-selected {
  color: #1677ff;
  font-weight: 700;
  font-size: 14px;
}
</style>
