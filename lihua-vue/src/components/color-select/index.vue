<template>
  <div class="color-container">
    <template v-for="item in data.dataSource">
      <a-tooltip :title="item.name">
        <div class="color-block" :style="{ background: item.color }" @click="selectedColor(item)">
          <div v-if="data.color">
            <CheckOutlined class="color-selected" v-if="item.color === data.color"/>
          </div>
          <div v-else-if="data.value">
            <CheckOutlined class="color-selected" v-if="item.key === data.value"/>
          </div>
        </div>
      </a-tooltip>
    </template>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
type ColorSelectItem = { name: string, color: string, key?: string }
// 接收全部颜色 items 和 双向绑定的颜色值 modelValue
const data = defineProps<{
  dataSource: Array<ColorSelectItem>
  color?: string
  value?: string
}>();
// 使用 update:modelValue 定义 更新 v-model 方法
const emits = defineEmits(['update:color','update:value','click'])

// 点击对应颜色返回颜色值，赋值给v-model。执行 @click 方法
const selectedColor = ({color, key}: ColorSelectItem) => {
  emits('update:color',color)
  emits('update:value',key)
  emits('click',color,data.dataSource)
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
  box-shadow: var(--lihua-light-box-shadow);
}

.color-selected {
  color: #ffffff;
  font-weight: 700;
  font-size: 14px;
}
</style>
