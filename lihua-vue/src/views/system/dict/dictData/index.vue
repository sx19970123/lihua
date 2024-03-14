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
          <a-form-item class="form-item-single-line" label="状态">
            <a-select v-model:value="dictDataQuery.status" allow-clear placeholder="请选择状态">
              <a-select-option value="0">正常</a-select-option>
              <a-select-option value="1">停用</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item  class="form-item-single-line">
            <a-button type="primary"
                      @click="queryList"
                      :loading="tableLoading"
            >
              <template #icon>
                <SearchOutlined />
              </template>
              查 询
            </a-button>
          </a-form-item>
          <a-form-item  class="form-item-single-line">
            <a-button @click="resetList"
                      :loading="tableLoading"
            >
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
               :loading="tableLoading"
               :pagination="false"
               rowKey="id"
               indentSize="20px"
               row-class-name="draggable-table-row"
        >
          <template #title>
            <a-button type="primary" @click="handleAdd">
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
                {{ text }}
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
            <!--          状态-->
            <template v-if="'status' === column.dataIndex">
              <a-select v-if="editableData[record.id]"  v-model:value="editableData[record.id].status">
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="1">停用</a-select-option>
              </a-select>
              <template v-else>
                <template v-if="record.status === '0'">
                  <a-tag color="success">正常</a-tag>
                </template>
                <template v-if="record.status === '1'">
                  <a-tag color="error">停用</a-tag>
                </template>
              </template>
            </template>
            <!--          排序-->
            <template v-if="'sort' === column.dataIndex">
              <a-input-number
                  v-if="editableData[record.id]"
                  class="err-placeholder"
                  placeholder="请输入排序值"
                  :status="editableData[record.id].sort?'':'error'"
                  v-model:value="editableData[record.id].sort"
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
                <template v-if="props.type === '1'">
                  <a-divider type="vertical"/>
                  <a-button type="link" size="small" @click="addChildren(record)">
                    <template #icon>
                      <VerticalAlignBottomOutlined />
                    </template>
                    添加下级
                  </a-button>
                </template>
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
          </template>
        </a-table>
    </a-card>
  </a-flex>
</template>

<script setup lang="ts">
// 接收父组件传入的typeId
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {deleteData, findList, save} from "@/api/system/dict/dictData";
import {reactive, ref} from "vue";
import type { UnwrapRef } from 'vue';
import {message} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';

const props = defineProps<{
  typeId: string,
  type: string
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
      title: '状态',
      dataIndex: 'status',
      align: 'center',
      key: 'status',
      width: '100px'
    },
    {
      title: '排序',
      dataIndex: 'sort',
      align: 'center',
      key: 'sort',
      width: '100px'
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
      width: props.type === '1' ? '294px' : '182px'
    },
  ]
  // 定义查询条件对象
  const dictDataQuery = ref<SysDictDataQueryType>({dictTypeId: props.typeId,type: props.type})
  // 定义查询出的列表集合
  const dictDataList = ref<Array<SysDictDataType>>()
  // 定义列表加载
  const tableLoading = ref<boolean>(false)
  // 查询列表
  const queryList = async () => {
    tableLoading.value = true
    const resp = await findList(dictDataQuery.value)
    if (resp.code === 200) {
      dictDataList.value = resp.data
    } else {
      message.error(resp.msg)
    }
    tableLoading.value = false
  }

  // 重置列表查询
  const resetList = () => {
    dictDataQuery.value.dictTypeId = props.typeId
    dictDataQuery.value.value = undefined
    dictDataQuery.value.label = undefined
    dictDataQuery.value.status = undefined
    queryList()
  }

  queryList()
  return {
    dictDataQuery,
    dictDataColumn,
    dictDataList,
    queryList,
    resetList,
    tableLoading
  }
}
const {dictDataQuery, dictDataColumn, dictDataList, queryList, resetList, tableLoading} = initSearch()

// 初始化保存
const initSave = () => {
  const editableData:UnwrapRef<Record<string, SysDictDataType>> = reactive({})

  // 处理新增
  const handleAdd = () => {
    // 新增默认数据
    const item = {
      id: 'add-' + dictDataList.value?.length,
      status: '0',
      sort: dictDataList.value?.length? dictDataList.value?.length + 1 : 1,
      dictTypeId: props.typeId
    }
    dictDataList.value?.push(item)
    handleEdit(item)
  }

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

    // 确保有有效的 label 和 value
    if (!data?.label || !data?.value) {
      message.error("无效的编辑数据");
      return;
    }

    try {
      // 保存前处理数据
      if (data.id && /^add-/.test(data.id)) {
        data.id = undefined
      }
      tableLoading.value = true
      // 尝试保存数据
      const resp = await save(data);

      // 检查响应状态
      if (resp.code === 200) {
        // 取消编辑状态
        handleCancel(id);
        // 重新查询数据
        await queryList()
        // 显示成功消息
        message.success(resp.msg);
      } else {
        // 显示错误信息
        message.error(resp.msg);
      }
      tableLoading.value = false
    } catch (e) {
      // 捕获并处理错误
      console.error(e);
      message.error("保存数据时发生错误");
    }
  };

  return {
    editableData,
    handleAdd,
    handleEdit,
    handleCancel,
    handleSave
  }
}
const {editableData,handleAdd,handleEdit,handleCancel,handleSave} = initSave()

// 初始化子数据
const initChildren = () => {
  // 添加子集
  const addChildren = (data: SysDictDataType) => {
    // 对象中没有children 的话，为 children 赋值
    if (!data.children) {
      data.children = []
    }

    const item = {
      id: 'add-' + data.children?.length,
      status: '0',
      sort: data.children?.length? data.children?.length + 1 : 1,
      dictTypeId: props.typeId,
      parentId: data.id
    }
    data.children.push(item)
    handleEdit(item)
  }

  return {
    addChildren
  }
}

const {addChildren} = initChildren()

// 处理删除
const handleDelete = async (id: string) => {
  // 新增未保存的id直接删除前端数据
  if (/^add-/.test(id)) {
    const list = dictDataList.value
    list?.splice(list?.findIndex(item => item.id === id),1)
    message.success("成功")
  } else {
    tableLoading.value = true
    // 其余数据从库中删除
    const resp = await deleteData([id])
    if (resp.code === 200) {
      message.success(resp.msg)
      await queryList()
    } else {
      message.error(resp.msg)
    }
    tableLoading.value = false
  }
}

</script>

<style>
.ant-table-tbody > tr.target > td {
  border-top: 2px var(--colorPrimary) solid !important;
}
.err-placeholder {
  .ant-input-number-input::placeholder,
  .ant-input::placeholder {
    color: rgba(255, 77, 79, 0.7) !important;
  }
}
</style>

