<template>
  <!--            表格设置-->
  <a-typography-title :level="4">table-setting组件放置a-table组件外，无法使用宽度调整</a-typography-title>
  <table-setting v-model="columns" settingKey="3"/>
  <a-table :dataSource="dataSource" :columns="columns" />
</template>
<script setup lang="ts">
import TableSetting from "@/components/table-setting/index.vue";
import {ref} from "vue";
import type { ColumnsType } from 'ant-design-vue/es/table/interface';

const columns = ref<ColumnsType>([
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '年龄',
    dataIndex: 'age',
    key: 'age',
  },
  {
    title: '住址',
    dataIndex: 'address',
    key: 'address',
  },
])

const dataSource = [
  {
    key: '1',
    name: '胡彦斌',
    age: 32,
    address: '西湖区湖底公园1号',
  },
  {
    key: '2',
    name: '胡彦祖',
    age: 42,
    address: '西湖区湖底公园1号',
  },
]
</script>