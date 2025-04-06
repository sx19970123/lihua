<template>
  <div>
    <a-flex vertical :gap="16">
<!--      查询条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="角色名称">
                <a-input placeholder="请输入角色名称" allowClear v-model:value="roleQuery.name"/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="角色编码">
                <a-input placeholder="请输入角色编码" allowClear v-model:value="roleQuery.code"/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="状态">
                <a-select placeholder="请选择" allowClear v-model:value="roleQuery.status">
                  <a-select-option v-for="item in sys_status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item>
                <a-space size="small">
                  <a-button type="primary" :loading="tableLoad" @click="initPage">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button :loading="tableLoad" @click="resetPage">
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
<!--      列表页面-->
        <a-table
            :columns="roleColumn"
            :data-source="roleList"
            :pagination="false"
            :loading="tableLoad"
            :row-selection="roleRowSelectionType"
            row-class-name="hover-cursor-pointer"
            :custom-row="handleRowClick"
            :scroll="{x: 1000}"
            row-key="id"
        >
          <template #title>
            <a-flex :gap="8">
              <a-button type="primary" @click="handleModelStatus('新增角色')">
                <template #icon>
                  <PlusOutlined />
                </template>
                新 增
              </a-button>
              <a-popconfirm title="删除后不可恢复，是否删除？"
                            :open="openDeletePopconfirm"
                            ok-text="确 定"
                            cancel-text="取 消"
                            @confirm="handleDelete(undefined)"
                            @cancel="closePopconfirm"
                            @open-change="(open: boolean) => !open ? closePopconfirm(): ''"
              >
                <a-button danger @click="openPopconfirm">
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  删 除
                  <span v-if="selectedIds && selectedIds.length > 0" style="margin-left: 4px"> {{selectedIds.length}} 项</span>
                </a-button>
              </a-popconfirm>

              <!--            表格设置-->
              <table-setting v-model="roleColumn"/>
            </a-flex>
          </template>

          <template #bodyCell="{column,record,text}">
            <!-- 角色状态-->
            <template v-if="column.key === 'status'">
              <a-switch v-model:checked="record.statusIsNormal"
                        :disabled="record.code === 'ROLE_admin'"
                        @change="(checked: boolean | string | number, event: MouseEvent) => handleUpdateStatus(event,record.id, text)"
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
            <template v-if="column.key === 'code'">
              <a-typography-paragraph copyable>{{text}}</a-typography-paragraph>
            </template>
            <template v-if="column.key === 'createTime'">
              {{dayjs(text).format('YYYY-MM-DD HH:mm')}}
            </template>
            <template v-if="column.key === 'action' && record.code !== 'ROLE_admin'">
              <a-button type="link" size="small" @click="(event: MouseEvent) => getRole(event, record.id)">
                <template #icon>
                  <EditOutlined />
                </template>
                编辑
              </a-button>
              <a-divider type="vertical"/>
              <a-popconfirm title="删除后不可恢复，是否删除？"
                            placement="bottomRight"
                            ok-text="确 定"
                            cancel-text="取 消"
                            @confirm="handleDelete(record.id)"
              >
                <a-button type="link" danger size="small" @click="(event: any) => event.stopPropagation()">
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  删除
                </a-button>
              </a-popconfirm>
            </template>
          </template>
          <template #footer>
            <a-flex justify="flex-end">
              <a-pagination v-model:current="roleQuery.pageNum"
                            v-model:page-size="roleQuery.pageSize"
                            show-size-changer
                            :total="roleTotal"
                            :show-total="(total:number) => `共 ${total} 条`"
                            @change="initPage"
                            />
            </a-flex>
          </template>
        </a-table>
      </a-flex>
<!--    角色模态框-->
    <a-modal v-model:open="modalActive.open" :footer="null">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
      <a-form :model="role"
              :label-col="{style: { width: '80px' }}"
              :colon="false"
              :rules="roleRules"
              @finish="saveRole"
      >
        <a-form-item label="角色名称" name="name">
          <a-input placeholder="请输入角色名称" v-model:value="role.name" :maxlength="35" show-count allow-clear/>
        </a-form-item>
        <a-form-item label="角色编码" name="code">
          <a-input placeholder="角色编码以ROLE_开头" v-model:value="role.code" :maxlength="50" show-count allow-clear/>
        </a-form-item>
        <a-form-item label="角色状态">
          <a-radio-group v-model:value="role.status">
            <a-radio v-for="item in sys_status" :value="item.value">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="菜单">
          <easy-tree-select ref="easyTreeSelectRef" :tree-data="menuTreeOption" v-model="role.menuIds">
            <template #title="{label,menuType,keyword}">
              <span v-if="label.indexOf(keyword) > -1">
                <span>{{label.substring(0,label.indexOf(keyword))}}</span>
                <span :style="{'color':  themeStore.getColorPrimary()}">{{keyword}}</span>
                <span>{{label.substring(label.indexOf(keyword) + keyword.length)}}</span>
              </span>
              <span v-else>{{ label }}</span>
              <dict-tag :dict-data-value="menuType" :dict-data-option="sys_menu_type" :style="{border: 'none', 'margin-left': '8px'}"/>
            </template>
          </easy-tree-select>
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea v-model:value="role.remark" placeholder="请输入备注" :maxlength="200" show-count allow-clear/>
        </a-form-item>

        <a-flex :gap="8" justify="flex-end">
          <a-button @click="modalActive.open = false">关 闭</a-button>
          <a-button type="primary" html-type="submit" :loading="modalActive.saveLoading">保 存</a-button>
        </a-flex>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {reactive, ref, useTemplateRef, watch} from "vue";
import {deleteData, queryById, queryPage, save, updateStatus} from "@/api/system/role/Role.ts";
import {initDict} from "@/utils/Dict.ts";
import DictTag from "@/components/dict-tag/index.vue";
import EasyTreeSelect from "@/components/easy-tree-select/index.vue"
import {queryMenuTreeOption} from "@/api/system/menu/Menu.ts";
import type {Rule} from "ant-design-vue/es/form";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
import type {SysMenu} from "@/api/system/menu/type/SysMenu.ts";
import type {SysRole, SysRoleDTO, SysRoleVO} from "@/api/system/role/type/SysRole.ts";
import {ResponseError} from "@/api/global/Type.ts";
import {useThemeStore} from "@/stores/theme.ts";
import settings from "@/settings.ts";
import TableSetting from "@/components/table-setting/index.vue";
const {sys_status,sys_menu_type} = initDict("sys_status","sys_menu_type")
const easyTreeSelectRef = useTemplateRef<InstanceType<typeof EasyTreeSelect>>("easyTreeSelectRef")
const themeStore = useThemeStore();

// 列表查询相关
const initSearch = () => {
  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const roleRowSelectionType = {
    columnWidth: '55px',
    type: 'checkbox',
    // 支持跨页勾选
    preserveSelectedRowKeys: true,
    // 指定选中id的数据集合，操作完后可手动清空
    selectedRowKeys: selectedIds,
    onChange: (ids: Array<string>) => {
      selectedIds.value = ids
    },
    // 设置禁选数据
    getCheckboxProps: (record: SysRoleVO) => ({
      disabled: record.code === 'ROLE_admin'
    })
  }
  // 点击选中行
  const handleRowClick = (record:SysRoleVO) => {
    if (record.code === 'ROLE_admin') {
      return
    }
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

  // 列表列定义
  const roleColumn = ref<ColumnsType>([
    {
      title: '角色名称',
      dataIndex: 'name',
      ellipsis: true,
      key: 'name'
    },
    {
      title: '角色编码',
      dataIndex: 'code',
      ellipsis: true,
      key: 'code'
    },
    {
      title: '状态',
      dataIndex: 'status',
      align: 'center',
      ellipsis: true,
      key: 'status',
    },{
      title: '备注',
      dataIndex: 'remark',
      ellipsis: true,
      key: 'remark'
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      align: 'center',
      key: 'createTime',
    },
    {
      title: '操作',
      align: 'center',
      key: 'action',
      width: '182px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    },
  ])
  // 查询条件
  const roleQuery = ref<SysRoleDTO> ({
    name: '',
    code: '',
    status: null,
    pageNum: 1,
    pageSize: 10,
  })
  // 列表数据
  const roleList = ref<Array<SysRoleVO>>([])
  const roleTotal = ref<number>()
  // 列表加载中loading
  const tableLoad = ref<boolean>(false)

  // 查询列表
  const initPage = async () => {
    tableLoad.value = true
    try {
      const resp = await queryPage(roleQuery.value)
      if (resp.code === 200) {
        roleList.value = resp.data.records
        roleTotal.value = resp.data.total

        // 回显状态
        roleList.value.forEach(role => {
          role.statusIsNormal = role.status === '0'
          role.updateStatusLoading = false
        })
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
  const resetPage = async () => {
    roleQuery.value = {
      name: '',
      code: '',
      status: null,
      pageNum: 1,
      pageSize: 10,
    }
    await initPage()
  }

  initPage()
  return {
    roleQuery,
    roleTotal,
    roleColumn,
    roleList,
    tableLoad,
    selectedIds,
    roleRowSelectionType,
    initPage,
    resetPage,
    handleRowClick,
  }
}
const {roleQuery,roleTotal,roleColumn,roleList,tableLoad,selectedIds,roleRowSelectionType,initPage,resetPage,handleRowClick} = initSearch()


// 数据保存相关
const initSave = () => {
  // modal 相关属性定义
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string, // 模态框标题
  }
  const modalActive = reactive<modalActiveType>({
    open: false,
    saveLoading: false,
    title: ''
  })
  // 角色数据
  let role = ref<SysRole>({})
  // 修改模态框状态
  const handleModelStatus = async (title?: string) => {
    modalActive.open = !modalActive.open
    if (title) {
      modalActive.title = title
    }
    if (modalActive.open) {
      // 加载菜单树
      await initMenuTree()
      // 重置表单
      resetForm()
    }
  }
  // 表单验证
  const roleRules: Record<string, Rule[]> = {
    name: [
      { required: true, message: '请输入角色名称',trigger: 'change' }
    ],
    code: [
      { required: true, message: '请输入角色编码',trigger: 'change' },
      { pattern: /^ROLE_[a-zA-Z0-9_]+$/, message: '角色编码以ROLE_开头，只能包含数字、字母、下划线',trigger: 'change' }
    ]
  }

  // 根据id获取角色，打开模态框
  const getRole = async (event:MouseEvent ,id: string) => {
    event.stopPropagation()
    try {
      const resp = await queryById(id)
      if (resp.code === 200 && resp.data) {
        await handleModelStatus("修改角色")
        role.value = resp.data
      } else {
        console.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }
  // 重置表单
  const resetForm = () => {
    role.value = {
      status: '0',
      menuIds: []
    }
    // 重置树形选择组件
    easyTreeSelectRef.value?.reset()
  }

  // 保存角色
  const saveRole = async () => {
    modalActive.saveLoading = true
    // 开关父子联动绑定赋值不同，进行处理
    if (!Array.isArray(role.value.menuIds)) {
      role.value.menuIds = role.value.menuIds?.checked
    }
    try {
      const resp = await save(role.value)
      if (resp.code === 200) {
        message.success(resp.msg)
        modalActive.open = false
        await initPage()
      } else {
        message.error(resp.msg)
        console.error(resp.msg)
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

  // 修改角色状态
  const handleUpdateStatus = async (event:MouseEvent, id: string, status: string) => {
    event.stopPropagation()
    let newStatus: string = ''
    try {
      const resp = await updateStatus(id, status)
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
      roleList.value.some(role => {
        if (role.id === id) {
          role.status = newStatus
          role.statusIsNormal = role.status === '0'
          role.updateStatusLoading = false
          return
        }
      })
    }
  }

  return {
    modalActive,
    role,
    roleRules,
    handleUpdateStatus,
    handleModelStatus,
    getRole,
    saveRole
  }
}

const {modalActive,role,roleRules,handleUpdateStatus,handleModelStatus,getRole,saveRole} = initSave()

// 初始化菜单树
const initMenuTree = () => {
  const menuTreeOption = ref<SysMenu[]>([])
  queryMenuTreeOption().then(resp => {
    if (resp.code === 200) {
      menuTreeOption.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }).catch((e) => {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  })
  return {
    menuTreeOption
  }
}
const { menuTreeOption } = initMenuTree()

// 删除岗位
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
          await initPage()
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
