<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--        检索条件-->
      <a-card :style="{border: 'none'}">
        <a-form :colon="false">
          <a-space size="small">
            <a-form-item label="所属部门" class="form-item-single-line">
              <a-tree-select
                  :tree-data="deptTree"
                  :fieldNames="{children:'children', label:'name', value: 'id' }"

                  placeholder="请选择部门"
                  allow-clear
                  v-model:value="postQuery.deptId"
              />
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
      <a-table :columns="postColumn" :data-source="postList" :pagination="false">
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
          </a-flex>
        </template>

        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'status'">
            <dict-tag  :dict-data-option="sys_status" :dict-data-value="text"/>
          </template>

          <template v-if="column.key === 'action'">
            <a-button type="link" size="small">
              <template #icon>
                <EditOutlined />
              </template>
              编辑
            </a-button>
            <a-divider type="vertical"/>
            <a-popconfirm ok-text="确 定"
                          cancel-text="取 消"
                          placement="bottomRight"
            >
              <template #title>
                数据删除后不可恢复，是否删除？
              </template>
              <a-button type="link" danger size="small">
                <template #icon>
                  <DeleteOutlined />
                </template>
                删除
              </a-button>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

import {deptOption} from "@/api/system/dept/dept.ts";
import {ref, watch} from "vue";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {initDict} from "@/utils/dict.ts";
import {findPage} from "@/api/system/post/post.ts";
import {useRoute} from "vue-router";
import DictTag from "@/components/dict-tag/index.vue";
const {sys_status} = initDict("sys_status")
const route = useRoute();

// 监听传入deptId变化进行部门赋值
watch(() => route.query.deptId, (value) => {
  postQuery.value.deptId = value as string | undefined;
  initList()
})


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
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center',
    },
    {
      title: '负责人',
      key: 'manager',
      dataIndex: 'manager',
      align: 'center',
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
    deptId: route.query.deptId as string | undefined,
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
    postQuery,
    initList,
    total
  }
}
const { postColumn,postList,postQuery,initList,total } = initSearch()


// 保存岗位

// 删除岗位
</script>

<style scoped>

</style>