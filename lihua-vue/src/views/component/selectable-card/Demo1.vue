<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <a-typography strong>选中的值：{{value}}</a-typography>
  <selectable-card :data-source="dataSource" item-key="key" v-model="value">
    <template #content="{dataSource, item, isSelected, color, index}">
      <a-typography-title :level="5" :style="{color: isSelected ? color : ''}">
        content插槽，可获取到dataSource、item、isSelected、color、index信息
      </a-typography-title>
      <div>
        <a-typography-text strong :style="{color: isSelected ? color : ''}">item</a-typography-text> {{item}}
      </div>
      <div>
        <a-typography-text strong :style="{color: isSelected ? color : ''}">是否选中</a-typography-text> {{isSelected}}
      </div>
      <div>
        <a-typography-text strong :style="{color: isSelected ? color : ''}">索引</a-typography-text> {{index}}
      </div>
    </template>
  </selectable-card>
</template>

<script setup lang="ts">
import SelectableCard from "@/components/selectable-card/index.vue"
import {ref} from "vue";
const dataSource = [{
  key: '1',
  label: '选项1'
},{
  key: '2',
  label: '选项2'
},{
  key: '3',
  label: '选项3'
},{
  key: '4',
  label: '选项4'
}]
const value = ref<string>()
</script>
