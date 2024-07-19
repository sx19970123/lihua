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
                <a-select placeholder="请选择" allow-clear>
                  <a-select-option v-for="item in sys_notice_type" v-model:value="noticeQuery.type" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="公告状态">
                <a-select placeholder="请选择" allow-clear>
                  <a-select-option v-for="item in sys_notice_status" v-model:value="noticeQuery.status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item>
                <a-space size="small">
                  <a-button type="primary">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button>
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
               :columns="noticeColumn"
               row-class-name="hover-cursor-pointer"
               :custom-row="handleRowClick"
               :pagination="false"
               row-key="id"
      >
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary">
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
  </div>
</template>
<script setup lang="ts">
import {initDict} from "@/utils/Dict.ts";
const {sys_notice_type, sys_notice_status} = initDict("sys_notice_type", "sys_notice_status")
import {ref} from "vue";
import type {SysNotice, SysNoticeDTO} from "@/api/system/noice/type/SysNotice.ts";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {findPage} from "@/api/system/noice/Notice.ts";
import {message} from "ant-design-vue";
const vModel = ref<string>()

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
      key: 'title',
      dataIndex: 'title'
    },
    {
      title: '公告状态',
      key: 'title',
      dataIndex: 'title'
    },
    {
      title: '优先级',
      key: 'title',
      dataIndex: 'title'
    },
    {
      title: '发送范围',
      key: 'title',
      dataIndex: 'title'
    },
    {
      title: '创建时间',
      key: 'title',
      dataIndex: 'title'
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '190px'
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
    tableLoad,
    noticeTotal,
    handleRowClick,
    queryPage,
    initPage,
    reloadPage,
  }
}

const {selectedIds, noticeRowSelectionType, noticeColumn, noticeQuery, tableLoad, noticeTotal, handleRowClick, queryPage, initPage, reloadPage} = initSearch()
</script>
<style scoped>

</style>