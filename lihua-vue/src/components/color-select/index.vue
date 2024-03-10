<template>
  <div class="color-container">
    <template v-for="item in data.items">
      <a-tooltip :title="item.name">
        <div class="color-block" :style="{ background: item.color }" @click="selectedColor(item)">
          <CheckOutlined class="color-selected" v-if="item.color === data.modelValue"/>
        </div>
      </a-tooltip>
    </template>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";

// 接收全部颜色 items 和 双向绑定的颜色值 modelValue
const data = defineProps<{
  items: Array<{ name: string, color: string}>
  modelValue: string
}>();

// 使用 update:modelValue 定义 更新 v-model 方法
const emits = defineEmits(['update:modelValue','click'])

// 点击对应颜色返回颜色值，赋值给v-model。执行 @click 方法
const selectedColor = ({ color }: { color: string}) => {
  emits('update:modelValue',color)
  emits('click',color,data.items)
};
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
  color: #ffffff;
  font-weight: 700;
  font-size: 14px;
}
</style>
