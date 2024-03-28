<template>
  <div>
    <a-flex vertical :gap="16">
<!--      检索条件-->
      <a-card>
        <a-form :colon="false">
          <a-flex :gap="8" align="center">
            <a-form-item class="form-item-single-line" label="菜单名称">
              <a-input placeholder="请输入菜单名称" allowClear/>
            </a-form-item>
            <a-form-item class="form-item-single-line" label="菜单状态">
              <a-input placeholder="请输入菜单名称" allowClear/>
            </a-form-item>
            <a-form-item class="form-item-single-line">
              <a-button type="primary">
                <template #icon>
                  <SearchOutlined />
                </template>
                查 询
              </a-button>
            </a-form-item>
            <a-form-item class="form-item-single-line">
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
<!--      列表-->
      <a-card :body-style="{padding: 0}">
        <a-table :columns="menuColumn">
          <template #title>
            <a-flex :gap="8">
              <a-button type="primary">
                <template #icon>
                  <PlusOutlined />
                </template>
                新 增
              </a-button>
              <a-button type="primary">
                <template #icon>
                  <Unfold/>
                </template>
                展 开
              </a-button>
              <a-button type="primary">
                <template #icon>
                  <PickUp/>
                </template>
                折 叠
              </a-button>
              <a-button danger>
                <template #icon>
                  <DeleteOutlined />
                </template>
                删 除
              </a-button>
            </a-flex>

          </template>
        </a-table>
      </a-card>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

// 列表查询相关
import type { ColumnsType } from 'ant-design-vue/es/table/interface';

const initSearch = () => {
  const menuColumn:ColumnsType = [
    {
      title: '菜单名称',
    },
    {
      title: '图标',
    },
    {
      title: '权限标识',
    },
    {
      title: '组件路径',
    },
    {
      title: '路由地址',
    },
    {
      title: '类型',
    },
    {
      title: '状态',
    },
    {
      title: '操作',
    }
  ]

  return {
    menuColumn
  }
}
const { menuColumn } = initSearch()

// 数据保存相关
const initSave = () => {

}
// 数据删除相关
const initDelete = () => {

}
</script>

<style scoped>

</style>
