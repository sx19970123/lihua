<template>
  <a-typography-title :level="4">多级表头无法使用宽度调整，并仅支持调整最上层位置</a-typography-title>
  <a-table
      :columns="columns"
      :data-source="data"
      bordered
      size="middle"
      :scroll="{ x: 'calc(700px + 50%)', y: 240 }"
  >
    <template #title>
      <a-flex>
        <table-setting v-model="columns" settingKey="2"/>
      </a-flex>
    </template>
  </a-table>
</template>
<script lang="ts" setup>
import type { TableColumnsType } from 'ant-design-vue';
import {ref} from "vue";
import TableSetting from "@/components/table-setting/index.vue";
type TableDataType = {
  key: number;
  name: string;
  age: number;
  street: string;
  building: string;
  number: number;
  companyAddress: string;
  companyName: string;
  gender: string;
};
const columns = ref<TableColumnsType> ([
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    width: 100,
    fixed: 'left',
    filters: [
      {
        text: 'Joe',
        value: 'Joe',
      },
      {
        text: 'John',
        value: 'John',
      },
    ]
  },
  {
    title: 'Other',
    key: 'other',
    children: [
      {
        title: 'Age',
        dataIndex: 'age',
        key: 'age',
        width: 200,
        sorter: (a: TableDataType, b: TableDataType) => a.age - b.age,
      },
      {
        title: 'Address',
        children: [
          {
            title: 'Street',
            dataIndex: 'street',
            key: 'street',
            width: 200,
          },
          {
            title: 'Block',
            children: [
              {
                title: 'Building',
                dataIndex: 'building',
                key: 'building',
                width: 100,
              },
              {
                title: 'Door No.',
                dataIndex: 'number',
                key: 'number',
                width: 100,
              },
            ],
          },
        ],
      },
    ],
  },
  {
    title: 'Company',
    key: 'company',
    children: [
      {
        title: 'Company Address',
        dataIndex: 'companyAddress',
        key: 'companyAddress',
        width: 200,
      },
      {
        title: 'Company Name',
        dataIndex: 'companyName',
        key: 'companyName',
      },
    ],
  },
  {
    title: 'Gender',
    dataIndex: 'gender',
    key: 'gender',
    width: 80,
    fixed: 'right',
  },
]);
const data = [...Array(100)].map((_, i) => ({
  key: i,
  name: 'John Brown',
  age: i + 1,
  street: 'Lake Park',
  building: 'C',
  number: 2035,
  companyAddress: 'Lake Street 42',
  companyName: 'SoftLake Co',
  gender: 'M',
}));
</script>

