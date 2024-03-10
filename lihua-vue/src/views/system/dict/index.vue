<template>
  <div>
    <a-flex vertical :gap="16">
      <!--    检索条件-->
      <a-card>
        <a-form>
          <a-flex :gap="8" align="center" v-hasRole="[123]">
            <a-form-item class="form-item-single-line" label="字典名称">
              <a-input placeholder="请输入字典名称"/>
            </a-form-item>
            <a-form-item class="form-item-single-line" label="字典编码">
              <a-input placeholder="请输入字典编码"/>
            </a-form-item>
            <a-form-item class="form-item-single-line">
              <a-button type="primary" @click="getPageData"> <SearchOutlined /> 查 询</a-button>
            </a-form-item>
            <a-form-item class="form-item-single-line">
              <a-button> <RedoOutlined /> 重 置</a-button>
            </a-form-item>
          </a-flex>
        </a-form>
      </a-card>
      <!--    列表页-->
      <a-table :data-source="dictTypeList"
                v-dragTable="sortData"
               :columns="dictTypeColumn"
               :pagination="false"
               :row-selection="dictTypeRowSelectionType"
               :rowKey="(item:SysDictType) => item.id"
      >
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary" @click="handleModelStatus('新增字典')"> <PlusOutlined /> 新 增</a-button>
            <a-button danger><DeleteOutlined /> 删 除</a-button>
          </a-flex>
        </template>
        <template #bodyCell="{column,record}">
          <template v-if="column.key === 'name'">
            <a-flex :gap="16"><HolderOutlined class="drag-item"/> {{record[column.key]}}</a-flex>
          </template>
          <template v-if="column.key === 'createTime'">
            {{ dayjs(record[column.key]).format('YYYY-MM-DD HH:mm') }}
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small"> <EditOutlined /> 编辑</a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small"> <SettingOutlined /> 字典配置</a-button>
            <a-divider type="vertical"/>
            <a-button type="link" danger size="small"> <DeleteOutlined /> 删除</a-button>
          </template>
        </template>
        <template #footer>
          <a-flex justify="flex-end">
            <a-pagination :total="dictTypeTotal" />
          </a-flex>
        </template>
      </a-table>
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
  </div>
</template>

<script setup lang="ts">
import {reactive, ref, watch} from "vue";
import type {SysDictType, SysDictTypeDTO} from "@/api/system/dict/type/SysDictType";
import type {ResponseType, PageResponseType } from "@/api/type"
import type { ColumnsType } from 'ant-design-vue/es/table/interface';
import {findPage, save} from "@/api/system/dict/dict";
import dayjs from "dayjs";
import type {Rule} from "ant-design-vue/es/form";
import { message } from "ant-design-vue";

// 初始化查询相关
const initSearch = () => {
  // 列表勾选对象
  const dictTypeRowSelectionType = {
    columnWidth: '55px',
    type: 'checkbox'
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
      width: '100px'
    },
    {
      title: '类型',
      dataIndex: 'type',
      align: 'center',
      key: 'type',
      width: '100px'
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
      width: '292px'
    },
  ]
  // 查询条件定义
  const dictTypeQuery = ref<SysDictTypeDTO>({
    name: '',
    code: '',
    createTimeStart: '',
    createTimeEnd: '',
    pageNum: 0,
    pageSize: 0
  })
  // 总条数
  const dictTypeTotal = ref<number>()
  // 数据集对象
  const dictTypeList = ref<Array<SysDictType>>()

  // 分页查询数据
  const getPageData = async () => {
    const resp:ResponseType<PageResponseType<SysDictType>> = await findPage(dictTypeQuery.value)
    if (resp.code === 200) {
      dictTypeList.value = resp.data.records
      dictTypeTotal.value = resp.data.total
    }
  }
  getPageData()
  return {
    dictTypeQuery,
    dictTypeTotal,
    dictTypeList,
    dictTypeColumn,
    dictTypeRowSelectionType,
    getPageData
  }
}

const {dictTypeQuery,dictTypeTotal,dictTypeList,dictTypeColumn,dictTypeRowSelectionType,getPageData} = initSearch()

// 初始化新增/编辑/回显 相关
const initSave = () => {
  // 定义表单ref
  const formRef = ref()

  type modalActionType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string, // 模态框标题
  }

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
  }

  // 保存的字典数据
  const dictTypeData = reactive<SysDictType>({
    id: '',
    name: '',
    code: '',
    type: '0',
    status: '0',
    remark: ''
  })

  const saveDictType = async () => {
    modalAction.saveLoading = true
    try {
      await formRef.value.validate()
      const resp = await save(dictTypeData)
      if (resp.code === 200) {
        message.success(resp.msg)
        handleModelStatus()
        await getPageData()
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      console.error(e)
    }
    modalAction.saveLoading = false
  }

  return {
    dictTypeData,
    modalAction,
    formRules,
    handleModelStatus,
    saveDictType,
    formRef
  }
}

const { dictTypeData,modalAction,formRules,handleModelStatus,saveDictType,formRef } = initSave()

const saveSort = () => {
  const updateSort = (value: Array<SysDictType>) => {
    dictTypeList.value = value
  }

  const sortData = {
    data: dictTypeList,
    fun: updateSort
  }
  return {sortData}
}

const { sortData } = saveSort()


// 关闭dialog时重置表单
watch(() => modalAction.open, (value) => {
  if (!value) {
    formRef.value.resetFields();
  }
})

</script>

<style>

</style>

