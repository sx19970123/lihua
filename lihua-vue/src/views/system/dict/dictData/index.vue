<template>
  <a-flex vertical :gap="16">
    <a-card>
      <a-form :colon="false">
        <a-flex :gap="8" align="center" >
          <a-form-item class="form-item-single-line" label="标签">
            <a-input placeholder="请输入字典标签"/>
          </a-form-item>
          <a-form-item  class="form-item-single-line" label="值">
            <a-input placeholder="请输入字典值"/>
          </a-form-item>
          <a-form-item  class="form-item-single-line">
            <a-button type="primary">
              <template #icon>
                <SearchOutlined />
              </template>
              查 询
            </a-button>
          </a-form-item>
          <a-form-item  class="form-item-single-line">
            <a-button>
              <template #icon>
                <RedoOutlined />
              </template>
              重 置
            </a-button>
          </a-form-item>
        </a-flex>
      </a-form>
    </a-card>
    <a-table :columns="dictDataColumn">
      <template #title>
        <a-button type="primary" >
          <template #icon>
            <PlusOutlined />
          </template>
          新 增
        </a-button>
      </template>
    </a-table>
  </a-flex>
</template>

<script setup lang="ts">
// 接收父组件传入的typeId
import type {ColumnsType} from "ant-design-vue/es/table/interface";

const props = defineProps<{
  typeId: string
}>()

const initSearch = () => {
  // 定义表头
  const dictDataColumn: ColumnsType = [
    {
      title: '标签',
      dataIndex: 'name',
      key: 'name'
    },
    {
      title: '值',
      dataIndex: 'code',
      key: 'code'
    },
    {
      title: '备注',
      dataIndex: 'code',
      key: 'code'
    },
    {
      title: '操作',
      align: 'center',
      key: 'action',
      width: '196px'
    },
  ]

  return {
    dictDataColumn
  }
}

const {dictDataColumn} = initSearch()
</script>

<style scoped>

</style>
