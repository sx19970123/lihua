<template>
  <div>
    <a-flex :gap="16" vertical>
<!--      检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="公告标题">
                <a-input placeholder="请输入公告标题" v-model:value="noticeQuery.title" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="公告类型">
                <a-select placeholder="请选择" v-model:value="noticeQuery.type" allow-clear>
                  <a-select-option v-for="item in sys_notice_type" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="公告状态">
                <a-select placeholder="请选择" v-model:value="noticeQuery.status" allow-clear>
                  <a-select-option v-for="item in sys_notice_status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
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
                  <a-button @click="reloadPage" :loading="tableLoad">
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
      <a-table :row-selection="noticeRowSelectionType"
               :data-source="noticeList"
               :columns="noticeColumn"
               row-class-name="hover-cursor-pointer"
               :custom-row="handleRowClick"
               :pagination="false"
               row-key="id"
      >
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary" @click="handleModalStatus('新增通知公告')">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          ok-text="确 定"
                          cancel-text="取 消">
              <a-button danger>
                <template #icon>
                  <DeleteOutlined />
                </template>
                删 除
                <span v-if="selectedIds && selectedIds.length > 0" style="margin-left: 4px"> {{selectedIds.length}} 项</span>
              </a-button>
            </a-popconfirm>
          </a-flex>
        </template>

        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'title'">
            <a-typography-link>
              {{text}}
            </a-typography-link>
          </template>
          <template v-if="column.key === 'type'">
            <dict-tag :dict-data-option="sys_notice_type" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'status'">
            <dict-tag :dict-data-option="sys_notice_status" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'priority'">
            <dict-tag :dict-data-option="sys_notice_priority" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'userScope'">
            <dict-tag :dict-data-option="sys_notice_user_scope" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'createTime'">
            {{dayjs(text).format('YYYY-MM-DD HH:mm')}}
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" >
              <template #icon>
                <EditOutlined />
              </template>
              编辑
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small" v-if="record.status === '1'">
              <template #icon>
                <Cancel />
              </template>
              撤销
            </a-button>
            <a-button type="link" size="small" v-else>
              <template #icon>
                <PublishAir />
              </template>
              发布
            </a-button>
            <a-divider type="vertical"/>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          placement="bottomRight"
                          ok-text="确 定"
                          cancel-text="取 消"
            >
              <a-button type="link" size="small" danger>
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
            <a-pagination v-model:current="noticeQuery.pageNum"
                          v-model:page-size="noticeQuery.pageSize"
                          :total="noticeTotal"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="initPage"/>
          </a-flex>
        </template>
      </a-table>
    </a-flex>
<!--    模态框-->
    <a-modal v-model:open="modalActive.open" width="900px">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
      <a-form :colon="false" ref="formRef" :model="sysNoticeVO" :label-col="{span: 2}">
        <a-form-item label="公告标题" :wrapper-col="{span: 8}">
          <a-input v-model:value="sysNoticeVO.title" placeholder="请输入标题"/>
        </a-form-item>
        <a-form-item label="优先程度" :wrapper-col="{span: 8}">
          <color-select v-model:value="sysNoticeVO.priority" :data-source="priorityOption"/>
        </a-form-item>
        <a-form-item label="公告类型">
          <a-radio-group v-model:value="sysNoticeVO.type">
            <a-radio v-for="item in sys_notice_type" :value="item.value">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="用户范围" :wrapper-col="{span: 8}">
          <a-radio-group v-model:value="sysNoticeVO.userScope">
            <a-radio v-for="item in sys_notice_user_scope" :value="item.value">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="指定用户" :wrapper-col="{span: 8}" v-if="sysNoticeVO.userScope === '1'">
          <a-flex :gap="6">
            <a-select/>
            <a-popover>
              <a-button>
                <template #icon>
                  <SearchOutlined />
                </template>
              </a-button>
            </a-popover>
          </a-flex>
        </a-form-item>
        <a-form-item label="内容">
          <editor height="300px" v-model="sysNoticeVO.content"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import {initDict} from "@/utils/Dict.ts";
import {reactive, ref, watch} from "vue";
import type {SysNotice, SysNoticeDTO, SysNoticeVO} from "@/api/system/noice/type/SysNotice.ts";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {findPage} from "@/api/system/noice/Notice.ts";
import DictTag from "@/components/dict-tag/index.vue"
import {Cascader, message} from "ant-design-vue";
import dayjs from "dayjs";
import Editor from "@/components/editor/index.vue"
import ColorSelect from "@/components/color-select/index.vue"
import {getUserOption} from "@/api/system/user/User.ts";
import type {CommonTree} from "@/api/global/Type.ts";

const {sys_notice_type, sys_notice_status, 	sys_notice_user_scope, sys_notice_priority} = initDict("sys_notice_type", "sys_notice_status", "sys_notice_user_scope", "sys_notice_priority")
// 查询列表
const initSearch = () => {
  // 列表多选
  const selectedIds = ref<Array<string>>([])
  const noticeRowSelectionType = {
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
  // 处理选择
  const handleRowClick = (record: SysNotice) => {
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

  // 列
  const noticeColumn: ColumnsType = [
    {
      title: '公告标题',
      key: 'title',
      dataIndex: 'title'
    },
    {
      title: '公告类型',
      key: 'type',
      dataIndex: 'type',
      align: 'center'
    },
    {
      title: '公告状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center'
    },
    {
      title: '发送范围',
      key: 'userScope',
      dataIndex: 'userScope',
      align: 'center'
    },
    {
      title: '优先级',
      key: 'priority',
      dataIndex: 'priority',
      align: 'center'
    },
    {
      title: '备注',
      key: 'remark',
      dataIndex: 'remark',
      ellipsis: true
    },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center',
      width: '150px'
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '270px'
    }
  ]

  // 查询条件
  const noticeQuery = ref<SysNoticeDTO>({
    pageNum: 1,
    pageSize: 10,
  })

  const noticeTotal = ref<number>()
  const noticeList = ref<Array<SysNotice>>([])
  const tableLoad = ref<boolean>(false)

  const queryPage = async () => {
    noticeQuery.value.pageNum = 1
    await initPage()
  }

  const reloadPage = async () => {
    noticeQuery.value = {
      pageNum: 1,
      pageSize: 10
    }
    await initPage()
  }

  const initPage = async () => {
    tableLoad.value = true
    const resp = await findPage(noticeQuery.value)
    if (resp.code === 200) {
      noticeList.value = resp.data.records
      noticeTotal.value = resp.data.total
      tableLoad.value = false
    } else {
      message.error(resp.msg)
    }
  }
  queryPage()
  return {
    selectedIds,
    noticeRowSelectionType,
    noticeColumn,
    noticeQuery,
    noticeList,
    tableLoad,
    noticeTotal,
    handleRowClick,
    queryPage,
    initPage,
    reloadPage,
  }
}
const {selectedIds, noticeRowSelectionType, noticeColumn, noticeQuery, noticeList, tableLoad, noticeTotal, handleRowClick, queryPage, initPage, reloadPage} = initSearch()

// 表单保存
const initSave = () => {

  // 模态框状态
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string // 模态框标题
  }

  const modalActive = reactive<modalActiveType>({
    open: false,
    saveLoading: false,
    title: ""
  })

  // 将优先级选项处理为color-select组件可以处理的数据类型
  const priorityOption = ref<Array<{
    name: string,
    color: string,
    key?: string
  }>>([])
  // 处理优先级字典数据
  const handlePriorityOption = () => {
    const dictPriority = sys_notice_priority.value
    dictPriority.forEach(item => {
      priorityOption.value.push({
        name: item.label ? item.label : '',
        color: item.tagStyle ? item.tagStyle : '',
        key: item.value,
      })
    })
  }

  // 用户选项
  const userOption = ref<Array<CommonTree>>([])
  const handleUserOption = async () => {
    const resp = await getUserOption()
    if (resp.code === 200) {
      userOption.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }
  handleUserOption()

  const searchValue = ref<string>('')

  const sysNoticeVO = ref<SysNoticeVO>({
    type: '0',
    status: '0',
    priority: '2',
    userScope: '0'
  })

  // 处理模态框状态
  const handleModalStatus = (title?: string) => {
    modalActive.open = !modalActive.open
    if(modalActive.open && priorityOption.value.length === 0) {
      handlePriorityOption()
    }
    if (title) {
      modalActive.title = title
    }
  }
  return {
    modalActive,
    priorityOption,
    sysNoticeVO,
    userOption,
    searchValue,
    handleModalStatus,
    handlePriorityOption
  }
}
const {modalActive, priorityOption, sysNoticeVO, userOption, searchValue, handleModalStatus, handlePriorityOption} = initSave()

watch(() => searchValue, ()=> {
  console.log(searchValue)
})
</script>
<style scoped>

</style>