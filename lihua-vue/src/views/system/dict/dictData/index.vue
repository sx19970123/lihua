<template>
  <a-flex vertical :gap="16">
    <a-card style="margin-top: 24px;" :body-style="{'padding-bottom': '0'}">
      <a-form :colon="false">
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="标签">
              <a-input v-model:value="dictDataQuery.label" allow-clear placeholder="请输入字典标签"/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="值">
              <a-input v-model:value="dictDataQuery.value" allow-clear placeholder="请输入字典值"/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="状态">
              <a-select v-model:value="dictDataQuery.status" allow-clear placeholder="请选择">
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="1">停用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item>
              <a-space size="small">
                <a-button type="primary"
                          @click="handleQueryList"
                          :loading="tableLoading"
                >
                  <template #icon>
                    <SearchOutlined />
                  </template>
                  查 询
                </a-button>
                <a-button @click="resetList"
                          :loading="tableLoading"
                >
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
    <a-card :body-style="{padding: 0}" :style="{'border-bottom': 'none'}">
        <a-table
            :scroll="{ x: 1050 }"
            :columns="dictDataColumn"
            :data-source="dictDataList"
            :loading="tableLoading"
            :pagination="false"
            v-model:expandedRowKeys="expandedRowKeys"
            @resizeColumn="handleResizeColumn"
            rowKey="id"
            sticky
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
                  :class="record.id"
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
                  @change="() => { if (editableData[record.id].dictTypeCode === 'sys_dict_tag_style') editableData[record.id].tagStyle = editableData[record.id].value }"
              />
              <!--当编辑字典为标签样式时，调用change方法，设置tagStyle值为value-->
              <template v-else>
                {{ text }}
              </template>
            </template>
            <!--          标签样式-->
            <template v-if="'tagStyle' === column.dataIndex">
              <!--当编辑字典为标签样式时，不展示选择框，进行标签样式预览-->
              <template v-if="editableData[record.id] && editableData[record.id].dictTypeCode === 'sys_dict_tag_style'">
                <a-tag :color="editableData[record.id].value">{{editableData[record.id].label}}</a-tag>
              </template>
              <template v-else-if="editableData[record.id] && editableData[record.id].dictTypeCode !== 'sys_dict_tag_style'">
                <a-select v-model:value="editableData[record.id].tagStyle" placeholder="请选择">
                  <a-select-option :value="item.value" v-for="item in sys_dict_tag_style">
                    <a-tag :color="item.value" :bordered="false">{{item.label}}</a-tag>
                  </a-select-option>
                </a-select>
              </template>
              <template v-else>
                <dict-tag :dict-data-value="text" :dict-data-option="sys_dict_tag_style"/>
              </template>
            </template>
            <!--          状态-->
            <template v-if="'status' === column.dataIndex">
              <a-select v-if="editableData[record.id]"  v-model:value="editableData[record.id].status">
                <a-select-option :value="item.value" v-for="item in sys_status">{{item.label}}</a-select-option>
              </a-select>
              <template v-else>
                <dict-tag :dict-data-value="text" :dict-data-option="sys_status"/>
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
                  保存
                </a-button>
                <a-divider type="vertical"/>
                <a-button type="link" size="small" danger @click="handleCancel(record.id, true)">
                  <template #icon>

                  </template>
                  取消
                </a-button>
              </template>
              <template v-else>
                <a-button type="link" size="small" @click="handleEdit(record)">
                  编辑
                </a-button>
                <template v-if="props.type === '1'">
                  <a-divider type="vertical"/>
                  <a-button type="link" size="small" @click="handleAddChildren(record)">
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
import {deleteData, queryList, save} from "@/api/system/dict/DictData.ts";
import {nextTick, reactive, ref} from "vue";
import type { UnwrapRef } from 'vue';
import {message} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';
import {initDict, reLoadDict} from "@/utils/Dict.ts";
import dictTag from "@/components/dict-tag/index.vue"
import type {SysDictDataType, SysDictDataTypeDTO} from "@/api/system/dict/type/SysDictDataType.ts";
import {ResponseError} from "@/api/global/Type.ts";
import {v4 as uuidv4} from "uuid";
import settings from "@/settings.ts";

const props = defineProps<{
  typeCode: string,
  type: string
}>()

const {sys_status,sys_dict_tag_style} = initDict("sys_status","sys_dict_tag_style")

// 查询
const initSearch = () => {
  // 定义表头
  const dictDataColumn = ref<ColumnsType>([
    {
      title: '标签',
      dataIndex: 'label',
      key: 'label',
      resizable: true,
      width: 200,
      maxWidth: 300,
      minWidth: 150
    },
    {
      title: '值',
      dataIndex: 'value',
      key: 'value',
    },
    {
      title: '样式',
      dataIndex: 'tagStyle',
      key: 'tagStyle',
      align: 'center',
      width: 110
    },
    {
      title: '状态',
      dataIndex: 'status',
      align: 'center',
      key: 'status',
      width: 100
    },
    {
      title: '排序',
      dataIndex: 'sort',
      align: 'center',
      key: 'sort',
      width: 106
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
      width: props.type === '1' ? 238 : 148,
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    },
  ])
  // 定义查询条件对象
  const dictDataQuery = ref<SysDictDataTypeDTO>({dictTypeCode: props.typeCode,type: props.type})
  // 定义查询出的列表集合
  const dictDataList = ref<Array<SysDictDataType>>([])
  // 默认展开
  const expandedRowKeys = ref<Array<string>>([])
  // 定义列表加载
  const tableLoading = ref<boolean>(false)
  // 查询列表
  const handleQueryList = async () => {
    tableLoading.value = true
    try {
      const resp = await queryList(dictDataQuery.value)
      if (resp.code === 200) {
        dictDataList.value = resp.data
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
      tableLoading.value = false
    }
  }

  // 重置列表查询
  const resetList = () => {
    dictDataQuery.value.dictTypeCode = props.typeCode
    dictDataQuery.value.value = undefined
    dictDataQuery.value.label = undefined
    dictDataQuery.value.status = undefined
    handleQueryList()
  }

  const handleResizeColumn = (w: number, col: { width: number }) => {
    col.width = w;
  }
  handleQueryList()
  return {
    dictDataQuery,
    dictDataColumn,
    dictDataList,
    handleQueryList,
    resetList,
    tableLoading,
    expandedRowKeys,
    handleResizeColumn
  }
}
const {dictDataQuery, dictDataColumn, dictDataList, handleQueryList, resetList, tableLoading, expandedRowKeys,handleResizeColumn} = initSearch()

// 新增/新增下级
const initAdd = () => {
  // 编辑中数据
  const editableData:UnwrapRef<Record<string, SysDictDataType>> = reactive({})

  // 处理新增
  const handleAdd = async () => {
    const tempId = generateTempId()
    console.log(tempId)
    // 新增默认数据
    const item: SysDictDataType = {
      id: tempId,
      status: '0',
      sort: generateDefaultSort(dictDataList.value),
      parentId: '0',
      tagStyle: 'default',
      dictTypeCode: props.typeCode
    }
    // 添加到集合
    dictDataList.value.push(item)
    handleEdit(item)
    // 自动滚动
    await autoRoll(tempId)
  }

  // 处理新增子集
  const handleAddChildren = async (data: SysDictDataType) => {
    if (!data.children) {
      data.children = []
    }
    const tempId = generateTempId()
    const item = {
      id: tempId,
      status: '0',
      sort: generateDefaultSort(data.children),
      dictTypeCode: props.typeCode,
      parentId: data.id,
      tagStyle: 'default',
    }

    data.children.push(item)
    handleEdit(item)
    // 展开数据
    if (data.id) {
      expandedRowKeys.value.push(data.id)
    }
    // 自动滚动
    await autoRoll(tempId)
  }

  /**
   * 点击新增自动滚动到执行输入框
   * @param targetClass
   */
  const autoRoll = async (targetClass: string) => {
    await nextTick(() => {
      const doc = document.querySelector('.' + targetClass)
      if (doc) {
        doc.scrollIntoView({behavior: 'smooth',block: 'nearest'})
      }
    })
  }

  // 处理点击编辑
  const handleEdit = (data: SysDictDataType) => {
    if (data.id) {
      editableData[data.id] = cloneDeep(data)
      handleTreeIconFlex(data.id)
    }
  }

  // 处理点击取消
  const handleCancel = (id: string,deleteData: boolean) => {
    if (editableData[id]) {
      delete editableData[id]
    }

    if (deleteData && checkIsTempId(id)) {
      handleDelete(id)
    }

    if (Object.keys(editableData).length === 0) {
      recoverTreeIcon(id)
    }
  }

  // 生成临时序号
  const generateDefaultSort = (list: Array<SysDictDataType>): number => {
    if (!list) {
      return 1
    }

    const last = list[list.length - 1]
    if (last && last.sort) {
      return last.sort + 1
    }

    return 1
  }
  // 生成临时id
  const generateTempId = () => {
    return 'add-' + uuidv4()
  }
  // 检查是否为临时id
  const checkIsTempId = (id: string): boolean => {
    return /^add-/.test(id);
  }

  // 解决在树形表格编辑下，input框分行的问题
  const handleTreeIconFlex = (id: string) => {
    // dom 元素挂载完成执行
    nextTick(() => {
      const tdElements = document.getElementsByClassName('ant-table-cell-with-append');
      if (tdElements) {
        for (let i = 0; i < tdElements.length; i++) {
          const td = tdElements[i] as HTMLElement;
          const targetClassList = td.getElementsByClassName(id)
          const btnList = td.getElementsByTagName("button")
          if (targetClassList.length > 0) {
            td.style.display = 'flex';
            td.style.alignItems = 'center';
          }
          if (btnList && btnList.length == 1) {
            btnList[0].style.paddingRight = '15px'
          }
        }
      }
    })
  }
  // 编辑完成后，恢复原有样式
  const recoverTreeIcon = (id:string) => {
    const tdElements = document.getElementsByClassName('ant-table-cell-with-append');
    if (tdElements) {
      for (let i = 0; i < tdElements.length; i++) {
        const td = tdElements[i] as HTMLElement;
        const targetClassList = td.getElementsByClassName(id)
        const btnList = td.getElementsByTagName("button")
        if (targetClassList.length > 0) {
          td.style.display = '';
          td.style.alignItems = '';
        }
        if (btnList && btnList.length == 1) {
          btnList[0].style.paddingRight = '0'
        }
      }
    }
  }

  return {
    editableData,
    handleAdd,
    handleEdit,
    handleCancel,
    checkIsTempId,
    handleAddChildren
  }
}
const {editableData,handleAdd,handleEdit,handleCancel,checkIsTempId,handleAddChildren} = initAdd()
// 保存方法
const initSave = () => {
  // 保存数据
  const handleSave = async (id: string) => {
    // 检查是否存在可编辑的数据
    if (!editableData[id]) {
      message.error("没有可编辑的数据");
      return;
    }

    const data = editableData[id];

    // 确保有有效的 label 和 value
    if (!data?.label || !data?.value || !data?.sort) {
      message.error("请将数据填写完整");
      return;
    }
    if (!data?.dictTypeCode) {
      message.error("数据类型编码为空");
      return;
    }

    try {
      // 如果是临时ID，将其设置为undefined
      if (data.id && checkIsTempId(data.id)) {
        data.id = undefined;
      }

      tableLoading.value = true;

      // 保存数据
      const resp = await save(data);

      if (resp.code === 200) {
        // 保存成功的处理
        data.id = resp.data;
        // 新增的数据保存到列表
        handleDeepSave(id, dictDataList.value, data);
        // 关闭编辑框
        handleCancel(id,false)
        // 重新排序
        handleSort(dictDataList.value)
        // 重载字典数据
        handleReloadDictData(data.dictTypeCode)
        message.success(resp.msg);
      } else {
        // 保存失败的处理
        message.error(resp.msg);
      }
    } catch (error) {
      // 错误处理
      console.error(error);
      message.error("保存数据时发生错误");
    } finally {
      tableLoading.value = false;
    }
  };

  // 保存时，当修改的字典数据为当前用到的字典数据（sys_dict_tag_style）时，重新加载字典数据
  const handleReloadDictData = (dictTypeCode: string) => {
    if (dictTypeCode === "sys_dict_tag_style") {
      reLoadDict(dictTypeCode).then(resp => {
        sys_dict_tag_style.value = resp as Array<SysDictDataType>
      })
    }
  }

  // 保存成功后不重新加载列表，而是修改列表中的数据
  const handleDeepSave = (id: string, list: SysDictDataType[], data: SysDictDataType) => {
    for (let item of list) {
      if (item.id === id) {
        for (let dataKey in data) {
          (item as any)[dataKey] = (data as any)[dataKey];
        }
        // 匹配到了对应的项，不需要继续遍历其子项
        return;
      }
      if (item.children && item.children.length > 0) {
        // 使用类型断言告诉 TypeScript 对象的确切类型
        handleDeepSave(id, item.children as SysDictDataType[], data);
      }
    }
  };


  return {
    handleSave
  }
}
const { handleSave } = initSave()
// 删除方法
const initDelete = () => {
  // 处理删除
  const handleDelete = async (id: string) => {
    const list = dictDataList.value
    // 新增未保存的id直接删除前端数据
    if (checkIsTempId(id)) {
      handleDeleteTableData(id,list)
      message.success("成功")
    } else {
      try {
        // 其余数据从库中删除
        const resp = await deleteData([id])
        if (resp.code === 200) {
          handleDeleteTableData(id,list)
          message.success(resp.msg)
        } else {
          message.error(resp.msg)
        }
      } catch (e) {
        if (e instanceof ResponseError) {
          message.error(e.msg)
        } else {
          console.log(e)
        }
      }
    }
    handleSort(list)
  }

  // 处理删除集合中的数据
  const handleDeleteTableData = (id: string, list: SysDictDataType[]) => {
    for (let i = 0; i < list.length; i++) {
      const item = list[i];
      if (item.id === id) {
        list.splice(i, 1);
        // i--; // 减小 i 因为数组长度已经减小了
      } else if (item.children && item.children.length > 0) {
        handleDeleteTableData(id, item.children);
      }
      // 递归回调如果没有子集的话设置子集为 undefined
      if (item.children && item.children.length === 0) {
        item.children = undefined;
      }
    }
  };

  return {
    handleDelete
  }
}
const { handleDelete } = initDelete()
// 处理排序
const handleSort = (list: SysDictDataType[]) => {
  if (list) {
    list.forEach(item => {
      if (item.children && item.children.length > 0) {
        handleSort(item.children);
      }
    })
    list.sort((a, b) => {
      if (a.sort !== undefined && b.sort !== undefined) {
        return a.sort - b.sort;
      } else if (a.sort === undefined && b.sort !== undefined) {
        return 1; // a.sort 为 undefined，排到后面
      } else if (a.sort !== undefined && b.sort === undefined) {
        return -1; // b.sort 为 undefined，排到前面
      } else {
        return 0; // 两者都为 undefined，保持原有顺序
      }
    });
  }
}

</script>

<!--suppress CssUnresolvedCustomProperty -->
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

