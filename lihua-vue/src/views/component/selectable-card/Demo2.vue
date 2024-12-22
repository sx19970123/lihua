<template>
  <a-typography-title :level="4">可多选</a-typography-title>
  <a-typography strong>选中的值：{{value}}</a-typography>
  <selectable-card :data-source="dataSource" item-key="key" multiple v-model="value">
    <template #content="{ item }">
      {{item.label}}
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
const value = ref<string[]>([])
</script>
