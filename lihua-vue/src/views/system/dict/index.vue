<template>
  <div>
    <a-flex vertical :gap="16">
      <!--    检索条件-->
      <a-card>
        <a-form :colon="false">
          <a-flex :gap="8" align="center">
            <a-form-item class="form-item-single-line" label="字典名称">
              <a-input v-model:value="dictTypeQuery.name" placeholder="请输入字典名称" allowClear/>
            </a-form-item>
            <a-form-item class="form-item-single-line" label="字典编码">
              <a-input  v-model:value="dictTypeQuery.code" placeholder="请输入字典编码" allowClear/>
            </a-form-item>
            <a-form-item class="form-item-single-line" label="创建时间">
              <a-range-picker v-model:value="dictTypeQuery.startEndTime" allowClear/>
            </a-form-item>
            <a-form-item class="form-item-single-line">
              <a-button type="primary" @click="initPage" :loading="tableLoad">
                <template #icon>
                  <SearchOutlined />
                </template>
                查 询
              </a-button>
            </a-form-item>
            <a-form-item class="form-item-single-line">
              <a-button :loading="tableLoad" @click="resetPage">
                <template #icon>
                  <RedoOutlined />
                </template>
                重 置
              </a-button>
            </a-form-item>
          </a-flex>
        </a-form>
      </a-card>
      <!--    列表页-->
      <a-card :body-style="{padding: 0}">
        <a-table :data-source="dictTypeList"
                 :columns="dictTypeColumn"
                 :pagination="false"
                 :loading="tableLoad"
                 :row-selection="dictTypeRowSelectionType"
                 :rowKey="(item:SysDictType) => item.id"
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
              >
                <a-button danger @click="openPopconfirm">
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  删 除
                  <span v-if="selectedIds && selectedIds.length > 0" style="margin-left: 4px"> {{selectedIds.length}} 项</span>
                </a-button>
              </a-popconfirm>
            </a-flex>
          </template>
          <template #bodyCell="{column,record}">
            <template v-if="column.key === 'type'">
              <template v-if="record.type === '0'">
                一般字典
              </template>
              <template v-if="record.type === '1'">
                树形字典
              </template>
            </template>
            <template v-if="column.key === 'createTime'">
              {{ dayjs(record[column.key]).format('YYYY-MM-DD HH:mm') }}
            </template>
            <template v-if="column.key === 'action'">
              <a-button type="link" size="small" @click="getDictType(record.id)">
                <template #icon>
                  <EditOutlined />
                </template>
                编辑
              </a-button>
              <a-divider type="vertical"/>
              <a-button type="link" size="small" @click="openDictConfig(record)">
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
                <a-button type="link" danger size="small">
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
                            :total="dictTypeTotal"
                            :show-total="(total:number) => `共 ${total} 条`"
                            @change="queryPage"/>
            </a-flex>
          </template>
        </a-table>
      </a-card>
    </a-flex>
    <!--    新增编辑对话框-->
    <a-modal v-model:open="modalAction.open" ok-text="保 存"
             cancel-text="关 闭"
             :confirm-loading="modalAction.saveLoading"
             @ok="saveDictType">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalAction.title}}</a-typography-title>
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
            <a-select-option value="0">一般字典</a-select-option>
            <a-select-option value="1">树型字典</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态">
          <a-radio-group v-model:value="dictTypeData.status">
            <a-radio value="0">正常</a-radio>
            <a-radio value="1">停用</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="dictTypeData.remark" placeholder="请输入字典备注" show-count :maxlength="200"/>
        </a-form-item>
      </a-form>
    </a-modal>
    <!--    字典配置抽屉-->
    <a-drawer v-model:open="drawerAction.openDrawer"
              :width="1120"
              :destroyOnClose="true"
              :title="drawerAction.title"
              :body-style="{'padding-top': '0'}">
      <dict-data :type-code="drawerAction.typeCode" :type="drawerAction.type" />
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from "vue";
import type {SysDictType, SysDictTypeDTO} from "@/api/system/dict/type/SysDictType";
import type {ResponseType, PageResponseType } from "@/api/type"
import type { ColumnsType } from 'ant-design-vue/es/table/interface';
import {deleteData, findById, findPage, save} from "@/api/system/dict/dictType";
import dayjs from "dayjs";
import type {Rule} from "ant-design-vue/es/form";
import { message } from "ant-design-vue";
import DictData from "./dictData/index.vue"
import {initDict} from "@/utils/dict"

// 在组件中的合适位置调用initDict函数
onMounted(async () => {
  const {yes_no} = await initDict("yes_no")
  console.log(yes_no[0][0])
});

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
  // 列表列定义
  const dictTypeColumn:ColumnsType = [
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name'
    },
    {
      title: '编码',
      dataIndex: 'code',
      key: 'code'
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
      width: '180px'
    },
    {
      title: '操作',
      align: 'center',
      key: 'action',
      width: '294px'
    },
  ]
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
  const dictTypeList = ref<Array<SysDictType>>()
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
    await queryPage()
  }
  // 查询数据
  const queryPage = async () => {
    tableLoad.value = true
    const resp:ResponseType<PageResponseType<SysDictType>> = await findPage(dictTypeQuery.value)
    if (resp.code === 200) {
      dictTypeList.value = resp.data.records
      dictTypeTotal.value = resp.data.total
    }
    tableLoad.value = false
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
    resetPage,
    initPage,
    queryPage
  }
}
const {dictTypeQuery,dictTypeTotal,dictTypeList,dictTypeColumn,dictTypeRowSelectionType,selectedIds,tableLoad,resetPage,initPage,queryPage} = initSearch()

// 数据保存相关
const initSave = () => {
  // 定义表单ref
  const formRef = ref()
  // modal 相关属性定义
  type modalActionType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string, // 模态框标题
  }
  // 表单验证
  const formRules: Record<string, Rule[]> = {
    name: [
        { required: true, message: '请输入字典名称', trigger: 'change' },
        { max: 30, message: '字典名称长度最大为30字符', trigger: 'change' },
    ],
    code: [
        { required: true, message: '请输入字典编码', trigger: 'change' },
      { max: 30, message: '字典编码长度最大为30字符', trigger: 'change' },
    ],
  }

  const modalAction = reactive<modalActionType>( {
    open: false,
    saveLoading: false,
    title: ''
  })

  // 控制模态框开关
  const handleModelStatus = (title?: string) => {
    modalAction.open = !modalAction.open
    if (title) {
      modalAction.title = title
    }
    if (modalAction.open) {
      resetForm()
    }
  }

  const dictTypeData = reactive<SysDictType>({
    id: '',
    name: '',
    code: '',
    type: '0',
    status: '0',
    remark: ''
  })

  // 保存方法
  const saveDictType = async () => {
    modalAction.saveLoading = true
    try {
      await formRef.value.validate()
      const resp = await save(dictTypeData)
      if (resp.code === 200) {
        message.success(resp.msg)
        handleModelStatus()
        await initPage()
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      console.error(e)
    }
    modalAction.saveLoading = false
  }

  const getDictType = async (id: string) => {
    const resp: ResponseType<SysDictType> = await findById(id)
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
  }

  const resetForm = () => {
    dictTypeData.id = ''
    dictTypeData.name = ''
    dictTypeData.code = ''
    dictTypeData.type =  '0'
    dictTypeData.status = '0'
    dictTypeData.remark = ''
  }

  return {
    dictTypeData,
    modalAction,
    formRules,
    handleModelStatus,
    saveDictType,
    getDictType,
    formRef
  }
}
const { dictTypeData,modalAction,formRules,handleModelStatus,saveDictType,getDictType,formRef } = initSave()

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
    if (id) {
      selectedIds.value = [id]
    }
    if (selectedIds.value && selectedIds.value.length > 0) {
      const resp = await deleteData(selectedIds.value)
      if (resp.code === 200) {
        message.success(resp.msg);
        // 清空选中
        selectedIds.value = []
        closePopconfirm()
        await initPage()
      } else {
        message.error(resp.msg)
      }
    } else {
      message.warning("请勾选数据")
    }
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
    typeCode?: string,
    type?: string
  }
  const drawerAction = reactive<drawerAction>({
    openDrawer: false,
    title: '',
    typeCode: '',
    type: '',
  })
  const openDictConfig = (dictType: SysDictType) => {
    drawerAction.title = dictType.name
    drawerAction.typeCode = dictType.code
    drawerAction.type = dictType.type
    drawerAction.openDrawer = true
  }

  return {
    drawerAction,
    openDictConfig
  }
}
const {drawerAction,openDictConfig} = initDictConfig()
</script>
