<template>
  <div>
    <a-flex vertical :gap="16">
      <!--      检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false" :model="menuQuery">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="菜单名称" name="label">
                <a-input placeholder="请输入菜单名称" v-model:value="menuQuery.label" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="类型" name="menuType">
                <a-select placeholder="请选择" v-model:value="menuQuery.menuType" allow-clear>
                  <a-select-option v-for="item in sys_menu_type" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="状态" name="status">
                <a-select placeholder="请选择" v-model:value="menuQuery.status" allow-clear>
                  <a-select-option v-for="item in sys_status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item>
                <a-space size="small">
                  <a-button type="primary" @click="initList" :loading="tableLoad">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button @click="resetList" :loading="tableLoad">
                    <template #icon>
                      <RedoOutlined />
                    </template>
                    重 置
                  </a-button>
                </a-space>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>
      <!--      列表-->
      <a-table
          :columns="menuColumn"
          :data-source="menuList"
          :pagination="false"
          row-key="id"
          v-model:expanded-row-keys="expandedRowKeys"
          :loading="tableLoad"
          :row-selection="menuRowSelectionType"
          row-class-name="hover-cursor-pointer"
          :custom-row="handleRowClick"
          :scroll="{x: 1500}"
        >
          <template #title>
            <a-flex :gap="8">
              <a-button type="primary" @click="addMenu">
                <template #icon>
                  <PlusOutlined />
                </template>
                新 增
              </a-button>

              <a-popconfirm :open="openDeletePopconfirm"
                            ok-text="确 定"
                            cancel-text="取 消"
                            @confirm="handleDelete(undefined)"
                            @cancel="closePopconfirm"
                            @open-change="(open: boolean) => !open ? closePopconfirm(): ''"
              >
                <template #title>
                  删除后对应角色解绑，不可恢复。<br/>是否继续？
                </template>
                <a-button danger @click="openPopconfirm">
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  删 除
                  <span v-if="selectedIds && selectedIds.length > 0" style="margin-left: 4px"> {{selectedIds.length}} 项</span>
                </a-button>
              </a-popconfirm>

              <a-button type="primary" ghost @click="handleExpanded">
                <template #icon>
                  <Unfold v-if="expandedRowKeys.length === 0"/>
                  <PickUp v-else/>
                </template>
                {{!expandedRowKeys.length ? '展 开' : '折 叠'}}
              </a-button>

              <!--            表格设置-->
              <table-setting v-model="menuColumn"/>
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
              <a-switch v-model:checked="record.statusIsNormal"
                        @change="(checked: boolean | string | number, event: MouseEvent) => handleUpdateStatus(event, record ,text)"
                        @click="(checked: boolean | string | number, event: MouseEvent) => { event.stopPropagation(); record.updateStatusLoading = true }"
                        :loading="record.updateStatusLoading">
                <template #checkedChildren>
                  {{sys_status.filter(item => item.value === text)[0]?.label}}
                </template>
                <template #unCheckedChildren>
                  {{sys_status.filter(item => item.value === text)[0]?.label}}
                </template>
              </a-switch>
            </template>
            <!--            操作-->
            <template v-if="column.key === 'action'">
              <a-button type="link" size="small" @click="(event:MouseEvent) => getMenu(event, record.id)">
                <template #icon>
                  <EditOutlined />
                </template>
                编辑
              </a-button>
              <a-divider type="vertical"/>
              <a-button type="link"
                        size="small"
                        @click="(event: MouseEvent) => addChildren(event, record)"
                        :disabled="record.menuType === 'link' || record.menuType === 'perms'"
              >
                <template #icon>
                  <PlusOutlined />
                </template>
                新增下级
              </a-button>
              <a-divider type="vertical"/>
              <a-popconfirm ok-text="确 定"
                            cancel-text="取 消"
                            placement="bottomRight"
                            @confirm="handleDelete(record.id)"
              >
                <template #title>
                  删除后对应角色解绑，不可恢复。<br/>是否继续？
                </template>
                <a-button type="link" danger size="small" @click="(event:MouseEvent) => event.stopPropagation()">
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
    <!--    模态框-->
    <a-modal v-model:open="modalActive.open"
             ok-text="保 存"
             cancel-text="关 闭"
             :confirm-loading="modalActive.saveLoading"
             @ok="saveMenu"
    >
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
      <a-form :model="sysMenu"
              :colon="false"
              :label-col="{ span : 4 }"
              :rules="menuRules"
              ref="formRef"
      >
        <transition :name="themeStore.routeTransition" mode="out-in">
          <a-alert message="组件路径中组件名称请保证系统内唯一，推荐根据目录大写驼峰命名"
                   v-if="sysMenu.menuType === 'page'"
                   type="warning"
                   closable
                   style="margin-bottom: 24px"
          />
        </transition>
        <a-form-item label="菜单类型" name="menuType">
          <a-radio-group v-model:value="sysMenu.menuType">
            <a-radio-button v-for="item in sys_menu_type" :value="item.value">{{item.label}}</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="上级目录" :wrapper-col="{span: 16}" name="parentId">
          <a-tree-select :tree-data="parentMenuTree"
                         :fieldNames="{children:'children', label:'label',value: 'id'}"
                         tree-node-filter-prop="label"
                         v-model:value="sysMenu.parentId"
                         show-search
          >
            <template #title="{ value: val, label, menuType }">
              {{label}}
              <dict-tag :dict-data-value="menuType" :dict-data-option="sys_menu_type" :bordered="false"/>
            </template>
          </a-tree-select>
        </a-form-item>
        <a-form-item label="菜单名称" :wrapper-col="{span: 16}" name="label">
          <a-input v-model:value="sysMenu.label" placeholder="请输入菜单名称" :maxlength="30" allowClear show-count/>
        </a-form-item>
        <a-form-item label="路由地址" name="routerPath" v-if="sysMenu.menuType !== 'perms'" :wrapper-col="{span: 16}">
          <a-input v-model:value="sysMenu.routerPath" placeholder="浏览器地址栏访问的路由地址" :maxlength="100" allowClear show-count/>
        </a-form-item>
        <a-form-item label="组件路径" name="componentPath" v-if="sysMenu.menuType === 'page'" :wrapper-col="{span: 16}">
          <a-input v-model:value="sysMenu.componentPath" placeholder="views下路径地址以 .vue结尾" :maxlength="100" allowClear show-count/>
        </a-form-item>
        <a-form-item label="链接地址" name="linkPath" :wrapper-col="{span: 16}" v-if="sysMenu.menuType === 'link'">
          <a-textarea v-model:value="sysMenu.linkPath" placeholder="外链地址需要以http(s)://开头" :maxlength="300" allowClear show-count/>
        </a-form-item>
        <a-form-item label="权限标识" name="perms" v-if="sysMenu.menuType === 'perms'" :wrapper-col="{span: 16}">
          <a-input v-model:value="sysMenu.perms" :maxlength="50" allowClear placeholder="controller中定义的权限标识" show-count/>
        </a-form-item>
        <a-form-item label="显示排序" name="sort">
          <a-input-number v-model:value="sysMenu.sort" style="width: 140px;" placeholder="升序排列"/>
        </a-form-item>
        <a-form-item label="菜单图标" name="icon" v-if="sysMenu.menuType !== 'perms'" :wrapper-col="{span: 16}">
            <a-flex :gap="6">
              <a-tooltip placement="bottom">
                <template #title v-if="sysMenu.icon">
                  {{sysMenu.icon}}
                </template>
                <a-input style="max-width: 140px;" readonly :value="sysMenu.icon" placeholder="选择图标">
                  <template #prefix>
                    <component :is="sysMenu.icon" v-if="sysMenu.icon"/>
                  </template>
                </a-input>
              </a-tooltip>
              <a-popover trigger="click">
                <template #content>
                  <icon-select width="416px"
                               max-height="228px"
                               size="small"
                               v-model="sysMenu.icon"
                  />
                </template>
              <a-button>
                <template #icon>
                  <SearchOutlined />
                </template>
              </a-button>
              </a-popover>
            </a-flex>
        </a-form-item>
        <a-form-item label="打开方式" name="linkOpenType" v-if="sysMenu.menuType === 'link'">
          <a-radio-group v-model:value="sysMenu.linkOpenType">
            <a-radio v-for="item in sys_link_menu_open_type" :value="item.value">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <!--        隐藏二级-->
        <a-button @click="modalActive.moreSetting = !modalActive.moreSetting" type="link">
          <span v-if="!modalActive.moreSetting">
            更多<CaretDownOutlined/>
          </span>
          <span v-else>
            收起<CaretUpOutlined/>
          </span>
        </a-button>
        <div v-if="modalActive.moreSetting">
          <a-form-item label="菜单描述" name="title" v-if="sysMenu.menuType === 'page' && false" :wrapper-col="{span: 17}">
            <a-input v-model:value="sysMenu.title"/>
          </a-form-item>
          <a-row>
            <a-col :span="12">
              <a-form-item label="菜单状态" name="status" :label-col="{span: 8}">
                <a-radio-group v-model:value="sysMenu.status">
                  <a-radio v-for="item in sys_status" :value="item.value">{{item.label}}</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="显示菜单" name="visible" v-if="sysMenu.menuType !== 'perms'" :label-col="{span: 8}">
                <a-radio-group v-model:value="sysMenu.visible">
                  <a-radio v-for="item in sys_whether" :value="item.value">{{item.label}}</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="多任务栏" name="skip" :label-col="{span: 8}"
                           v-if="sysMenu.menuType === 'page' || (sysMenu.menuType === 'link' && sysMenu.linkOpenType === 'inner')">
                <a-radio-group v-model:value="sysMenu.viewTab">
                  <a-radio v-for="item in sys_whether" :value="item.value">{{item.label}}</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="是否缓存" name="cache" :label-col="{span: 8}"  v-if="sysMenu.menuType === 'page'">
                <a-radio-group v-model:value="sysMenu.cache">
                  <a-radio v-for="item in sys_whether" :value="item.value">{{item.label}}</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-form-item label="路由参数" name="query" v-if="sysMenu.menuType === 'page'" :wrapper-col="{span: 17}">
            <a-textarea placeholder="访问路由的默认参数，格式为json字符串" v-model:value="sysMenu.query" :rows="1" :maxlength="100" allowClear show-count/>
          </a-form-item>
          <a-form-item label="备注" name="remark" :wrapper-col="{span: 17}">
            <a-textarea :maxlength="500" :rows="2" v-model:value="sysMenu.remark" allowClear placeholder="请输入备注信息" show-count/>
          </a-form-item>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">

// 列表查询相关
import type { ColumnsType } from 'ant-design-vue/es/table/interface';
import {deleteData, queryById, queryList, queryMenuTreeOption, save, updateStatus} from "@/api/system/menu/Menu.ts";
import {reactive, ref, useTemplateRef} from "vue";
import {initDict} from "@/utils/Dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
import IconSelect from "@/components/icon-select/index.vue"
import {flattenTree} from "@/utils/Tree.ts"
import type {Rule} from "ant-design-vue/es/form";
import {type FormInstance, message} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';
import { useThemeStore } from "@/stores/theme";
import type {SysMenu, SysMenuVO} from "@/api/system/menu/type/SysMenu.ts";
import PickUp from "@/components/icon/pick-up/PickUp.vue";
import Unfold from "@/components/icon/unfold/Unfold.vue";
import {ResponseError} from "@/api/global/Type.ts";
import settings from "@/settings.ts";
import TableSetting from "@/components/table-setting/index.vue";
const themeStore = useThemeStore()
const  {sys_menu_type,sys_status,sys_link_menu_open_type,sys_whether} = initDict("sys_menu_type","sys_status","sys_link_menu_open_type","sys_whether")
const initSearch = () => {

  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const menuRowSelectionType = {
    checkStrictly: true,
    columnWidth: '55px',
    type: 'checkbox',
    // 支持跨页勾选
    preserveSelectedRowKeys: true,
    // 指定选中id的数据集合，操作完后可手动清空
    selectedRowKeys: selectedIds,
    onChange: (ids: Array<string>) => {
      selectedIds.value = ids
    }
  }
  const handleRowClick = (record:SysMenuVO) => {
    return {
      onClick: () => {
        if (record.id) {
          const selected = selectedIds.value
          if (selected.includes(record.id)) {
            selected.splice(selected.indexOf(record.id),1)
          } else {
            selected.push(record.id)
          }
        }
      }
    }
  }

  // 列表列集合
  const menuColumn = ref<ColumnsType>([
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
      title: '排序',
      key: 'sort',
      dataIndex: 'sort',
      width: '80px',
      align: 'right'
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
      title: '备注',
      key: 'remark',
      dataIndex: 'remark',
      ellipsis: true
    },
    {
      title: '类型',
      key: 'menuType',
      dataIndex: 'menuType',
      align: 'center',
      width: '100px'
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center',
      width: '100px'
    },
    {
      title: '操作',
      align: 'center',
      key: 'action',
      width: '292px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    }
  ])
  // 筛选条件
  const menuQuery = ref<SysMenu>({})
  // 列表元素
  const menuList = ref<Array<SysMenuVO>>([])
  const parentMenuTree = ref<Array<SysMenuVO>>([])
  // 默认展开的行
  const expandedRowKeys = ref<Array<string>>([])
  // 列表加载中loading
  const tableLoad = ref<boolean>(false)
  // 查询列表
  const initList = async () => {
    tableLoad.value = true
    try {
      const resp = await queryList(menuQuery.value)
      if (resp.code === 200) {
        // 列表数据
        menuList.value = resp.data
        // 回显状态
        handleMenuStatus(menuList.value)
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      tableLoad.value = false
    }
  }

  // 处理回显菜单状态
  const handleMenuStatus = (menuList: Array<SysMenuVO>) => {
    menuList.forEach(menu => {
      menu.statusIsNormal = menu.status === '0'
      menu.updateStatusLoading = false

      if (menu.children && menu.children.length > 0) {
        handleMenuStatus(menu.children)
      }
    })
  }

  // 重置表单
  const resetList = async () => {
    menuQuery.value = {}
    await initList()
  }

  // 展开折叠
  const handleExpanded = () => {
    if (expandedRowKeys.value.length == 0) {
      const data = flattenTree(menuList.value)
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
    parentMenuTree,
    tableLoad,
    selectedIds,
    menuRowSelectionType,
    handleRowClick,
    handleExpanded,
    initList,
    resetList
  }
}


const { menuColumn,menuQuery,menuList,expandedRowKeys,parentMenuTree,tableLoad,selectedIds,menuRowSelectionType,handleRowClick,handleExpanded,initList,resetList } = initSearch()
// 数据保存相关
const initSave = () => {
  // 保存的menu对象
  const sysMenu = ref<SysMenu>({})
  // 表单校验
  const menuRules: Record<string, Rule[]> = {
    label: [
      { required: true, message: '请输入菜单名称',trigger: 'change' }
    ],
    routerPath: [
      { required: true, message: '请输入路由地址',trigger: 'change' },
      { pattern: /^\/(?!.*\/{2})[A-Za-z0-9_/-]*$/, message: '请输入正确的路由地址，需以/开头',trigger: 'change' }
    ],
    sort: [
      { required: true, message: '请输入显示顺序',trigger: 'change' }
    ],
    componentPath: [
      { required: true, message: '请输入组件路径',trigger: 'change' },
      { pattern: /^\/[A-Za-z0-9-]+(?:[./][A-Za-z0-9-]+)*\.vue$/,message: '请输入正确的组件路径，需以/开头',trigger: 'change' }
    ],
    linkPath: [
      { required: true, message: '请输入链接地址',trigger: 'change' },
      { pattern: /^(https?|ftp):\/\/[^\s\/$.?#].[^\s]*$/, message: '请输入正确的链接地址',trigger: 'change' }
    ],
    perms: [
      { required: true, message: '请输入权限标识',trigger: 'change' },
      { pattern: /^[A-Za-z0-9:]*$/, message: '请输入正确的权限标识',trigger: 'change' }
    ],
    query: [
      { validator: (rule: any,value: string,callback:Function) => {
          try {
            if (value) {
              JSON.parse(value)
            }
          } catch (e) {
            callback(new Error("请输入正确的json数据"))
          }
          callback()
        } ,trigger: 'blur'
      }
    ]
  }

  // modal 相关属性定义
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string, // 模态框标题
    moreSetting: boolean, // 表单更多设置显示隐藏开关
  }
  const modalActive = reactive<modalActiveType>( {
    open: false,
    saveLoading: false,
    title: '',
    moreSetting: false,
  })

  // 表单
  const formRef = useTemplateRef<FormInstance>("formRef")

  // 打开模态框（打开modal时会重制表单，应在方法最开始调用）
  const handleModelStatus = (title?: string) => {
    modalActive.open = !modalActive.open
    if (title) {
      modalActive.title = title
    }

    if (modalActive.open) {
      modalActive.moreSetting = false
      resetForm()
    }
  }
  // 根据id获取菜单数据
  const getMenu = async (event:MouseEvent, id:string) => {
    event.stopPropagation()
    try {
      const resp = await queryById(id)
      if (resp.code === 200) {
        handleModelStatus('编辑菜单')
        sysMenu.value = resp.data
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }
  // 新增菜单
  const addMenu = () => {
    handleModelStatus('新增菜单')
    const sorts = menuList.value.map(item => item.sort)
    if (sorts && sorts.length > 0) {
      const max = Math.max(...sorts as number[])
      sysMenu.value.sort = max + 1
    } else {
      sysMenu.value.sort = 1
    }
  }
  // 新增下级
  const addChildren = (event: MouseEvent, parent: SysMenu) => {
    event.stopPropagation()
    handleModelStatus('新增菜单')

    sysMenu.value.parentId = parent.id
    // 父级为菜单时，默认下级类型为页面
    if (parent.menuType === 'directory') {
      sysMenu.value.menuType = 'page'
    }
    // 父级为页面时，默认下级为权限
    else if (parent.menuType === 'page') {
      sysMenu.value.menuType = 'perms'
    }
    // 默认下级排序为最大值 + 1
    if (parent.children) {
      const sorts = parent.children.map(item => item.sort)
      if (sorts && sorts.length > 0) {
        const max = Math.max(...sorts as number[])
        sysMenu.value.sort = max + 1
      }
    } else {
      sysMenu.value.sort = 1
    }
  }

  // 修改菜单状态
  const handleUpdateStatus = async (event: MouseEvent, sysMenuVO: SysMenuVO, status: string) => {
    event.stopPropagation()
    let menuIds
    if (status == '0') {
      const flattenMenuVo = flattenTree([sysMenuVO])
      // 获取当前及子节点 id
      menuIds = flattenMenuVo.map(menu => menu.id) as string[]
    } else {
      menuIds = [sysMenuVO.id ? sysMenuVO.id : '']
      // 菜单启用时展开子菜单
      expandedRowKeys.value.push(...menuIds)
    }

    let newStatus: string = ''
    try {
      const resp = await updateStatus(menuIds as string[], status)
      if (resp.code === 200) {
        newStatus = resp.data
        message.success(resp.msg)
      } else {
        newStatus = status
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      // 重新赋值
      menuIds.forEach(id => handleMenuStatus(menuList.value, id, newStatus))
    }
  }

  // 回显菜单状态
  const handleMenuStatus = (menuList: Array<SysMenuVO>, id: string, status: string): boolean => {
    for (let menu of menuList) {
      if (menu.id === id) {
        menu.status = status;
        menu.statusIsNormal = menu.status === '0';
        menu.updateStatusLoading = false;
        return true; // 终止循环并返回 true
      }

      if (menu.children && menu.children.length > 0) {
        const found = handleMenuStatus(menu.children, id, status);
        if (found) return true; // 终止外部循环
      }
    }

    return false;
  };

  // 重置表单
  const resetForm = () => {
    sysMenu.value = {
      menuType: 'directory',
      visible: '0',
      cache: '0',
      status: '0',
      linkOpenType: 'inner',
      parentId: '0',
      viewTab: '0'
    }
  }

  // 加载菜单树
  const initTreeData = async () => {
    try {
      const resp = await queryMenuTreeOption()
      if (resp.code === 200) {
        // 深拷贝数据
        const menuTree = cloneDeep(resp.data)
        // 过滤不需要的数据（只保留菜单和页面）
        handleMenuTree(menuTree)
        // 表单树
        parentMenuTree.value = [{
          label: '根节点',
          id: '0',
          menuType: 'directory',
          children: menuTree
        }]
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }

  // 处理menu树形选择
  const handleMenuTree = (menuTree: Array<SysMenu>) => {
    // 过滤最外层
    menuTree = menuTree.filter(tree => tree.menuType !== 'link' && tree.menuType !== 'perms')
    menuTree.forEach(item => {
      if (item.children && item.children.length > 0) {
        item.children = item.children.filter(tree => tree.menuType !== 'link' && tree.menuType !== 'perms')
        handleMenuTree(item.children)
      }
    })
  }
  // 保存菜单
  const saveMenu = async () => {
    await formRef.value?.validate()
    modalActive.saveLoading = true
    try {
      const resp = await save(sysMenu.value)
      if (resp.code === 200) {
        message.success(resp.msg)
        await initList()
        await initTreeData()
        modalActive.open = false
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      modalActive.saveLoading = false
    }
  }

  return {
    modalActive,
    formRef,
    menuRules,
    sysMenu,
    handleUpdateStatus,
    getMenu,
    addMenu,
    addChildren,
    initTreeData,
    saveMenu
  }
}
const {modalActive,sysMenu,menuRules,formRef,handleUpdateStatus,getMenu,addMenu,addChildren,initTreeData,saveMenu} = initSave()
initTreeData()

// 删除菜单
const initDelete = () => {
  // 显示删除提示
  const openDeletePopconfirm = ref<boolean>(false);
  // 打开删除提示框
  const openPopconfirm = () => {
    if (selectedIds.value && selectedIds.value.length > 0) {
      openDeletePopconfirm.value = true
    } else {
      message.warning("请勾选数据")
    }
  }
  // 关闭删除提示框
  const closePopconfirm = () => {
    openDeletePopconfirm.value = false
  }
  // 处理删除逻辑
  const handleDelete = async (id?:string) => {
    const deleteIds = id ? [id] : [...selectedIds.value];

    try {
      if (deleteIds.length > 0) {
        const resp = await deleteData(deleteIds)
        if (resp.code === 200) {
          message.success(resp.msg);
          // id 不存在则清空选中数据
          if (!id) {
            selectedIds.value = []
          } else {
            selectedIds.value = selectedIds.value.filter(item => item !== id)
          }
          await initList()
          await initTreeData()
        } else {
          message.error(resp.msg)
        }
      } else {
        message.warning("请勾选数据")
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      closePopconfirm()
    }
  }

  return {
    openDeletePopconfirm,
    closePopconfirm,
    handleDelete,
    openPopconfirm
  }
}

const {openDeletePopconfirm,closePopconfirm,handleDelete,openPopconfirm} = initDelete()

</script>
