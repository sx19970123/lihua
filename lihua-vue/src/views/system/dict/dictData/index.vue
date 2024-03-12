<template>
  <a-flex vertical :gap="16">
    <a-card>
      <a-form :colon="false">
        <a-flex :gap="8" align="center" >
          <a-form-item class="form-item-single-line" label="标签">
            <a-input v-model:value="dictDataQuery.label" allow-clear placeholder="请输入字典标签"/>
          </a-form-item>
          <a-form-item class="form-item-single-line" label="值">
            <a-input v-model:value="dictDataQuery.value" allow-clear placeholder="请输入字典值"/>
          </a-form-item>
          <a-form-item  class="form-item-single-line">
            <a-button type="primary" @click="queryList">
              <template #icon>
                <SearchOutlined />
              </template>
              查 询
            </a-button>
          </a-form-item>
          <a-form-item  class="form-item-single-line">
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
    <a-card :body-style="{padding: 0}">
      <a-table :columns="dictDataColumn" :data-source="dictDataList" :pagination="false">
        <template #title>
          <a-button type="primary" >
            <template #icon>
              <PlusOutlined />
            </template>
            新 增
          </a-button>
        </template>
        <template #headerCell="{ column }">
          <template v-if="column.key === 'label'">
            <span style="color: #ff4d4f">*</span>
            {{column.title}}
          </template>
          <template v-if="column.key === 'value'">
            <span style="color: #ff4d4f">*</span>
            {{column.title}}
          </template>
        </template>
        <template #bodyCell="{column,text,record}">
          <!--            可编辑内容-->
          <!--          标签-->
          <a-form :model="editableData[record.id]">
            <template v-if="'label' === column.dataIndex">
              <a-form-item class="form-item-single-line" :rules="[{required: true,message: '请输入标签'}]" name="label">
                <a-input v-if="editableData[record.id]"
                         v-model:value="editableData[record.id].label"
                         placeholder="请输入标签"
                         allow-clear
                         style="margin: -5px 0"/>
                <template v-else>
                  {{ text }}
                </template>
              </a-form-item>
            </template>
            <!--          值-->
            <template v-if="'value' === column.dataIndex">
              <a-form-item class="form-item-single-line">
                <a-input v-if="editableData[record.id]"
                         placeholder="请输入值"
                         v-model:value="editableData[record.id].value"
                         allow-clear
                         style="margin: -5px 0"/>
                <template v-else>
                  {{ text }}
                </template>
              </a-form-item>
            </template>
            <!--          备注-->
            <template v-if="'remark' === column.dataIndex">
              <a-form-item class="form-item-single-line">
                <a-input v-if="editableData[record.id]"
                         placeholder="请输入备注"
                         v-model:value="editableData[record.id].remark"
                         allow-clear
                         style="margin: -5px 0"/>
                <template v-else>
                  {{ text }}
                </template>
              </a-form-item>
            </template>
          </a-form>
          <template v-if="column.key === 'action'">
<!--            编辑列-->
            <template v-if="editableData[record.id]">
              <a-button type="link" size="small" >
                <template #icon>
                  <CheckOutlined />
                </template>
                保存
              </a-button>
              <a-divider type="vertical"/>
              <a-button type="link" size="small" danger>
                <template #icon>
                  <CloseOutlined />
                </template>
                取消
              </a-button>
            </template>
            <template v-else>
              <a-button type="link" size="small" @click="edit(record)">
                <template #icon>
                  <EditOutlined />
                </template>
                编辑
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
        </template>
      </a-table>
    </a-card>
  </a-flex>
</template>

<script setup lang="ts">
// 接收父组件传入的typeId
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {findList} from "@/api/system/dict/dictData";
import {reactive, ref} from "vue";
import type { UnwrapRef } from 'vue';
import {message} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';


const props = defineProps<{
  typeId: string
}>()

const initSearch = () => {
  // 定义表头
  const dictDataColumn: ColumnsType = [
    {
      title: '标签',
      dataIndex: 'label',
      key: 'label'
    },
    {
      title: '值',
      dataIndex: 'value',
      key: 'value'
    },
    {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark'
    },
    {
      title: '操作',
      align: 'center',
      key: 'action',
      width: '182px'
    },
  ]
  // 定义查询条件对象
  const dictDataQuery = ref<SysDictDataQueryType>({dictTypeId: props.typeId})
  // 定义查询出的列表集合
  const dictDataList = ref<Array<SysDictDataType>>()
  // 查询列表
  const queryList = async () => {
    const resp =  await findList(dictDataQuery.value)
    if (resp.code === 200) {
      dictDataList.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }
  queryList()
  return {
    dictDataQuery,
    dictDataColumn,
    dictDataList,
    queryList
  }
}

const {dictDataQuery, dictDataColumn, dictDataList, queryList} = initSearch()

const initSave = () => {
  const editableData:UnwrapRef<Record<string, SysDictDataType>> = reactive({})

  const edit = (data: SysDictDataType) => {
    if (data.id) {
      editableData[data.id] = cloneDeep(data)
    }
  }

  return {
    editableData,
    edit
  }
}
const {editableData,edit} = initSave()
</script>

<style scoped>

</style>
