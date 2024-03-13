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
      <a-table :columns="dictDataColumn"
               :data-source="dictDataList"
               :pagination="false"
        >
          <template #title>
            <a-button type="primary" >
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
          </template>

          <template #bodyCell="{column,text,record}">
            <!--            可编辑内容-->
            <!--          标签-->
            <template v-if="'label' === column.dataIndex">
              <a-input
                  v-if="editableData[record.id]"
                  class="err-placeholder"
                  placeholder="请输入标签"
                  :status="editableData[record.id].label?'':'error'"
                  v-model:value="editableData[record.id].label"
                  allow-clear/>
              <template v-else>
                <a-flex :gap="8">
                  <HolderOutlined class="table-move-item" style="cursor: move"/>
                  {{ text }}
                </a-flex>
              </template>
            </template>
            <!--          值-->
            <template v-if="'value' === column.dataIndex">
              <a-input
                  v-if="editableData[record.id]"
                  class="err-placeholder"
                  placeholder="请输入值"
                  :status="editableData[record.id].value?'':'error'"
                  v-model:value="editableData[record.id].value"
                  allow-clear
              />
              <template v-else>
                {{ text }}
              </template>
            </template>
            <!--          备注-->
            <template v-if="'remark' === column.dataIndex">
              <a-form-item class="form-item-single-line">
                <a-input v-if="editableData[record.id]"
                         placeholder="请输入备注"
                         v-model:value="editableData[record.id].remark"
                         allow-clear
                />
                <template v-else>
                  {{ text }}
                </template>
              </a-form-item>
            </template>
            <template v-if="column.key === 'action'">
              <!--            编辑列-->
              <template v-if="editableData[record.id]">
                <a-button type="link" size="small" html-type="submit" @click="handleSave(record.id)">
                  <template #icon>
                    <CheckOutlined />
                  </template>
                  保存
                </a-button>
                <a-divider type="vertical"/>
                <a-button type="link" size="small" danger @click="handleCancel(record.id)">
                  <template #icon>
                    <CloseOutlined />
                  </template>
                  取消
                </a-button>
              </template>
              <template v-else>
                <a-button type="link" size="small" @click="handleEdit(record)">
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
import {findList, save} from "@/api/system/dict/dictData";
import {nextTick, onMounted, reactive, ref} from "vue";
import type { UnwrapRef } from 'vue';
import {message} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';
import SortTable from 'sortablejs'

const props = defineProps<{
  typeId: string
}>()
// 初始化查询
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
// 初始化保存
const initSave = () => {
  const editableData:UnwrapRef<Record<string, SysDictDataType>> = reactive({})
  // 处理点击编辑
  const handleEdit = (data: SysDictDataType) => {
    if (data.id) {
      editableData[data.id] = cloneDeep(data)
    }
  }
  // 处理点击取消
  const handleCancel = (id: string) => {
    if (editableData[id]) {
      delete editableData[id]
    }
  }
  // 处理数据保存
  const handleSave = async (id: string) => {
    // 检查编辑数据是否存在
    if (!editableData[id]) {
      message.error("没有可编辑的数据");
      return;
    }

    const data = editableData[id];
    const original = dictDataList.value?.find(item => item.id === id);

    // 检查原始数据是否存在
    if (!original) {
      message.error("列表数据不存在");
      return;
    }

    // 确保有有效的 label 和 value
    if (!data?.label || !data?.value) {
      message.error("无效的编辑数据");
      return;
    }

    try {
      // 尝试保存数据
      const resp = await save(data);

      // 检查响应状态
      if (resp.code === 200) {
        // 更新原始数据
        original.label = data.label;
        original.value = data.value;
        original.remark = data.remark;

        // 取消编辑状态
        handleCancel(id);

        // 显示成功消息
        message.success(resp.msg);
      } else {
        // 显示错误信息
        message.error(resp.msg);
      }
    } catch (e) {
      // 捕获并处理错误
      console.error(e);
      message.error("保存数据时发生错误");
    }
  };

  return {
    editableData,
    handleEdit,
    handleCancel,
    handleSave
  }
}
const {editableData,handleEdit,handleCancel,handleSave} = initSave()
// 初始化拖动
const initDraggable = () => {
  const init = () => {
    const tbodyList = document.getElementsByClassName('ant-table-tbody')
    if (tbodyList) {
      const tbody = tbodyList[tbodyList.length - 1] as HTMLElement
      const data = SortTable.create(tbody,{
        draggable: '.ant-table-row',
        sort: true,
        animation: 150,
        handle: ".table-move-item",
        ghostClass: "sortable-ghost",
        chosenClass: "sortable-chosen",
        dragClass: "sortable-drag",
        onEnd: ({newIndex, oldIndex}) => {
          console.log(newIndex,oldIndex)
        }
      })
    }

  }
  init()
}

onMounted(() => {
  initDraggable()
})

</script>

<style>
.err-placeholder {
  .ant-input::placeholder {
    color: rgba(255, 77, 79, 0.7) !important;
  }
}
</style>
