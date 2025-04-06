<template>
  <div>
    <a-flex vertical :gap="16">
      <!--    检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="字典名称">
                <a-input v-model:value="dictTypeQuery.name" placeholder="请输入字典名称" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="字典编码">
                <a-input  v-model:value="dictTypeQuery.code" placeholder="请输入字典编码" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="状态">
                <a-select v-model:value="dictTypeQuery.status" placeholder="清选择" allowClear>
                  <a-select-option v-for="item in sys_status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="创建时间">
                <a-range-picker v-model:value="dictTypeQuery.startEndTime" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item>
                <a-space size="small">
                  <a-button type="primary" @click="initPage" :loading="tableLoad">
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
      <!--    列表页-->
      <a-table :data-source="dictTypeList"
               :columns="dictTypeColumn"
               :pagination="false"
               :loading="tableLoad"
               :row-selection="dictTypeRowSelectionType"
               row-class-name="hover-cursor-pointer"
               :custom-row="handleRowClick"
               row-key="id"
               :scroll="{x: 1500}"
      >
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary" @click="handleModelStatus('新增字典')">
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
            <a-button type="primary" ghost @click="handleReloadCache" :loading="loadCache">
              <template #icon>
                <RedoOutlined />
              </template>
              刷新缓存
            </a-button>
            <table-setting v-model="dictTypeColumn"/>
          </a-flex>
        </template>
        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'code'">
            <a-typography-paragraph copyable>{{text}}</a-typography-paragraph>
          </template>
          <template v-if="column.key === 'type'">
            <dict-tag :dict-data-option="sys_dict_type" :dict-data-value="record[column.key]"/>
          </template>
          <template v-if="column.key === 'status'">
            <a-switch v-model:checked="record.statusIsNormal"
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
          <template v-if="column.key === 'createTime'">
            {{ dayjs(record[column.key]).format('YYYY-MM-DD HH:mm') }}
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" @click="getDictType($event,record.id)">
              <template #icon>
                <EditOutlined />
              </template>
              编辑
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small" @click="openDictConfig($event,record)">
              <template #icon>
                <SettingOutlined />
              </template>
              字典配置
            </a-button>
            <a-divider type="vertical"/>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          ok-text="确 定"
                          cancel-text="取 消"
                          placement="bottomRight"
                          @confirm="handleDelete(record.id)"
            >
              <a-button type="link" danger size="small" @click="(event: MouseEvent) => event.stopPropagation()">
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
            <a-pagination v-model:current="dictTypeQuery.pageNum"
                          v-model:page-size="dictTypeQuery.pageSize"
                          show-size-changer
                          :total="dictTypeTotal"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="handleQueryPage"/>
          </a-flex>
        </template>
      </a-table>
    </a-flex>
    <!--    新增编辑对话框-->
    <a-modal v-model:open="modalActive.open"
             ok-text="保 存"
             cancel-text="关 闭"
             :confirm-loading="modalActive.saveLoading"
             @ok="saveDictType">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
      <a-form layout="horizontal"
              ref="formRef"
              :model="dictTypeData"
              :label-col="{style: { width: '80px' }}"
              :rules="formRules"
              :colon="false"
              @finish="saveDictType"

      >
        <a-form-item label="字典名称" name="name">
          <a-input placeholder="请输入字典名称" v-model:value="dictTypeData.name" show-count :maxlength="30"/>
        </a-form-item>
        <a-form-item label="字典编码" name="code">
          <a-input placeholder="请输入字典编码" v-model:value="dictTypeData.code" show-count :maxlength="30"/>
        </a-form-item>
        <a-form-item label="字典类型">
          <a-select style="width: 120px" v-model:value="dictTypeData.type">
            <a-select-option :value="item.value" v-for="item in sys_dict_type">{{item.label}}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态">
          <a-radio-group v-model:value="dictTypeData.status">
            <a-radio :value="item.value" v-for="item in sys_status">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="dictTypeData.remark" placeholder="请输入字典备注" show-count :maxlength="200"/>
        </a-form-item>
      </a-form>
    </a-modal>
    <!--    字典配置抽屉-->
    <a-drawer v-model:open="drawerAction.openDrawer"
              :width="drawerAction.width"
              :destroyOnClose="true"
              :title="drawerAction.title"
              :body-style="{'padding-top': '0'}">
      <dict-data :type-code="drawerAction.typeCode" :type="drawerAction.type"/>
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import {onMounted, onUnmounted, reactive, ref} from "vue";
import type {SysDictType, SysDictTypeDTO, SysDictTypeVO} from "@/api/system/dict/type/SysDictType";
import {ResponseError, type ResponseType} from "@/api/global/Type.ts"
import type { ColumnsType } from 'ant-design-vue/es/table/interface';
import {deleteData, queryById, queryPage, reloadCache, save, updateStatus} from "@/api/system/dict/DictType.ts";
import dayjs from "dayjs";
import type {Rule} from "ant-design-vue/es/form";
import { message } from "ant-design-vue";
import DictData from "./dictData/index.vue"
import { initDict } from "@/utils/Dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
import settings from "@/settings.ts";
import TableSetting from "@/components/table-setting/index.vue";

const { sys_status,sys_dict_type } = initDict("sys_status","sys_dict_type")


// 列表查询相关
const initSearch = () => {
  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const dictTypeRowSelectionType = {
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
  const handleRowClick = (record:SysDictType) => {
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
  const dictTypeColumn = ref<ColumnsType>([
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
      ellipsis: true,

    },
    {
      title: '编码',
      dataIndex: 'code',
      key: 'code',
      ellipsis: true,

    },
    {
      title: '状态',
      dataIndex: 'status',
      align: 'center',
      key: 'status',
    },
    {
      title: '类型',
      dataIndex: 'type',
      align: 'center',
      key: 'type',
    },
    {
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
      ellipsis: true,

    },
    {
      title: '操作',
      align: 'center',
      key: 'action',
      width: '292px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    },
  ])
  // 查询条件定义
  const dictTypeQuery = ref<SysDictTypeDTO>({
    name: '',
    code: '',
    startEndTime: [],
    pageNum: 1,
    pageSize: 10
  })
  // 总条数
  const dictTypeTotal = ref<number>()
  // 数据集对象
  const dictTypeList = ref<Array<SysDictTypeVO>>([])
  // 列表loading
  const tableLoad = ref<boolean>(false)

  // 重置查询
  const resetPage = async () => {
    dictTypeQuery.value = {
      name: '',
      code: '',
      startEndTime: [],
      pageNum: 1,
      pageSize: 10
    }
    await initPage()
  }
  // 数据页码从1开始加载数据
  const initPage = async () => {
    dictTypeQuery.value.pageNum = 1
    await handleQueryPage()
  }
  // 查询数据
  const handleQueryPage = async () => {
    tableLoad.value = true
    try {
      const resp = await queryPage(dictTypeQuery.value)
      if (resp.code === 200) {
        dictTypeList.value = resp.data.records
        dictTypeTotal.value = resp.data.total
        // 回显状态
        dictTypeList.value?.some(dictType => {
          dictType.statusIsNormal = dictType.status === '0'
          dictType.updateStatusLoading = false
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
  initPage()
  return {
    dictTypeQuery,
    dictTypeTotal,
    dictTypeList,
    dictTypeColumn,
    dictTypeRowSelectionType,
    selectedIds,
    tableLoad,
    handleRowClick,
    resetPage,
    initPage,
    handleQueryPage
  }
}
const {dictTypeQuery,dictTypeTotal,dictTypeList,dictTypeColumn,dictTypeRowSelectionType,selectedIds,tableLoad,handleRowClick,resetPage,initPage,handleQueryPage} = initSearch()

// 数据保存相关
const initSave = () => {
  // 定义表单ref
  const formRef = ref()
  // 表单验证
  const formRules: Record<string, Rule[]> = {
    name: [
        { required: true, message: '请输入字典名称', trigger: 'change' },
        { max: 30, message: '字典名称长度最大为30字符', trigger: 'change' },
    ],
    code: [
        { required: true, message: '请输入字典编码', trigger: 'change' },
        { max: 30, message: '字典编码长度最大为30字符', trigger: 'change' },
        { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: '字典编码支持字母、数字、下划线，并且不能以数字开头', trigger: 'change' },
    ],
  }
  // modal 相关属性定义
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string, // 模态框标题
  }
  const modalActive = reactive<modalActiveType>( {
    open: false,
    saveLoading: false,
    title: ''
  })

  // 控制模态框开关
  const handleModelStatus = (title?: string) => {
    modalActive.open = !modalActive.open
    if (title) {
      modalActive.title = title
    }
    if (modalActive.open) {
      resetForm()
    }
  }

  const dictTypeData = reactive<SysDictType>({
    type: '0',
    status: '0'
  })

  // 保存方法
  const saveDictType = async () => {
      await formRef.value.validate()
      modalActive.saveLoading = true
      try {
        const resp = await save(dictTypeData)
        if (resp.code === 200) {
          handleModelStatus()
          message.success(resp.msg)
          await handleQueryPage()
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

  const getDictType = async (event: MouseEvent,id: string) => {
    event.stopPropagation()
    try {
      const resp: ResponseType<SysDictType> = await queryById(id)
      if (resp.code === 200) {
        handleModelStatus('编辑字典')
        dictTypeData.id = resp.data.id
        dictTypeData.name = resp.data.name
        dictTypeData.code = resp.data.code
        dictTypeData.type = resp.data.type
        dictTypeData.status = resp.data.status
        dictTypeData.remark = resp.data.remark
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

  const resetForm = () => {
    dictTypeData.id = undefined
    dictTypeData.name = undefined
    dictTypeData.code = undefined
    dictTypeData.type =  '0'
    dictTypeData.status = '0'
    dictTypeData.remark = undefined
  }

  // 修改角色状态
  const handleUpdateStatus = async (event: MouseEvent, id: string, status: string) => {
    event.stopPropagation();
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
      dictTypeList.value.some(dictType => {
        if (dictType.id === id) {
          dictType.status = newStatus
          dictType.statusIsNormal = dictType.status === '0'
          dictType.updateStatusLoading = false
          return
        }
      })
    }
  }

  return {
    dictTypeData,
    modalActive,
    formRules,
    handleUpdateStatus,
    handleModelStatus,
    saveDictType,
    getDictType,
    formRef
  }
}
const { dictTypeData,modalActive,formRules,handleUpdateStatus,handleModelStatus,saveDictType,getDictType,formRef } = initSave()

// 数据删除相关
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
  // 删除方法
  const handleDelete = async (id?:string) => {
    const deleteIds = id ? [id] : [...selectedIds.value];

    if (deleteIds.length > 0) {
      try {
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
      } catch (e) {
        if (e instanceof ResponseError) {
          message.error(e.msg)
        } else {
          console.error(e)
        }
      }
    } else {
      message.warning("请勾选数据")
    }
    closePopconfirm()
  }

  return{
    openDeletePopconfirm,
    openPopconfirm,
    closePopconfirm,
    handleDelete
  }
}
const {openDeletePopconfirm,openPopconfirm,closePopconfirm, handleDelete } = initDelete()

const initDictConfig = () => {
  type drawerAction = {
    openDrawer: boolean,
    title?: string,
    typeCode: string,
    type: string,
    width: number
  }
  const drawerAction = reactive<drawerAction>({
    openDrawer: false,
    title: '',
    typeCode: '',
    type: '',
    width: 1100
  })
  const openDictConfig = (event: any ,dictType: SysDictType) => {
    event.stopPropagation()
    drawerAction.title = dictType.name
    drawerAction.openDrawer = true
    if (dictType.code) {
      drawerAction.typeCode = dictType.code
    }
    if (dictType.type) {
      drawerAction.type = dictType.type
    }
  }
  const getDrawerWidth = () => {
    const width = window.innerWidth
    drawerAction.width = width >= 1100 ? 1100 : width
  }

  return {
    drawerAction,
    openDictConfig,
    getDrawerWidth
  }
}
const {drawerAction,openDictConfig,getDrawerWidth} = initDictConfig()

// 处理刷新缓存
const initLoadCache = () => {
  // 刷新缓存loading
  const loadCache = ref<boolean>(false)

  // 执行刷新缓存
  const handleReloadCache = async () => {
    loadCache.value = true
    try {
      const resp = await reloadCache()
      if (resp.code === 200) {
        message.success(resp.msg)
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
      loadCache.value = false
    }
  }

  return {
    handleReloadCache,
    loadCache
  }
}

const {handleReloadCache, loadCache} = initLoadCache()

// 组件创建完成后获取抽屉展开宽度
onMounted(() => {
  getDrawerWidth()

  window.addEventListener('resize', getDrawerWidth)
})
// 组件销毁后删除监听
onUnmounted(() => {
  window.removeEventListener('resize', getDrawerWidth)
})

</script>
