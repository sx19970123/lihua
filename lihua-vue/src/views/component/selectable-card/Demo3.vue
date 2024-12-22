<template>
  <a-typography-title :level="4">加载遮罩及空数据</a-typography-title>
  <a-button @click="handleLoading" style="width: 100px">加载数据</a-button>
  <a-typography strong>选中的值：{{value}}</a-typography>
  <a-row>
    <a-col :span="6">
      <selectable-card :data-source="dataSource" item-key="key" multiple v-model="value" :loading="dataLoading">
        <template #content="{ item }">
          {{item.label}}
        </template>
      </selectable-card>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import SelectableCard from "@/components/selectable-card/index.vue"
import {ref} from "vue";
const dataSource = ref<any[]>([])
const dataLoading = ref<boolean>(false)
const value = ref<string[]>([])
const handleLoading = () => {
  dataLoading.value = true
  setTimeout(() => {
    dataSource.value = [{
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
    dataLoading.value = false
  },1500)
}
</script>
