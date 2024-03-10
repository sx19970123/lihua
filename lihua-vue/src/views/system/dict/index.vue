<template>
  <div>
    <a-flex vertical :gap="16">
      <!--    检索条件-->
      <a-card>
        <a-form>
          <a-flex :gap="8" align="center" v-hasRole="[123]">
            <a-form-item class="form-item-single-line"  label="字典名称">
              <a-input placeholder="请输入字典名称"/>
            </a-form-item>
            <a-form-item class="form-item-single-line"  label="字典编码">
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
               :columns="dictTypeColumn"
               :pagination="false"
               :row-selection="dictTypeRowSelectionType"
               :rowKey="(item:SysDictType) => item.id"
      >
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary" @click="handleOpenModel('新增字典')"> <PlusOutlined /> 新 增</a-button>
            <a-button danger><DeleteOutlined /> 删 除</a-button>
          </a-flex>
        </template>
        <template #bodyCell="{column,record}">
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small"> <EditOutlined /> 编辑</a-button>
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
    <a-modal v-model:open="modalAction.open">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalAction.title}}</a-typography-title>
        </div>
      </template>
      <a-form layout="horizontal"
              :label-col="{style: { width: '70px' }}"
              :colon="false"
      >
        <a-form-item label="字典名称">
          <a-input/>
        </a-form-item>
        <a-form-item label="字典编码">
          <a-input/>
        </a-form-item>
        <a-form-item label="状态">
          <a-radio>正常</a-radio>
          <a-radio>停用</a-radio>
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref, type Ref, watch} from "vue";
import type {SysDictType, SysDictTypeDTO} from "@/api/system/dict/type/SysDictType";
import type {ResponseType, PageResponseType } from "@/api/type"
import type { ColumnsType, RowSelectionType } from 'ant-design-vue/es/table/interface';
import {findPage} from "@/api/system/dict/dict";

// 初始化查询相关
const initSearch = () => {
  // 列表勾选对象
  const dictTypeRowSelectionType = {
    columnWidth: '55px',
    fixed: true,
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
      key: 'status'
    },
    {
      title: '类型',
      dataIndex: 'isTree',
      align: 'center',
      key: 'isTree'
    },
    {
      title: '备注',
      dataIndex: 'isTree',
      key: 'isTree'
    },
    {
      title: '创建用户',
      dataIndex: 'createId',
      align: 'center',
      key: 'createId'
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      align: 'center',
      key: 'createTime'
    },
    {
      title: '操作',
      align: 'center',
      key: 'action'
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
  type modalActionType = {
    open: boolean, // 模态框开关
    title: string, // 模态框标题
  }

  const modalAction = reactive<modalActionType>( {
    open: false,
    title: '新增'
  })

  // 控制模态框开关
  const handleOpenModel = (title: string) => {
    modalAction.open = !modalAction.open
    modalAction.title = title
  }


  return {
    modalAction,
    handleOpenModel
  }
}

const { modalAction,handleOpenModel } = initSave()



</script>

<style>

</style>

