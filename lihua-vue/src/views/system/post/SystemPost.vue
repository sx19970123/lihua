<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--        检索条件-->
      <a-card :style="{border: 'none'}">
        <a-form :colon="false">
          <a-space size="small">
            <a-form-item label="所属部门" class="form-item-single-line">
              <a-tree-select :tree-data="deptTree"
                          :fieldNames="{children:'children', label:'name', value: 'id' }"
                          style="width: 196px"
                          placeholder="请选择部门"
                          allow-clear/>
            </a-form-item>
            <a-form-item label="岗位名称" class="form-item-single-line">
              <a-input placeholder="请输入岗位名称" allow-clear/>
            </a-form-item>
            <a-form-item label="岗位编码" class="form-item-single-line">
              <a-input placeholder="请输入岗位编码" allow-clear/>
            </a-form-item>
            <a-form-item label="岗位状态" class="form-item-single-line">
              <a-select placeholder="请选择" allow-clear>
                <a-select-option :value="item.value" v-for="item in sys_status">{{item.label}}</a-select-option>
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
      <!--        列表-->
      <a-table :columns="postColumn" :data-source="postList">

      </a-table>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

import {deptOption} from "@/api/system/dept/dept.ts";
import {ref} from "vue";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {initDict} from "@/utils/dict.ts";
import {findPage} from "@/api/system/post/post.ts";

const {sys_status} = initDict("sys_status")


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
      key: 'deptName',
      dataIndex: 'deptName',
      ellipsis: true,
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '300px'
    }
  ]

  const postQuery = ref<SysPostDTO>({
    pageNum: 0,
    pageSize: 10,
  })
  const total = ref<number>()
  const postList = ref<Array<SysPost>>()

  const initList = async () => {
    const resp = await findPage(postQuery.value)
    if (resp.code === 200) {
      postList.value = resp.data.records
      total.value = resp.data.total
    }
  }
  initList()
  return {
    postColumn,
    postList,
    total
  }
}
const { postColumn,postList,total } = initSearch()


// 保存岗位

// 删除岗位
</script>

<style scoped>

</style>