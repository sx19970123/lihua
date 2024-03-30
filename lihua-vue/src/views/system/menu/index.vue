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
                 v-model:expanded-row-keys="expandedRowKeys"
                 :scroll="{ y: 500 }"
        >
          <template #title>
            <a-flex :gap="8">
              <a-button type="primary" @click="handleOpenModal">
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
                  <PlusOutlined />
                </template>
                新增下级
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

    <a-modal v-model:open="openSaveModal">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">新增菜单</a-typography-title>
        </div>
      </template>
      <a-form :model="sysMenu" :colon="false" :label-col="{ span : 4 }">
        <a-form-item label="菜单类型">
          <a-radio-group v-model:value="sysMenu.menuType">
            <a-radio-button v-for="item in sys_menu_type" :value="item.value">{{item.label}}</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="上级目录" :wrapper-col="{span: 16}">
          <a-tree-select/>
        </a-form-item>
        <a-form-item label="菜单名称" :wrapper-col="{span: 16}">
          <a-input/>
        </a-form-item>
        <a-form-item label="路由地址" v-if="sysMenu.menuType === 'directory' || sysMenu.menuType === 'page'" :wrapper-col="{span: 16}">
          <a-input/>
        </a-form-item>
        <a-form-item label="组件路径" v-if="sysMenu.menuType === 'page'" :wrapper-col="{span: 16}">
          <a-input/>
        </a-form-item>
        <a-form-item label="链接地址" :wrapper-col="{span: 16}" v-if="sysMenu.menuType === 'link'">
          <a-textarea/>
        </a-form-item>
        <a-form-item label="权限标识" v-if="sysMenu.menuType === 'permi'" :wrapper-col="{span: 16}">
          <a-input/>
        </a-form-item>
        <a-form-item label="菜单图标" v-if="sysMenu.menuType !== 'permi'" :wrapper-col="{span: 16}">
          <a-popover placement="top" trigger="click" arrow-point-at-center>
            <template #content>
              <icon-select width="350px" size="small"/>
            </template>
            <a-button style="width: 90px">
              <template #icon>
                <component :is="sysMenu.icon" v-if="sysMenu.icon"/>
              </template>
              <a-typography v-if="!sysMenu.icon">icon</a-typography>
            </a-button>
          </a-popover>
        </a-form-item>
        <a-form-item label="显示排序">
          <a-input-number/>
        </a-form-item>
<!--        隐藏二级-->
        <a-button @click="moreSetting = !moreSetting" type="link">
          <span v-if="!moreSetting">
            更多<CaretDownOutlined/>
          </span>
          <span v-else>
            收起<CaretUpOutlined/>
          </span>
        </a-button>
        <div v-if="moreSetting">
          <a-form-item label="菜单描述" v-if="sysMenu.menuType === 'page' && false" :wrapper-col="{span: 17}">
            <a-input/>
          </a-form-item>
          <a-form-item label="路由参数" v-if="sysMenu.menuType === 'page'" :wrapper-col="{span: 17}">
            <a-input/>
          </a-form-item>
          <a-row>
            <a-col :span="12">
              <a-form-item label="菜单状态" :label-col="{span: 8}">
                <a-radio-group>
                  <a-radio>正常</a-radio>
                  <a-radio>停用</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="显示菜单" v-if="sysMenu.menuType !== 'permi'" :label-col="{span: 8}">
                <a-radio-group>
                  <a-radio>是</a-radio>
                  <a-radio>否</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row v-if="sysMenu.menuType === 'page'">
            <a-col :span="12">
              <a-form-item label="多任务栏" :label-col="{span: 8}">
                <a-radio-group>
                  <a-radio>是</a-radio>
                  <a-radio>否</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="是否缓存" :label-col="{span: 8}">
                <a-radio-group>
                  <a-radio>是</a-radio>
                  <a-radio>否</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-form-item label="备注" :wrapper-col="{span: 17}">
            <a-textarea/>
          </a-form-item>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">

// 列表查询相关
import type { ColumnsType } from 'ant-design-vue/es/table/interface';
import {findList} from "@/api/system/menu/menu.ts";
import {reactive, ref} from "vue";
import {initDict} from "@/utils/dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
import IconSelect from "@/components/icon-select/index.vue"
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
      width: '300px'
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
  const sysMenu = reactive<SysMenu>({
    menuType: 'directory'
  })
  const moreSetting = ref<boolean>(false)
  // 模态框展开关闭
  const openSaveModal = ref<boolean>(false)
  // 控制模态框展开关闭
  const handleOpenModal = (menuId?: string) => {
    openSaveModal.value = true
  }
  return {
    openSaveModal,
    moreSetting,
    handleOpenModal,
    sysMenu
  }
}
const {openSaveModal,sysMenu,moreSetting,handleOpenModal} = initSave()
// 数据删除相关
const initDelete = () => {

}
</script>

<style scoped>

</style>
