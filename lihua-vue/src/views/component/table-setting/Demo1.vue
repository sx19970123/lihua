<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <a-table :columns="columns" :data-source="data">
    <template #title>
      <a-flex>
        <table-setting v-model="columns" settingKey="1"/>
      </a-flex>
    </template>
  </a-table>
</template>
<script lang="ts" setup>
import TableSetting from "@/components/table-setting/index.vue";
import {ref} from "vue";

const columns = ref([
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Chinese Score',
    dataIndex: 'chinese',
    key: 'chinese',
  },
  {
    title: 'Math Score',
    dataIndex: 'math',
    key: 'math',
  },
  {
    title: 'English Score',
    dataIndex: 'english',
    key: 'english',
  },
]);

const data = [
  {
    key: '1',
    name: 'John Brown',
    chinese: 98,
    math: 60,
    english: 70,
  },
  {
    key: '2',
    name: 'Jim Green',
    chinese: 98,
    math: 66,
    english: 89,
  },
  {
    key: '3',
    name: 'Joe Black',
    chinese: 98,
    math: 90,
    english: 70,
  },
  {
    key: '4',
    name: 'Jim Red',
    chinese: 88,
    math: 99,
    english: 89,
  },
];
</script>

