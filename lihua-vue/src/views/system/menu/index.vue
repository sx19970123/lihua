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
        <a-table :columns="menuColumn"
                 :data-source="menuList"
                 :pagination="false"
                 row-key="id"
                 v-model:expanded-row-keys="expandedRowKeys">
          <template #title>
            <a-flex :gap="8">
              <a-button type="primary">
                <template #icon>
                  <PlusOutlined />
                </template>
                新 增
              </a-button>
              <a-button type="primary" @click="handleExpanded">
                  <template #icon>
                    <Unfold v-if="expandedRowKeys.length === 0"/>
                    <PickUp v-else/>
                  </template>
                  {{!expandedRowKeys.length ? '展 开' : '折 叠'}}
              </a-button>
              <a-button danger>
                <template #icon>
                  <DeleteOutlined />
                </template>
                删 除
              </a-button>
            </a-flex>
          </template>
          <template #bodyCell = "{column,text,record}">
<!--            图标-->
            <template v-if="column.key === 'icon'">
              <component :is="text"/>
            </template>
<!--            类型-->
            <template v-if="column.key === 'menuType'">
              <dict-tag :dict-data-value="text" :dict-data-option="sys_menu_type"/>
            </template>
<!--            状态-->
            <template v-if="column.key === 'status'">
              <dict-tag :dict-data-value="text" :dict-data-option="sys_status"/>
            </template>
<!--            操作-->
            <template v-if="column.key === 'action'">
              <a-button type="link" size="small">
                <template #icon>
                  <EditOutlined />
                </template>
                编辑
              </a-button>
              <a-divider type="vertical"/>
              <a-button type="link" size="small">
                <template #icon>
                  <EnterOutlined />
                </template>
                添加下级
              </a-button>
              <a-divider type="vertical"/>
              <a-popconfirm title="删除后不可恢复，是否删除？"
                            ok-text="确 定"
                            cancel-text="取 消"
                            placement="bottomRight"
              >
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
      </a-card>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

// 列表查询相关
import type { ColumnsType } from 'ant-design-vue/es/table/interface';
import {findList} from "@/api/system/menu/menu.ts";
import {ref} from "vue";
import {initDict} from "@/utils/dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
import {flattenTreeData} from "@/utils/tree.ts"
const  {sys_menu_type,sys_status} = initDict("sys_menu_type","sys_status")
const initSearch = () => {
  // 列表列集合
  const menuColumn:ColumnsType = [
    {
      title: '菜单名称',
      key: 'label',
      dataIndex: 'label',
      ellipsis: true
    },
    {
      title: '图标',
      key: 'icon',
      align: 'center',
      dataIndex: 'icon',
      width: '80px'
    },
    {
      title: '权限标识',
      key: 'perms',
      dataIndex: 'perms',
      ellipsis: true
    },
    {
      title: '组件路径',
      key: 'componentPath',
      dataIndex: 'componentPath',
      ellipsis: true
    },
    {
      title: '路由地址',
      key: 'routerPath',
      dataIndex: 'routerPath',
      ellipsis: true
    },
    {
      title: '类型',
      key: 'menuType',
      dataIndex: 'menuType',
      width: '100px'
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      width: '100px'
    },
    {
      title: '操作',
      align: 'center',
      key: 'action',
      width: '294px'
    }
  ]
  // 筛选条件
  const menuQuery = ref<SysMenu>({})
  // 列表元素
  const menuList = ref<Array<SysMenu>>([])
  // 默认展开的行
  const expandedRowKeys = ref<Array<string>>([])
  // 查询列表
  const initList = async () => {
    const resp = await findList(menuQuery.value)
    menuList.value = resp.data
  }
  // 展开折叠
  const handleExpanded = () => {
    if (expandedRowKeys.value.length == 0) {
      const data:Array<SysMenu> = ([])
      flattenTreeData(menuList.value,data)
      expandedRowKeys.value = data.filter(item => item.id).map(item => item.id as string)
    } else {
      expandedRowKeys.value = []
    }
  }
  initList()

  return {
    menuColumn,
    menuQuery,
    menuList,
    expandedRowKeys,
    handleExpanded,
  }
}


const { menuColumn,menuQuery,menuList,expandedRowKeys,handleExpanded } = initSearch()
// 数据保存相关
const initSave = () => {

}
// 数据删除相关
const initDelete = () => {

}
</script>

<style scoped>

</style>
