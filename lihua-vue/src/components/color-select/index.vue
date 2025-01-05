<template>
  <a-flex>
    <template v-for="item in data.dataSource">
      <a-tooltip :title="item.name" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
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
  </a-flex>
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
const selectedColor = ({color, name, key}: ColorSelectItem) => {
  emits('update:color',color)
  emits('update:value',key)
  emits('click',{color, name, key})
};
</script>

<style scoped>
.color-block {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
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
