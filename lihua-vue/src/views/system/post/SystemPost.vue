<template>
  <div>
    <a-flex :gap="16" v-hasRole="['ROLE_admin']">
<!--      部门树-->
      <div style="width: 20%">
        <a-card>
          <a-space size="small" direction="vertical">
            <a-input-search placeholder="请输入部门名称"/>
            <a-tree :tree-data="deptTree"
                    :fieldNames="{children:'children', title:'name',key: 'id'}"/>
          </a-space>
        </a-card>
      </div>
      <div style="width: 80%">
        <a-flex :gap="16" vertical>
          <!--        检索条件-->
          <a-card :style="{border: 'none'}">
            <a-form :colon="false">
              <a-space size="small">
                <a-form-item label="岗位名称" class="form-item-single-line">
                  <a-input placeholder="请输入岗位名称"/>
                </a-form-item>
                <a-form-item label="岗位编码" class="form-item-single-line">
                  <a-input placeholder="请输入岗位编码"/>
                </a-form-item>
                <a-form-item label="岗位状态" class="form-item-single-line">
                  <a-select placeholder="请选择">

                  </a-select>
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
              </a-space>
            </a-form>
          </a-card>
          <!--        树型结构-->
          <a-table :columns="postColumn">

          </a-table>
        </a-flex>
      </div>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

import {deptOption} from "@/api/system/dept/dept.ts";
import {ref} from "vue";
import type {ColumnsType} from "ant-design-vue/es/table/interface";

// 初始化部门树
const initDept = () => {
  const deptTree = ref<Array<SysDept>>([])
  const initDeptTree = async () => {
    const resp = await deptOption()
    if (resp.code === 200 ) {
      deptTree.value = resp.data
    }
  }
  initDeptTree()

  return {
    deptTree
  }
}
const {deptTree} = initDept()

// 查询列表
const initSearch = () => {
  const postColumn: ColumnsType = [
    {
      title: '岗位名称',
      key: 'name',
      dataIndex: 'name'
    },
    {
      title: '岗位编码',
      key: 'code',
      dataIndex: 'code'
    },
    {
      title: '排序',
      key: 'sort',
      dataIndex: 'sort',
      align: 'right',
      width: 100
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center',
      width: 100
    },
    {
      title: '负责人',
      key: 'manager',
      dataIndex: 'manager',
      align: 'center',
      width: 120
    },
    {
      title: '所属部门',
      key: 'dept',
      dataIndex: 'dept',
      ellipsis: true,
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '300px'
    }
  ]

  return {
    postColumn
  }
}
const { postColumn } = initSearch()


// 保存岗位

// 删除岗位
</script>

<style scoped>

</style>