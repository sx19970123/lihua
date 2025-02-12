<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--      检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="附件名称">
                <a-input placeholder="请输入附件名称" v-model:value="attachmentQuery.originalName" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="所属模块">
                <a-input placeholder="请输入所属模块" v-model:value="attachmentQuery.businessName" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="状态">
                <a-select placeholder="请选择状态" v-model:value="attachmentQuery.status" allowClear>
                  <a-select-option v-for="item in sys_attachment_status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="上传日期">
                <a-range-picker v-model:value="attachmentQuery.createTimeList" allowClear></a-range-picker>
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
      <a-table :pagination="false"
               :data-source="attachmentList"
               :row-selection="attachmentRowSelectionType"
               :custom-row="handleRowClick"
               :columns="attachmentColumn"
               row-class-name="hover-cursor-pointer"
               row-key="id"
               :scroll="{x: 1500}">
        <template #title>
          <a-flex :gap="8">
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          :open="openDeletePopconfirm"
                          ok-text="确 定"
                          cancel-text="取 消"
                          @confirm="handleDelete"
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
            <a-button ghost type="primary">
              <template #icon>
                <CloudDownloadOutlined />
              </template>
              打包下载
              <span v-if="selectedIds && selectedIds.length > 0" style="margin-left: 4px"> {{selectedIds.length}} 项</span>
            </a-button>
          </a-flex>
        </template>
        <template #bodyCell="{column, record}">
          <template v-if="column.key === 'createTime'">
            {{dayjs(record[column.key]).format('YYYY-MM-DD HH:mm')}}
          </template>
          <template v-if="column.key === 'status'">
            <dict-tag :dict-data-value="record[column.key]" :dict-data-option="sys_attachment_status"/>
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small">
              <template #icon>
                <CloudDownloadOutlined />
              </template>
              下载
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small">
              <template #icon>
                <ShareAltOutlined />
              </template>
              分享
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small" danger>
              <template #icon>
                <DeleteOutlined />
              </template>
              删除
            </a-button>
          </template>
        </template>
        <template #footer>
          <a-flex justify="flex-end">
            <a-pagination v-model:current="attachmentQuery.pageNum"
                          v-model:page-size="attachmentQuery.pageSize"
                          show-size-changer
                          :total="attachmentTotal"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="initPage"/>
          </a-flex>
        </template>
      </a-table>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

// 查询列表
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {ref} from "vue";
import type {SysAttachment, SysAttachmentDTO} from "@/api/system/attachment/type/SysAttachment.ts";
import {message} from "ant-design-vue";
import {ResponseError} from "@/api/global/Type.ts";
import {queryPage} from "@/api/system/attachment/Attachment.ts";
import dayjs from "dayjs";
import {initDict} from "@/utils/Dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
const  {sys_attachment_status, sys_attachment_upload_mode} = initDict("sys_attachment_status","sys_attachment_upload_mode")
const initSearch = () => {
// 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const attachmentRowSelectionType = {
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
  const handleRowClick = (record: SysAttachment) => {
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

  const attachmentColumn: ColumnsType = [
    {
      title: '附件名称',
      key: 'originalName',
      dataIndex: 'originalName'
    },
    {
      title: '附件类型',
      key: 'type',
      dataIndex: 'type'
    },
    {
      title: '存储路径',
      key: 'path',
      dataIndex: 'path',
    },
    {
      title: '所属模块',
      key: 'businessName',
      dataIndex: 'businessName',
      align: 'center',
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center',
    },
    {
      title: '上传时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center',
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '292px'
    }
  ]

  const reloadPage = async () => {
    attachmentQuery.value = {
      pageNum: 1,
      pageSize: 10
    }
    await initPage()
  }

  const attachmentQuery = ref<SysAttachmentDTO>({
    pageNum: 1,
    pageSize: 10
  })

  const attachmentTotal = ref<number>()
  const attachmentList = ref<Array<SysAttachment>>([])
  const tableLoad = ref<boolean>(false)

  const handleQueryPage = async () => {
    attachmentQuery.value.pageNum = 1
    await initPage()
  }

  const initPage = async () => {
    tableLoad.value = true
    try {
      const resp = await queryPage(attachmentQuery.value)
      if (resp.code === 200) {
        attachmentTotal.value = resp.data.total
        attachmentList.value = resp.data.records
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
  handleQueryPage()
  return {
    selectedIds,
    attachmentRowSelectionType,
    attachmentColumn,
    attachmentTotal,
    attachmentList,
    tableLoad,
    attachmentQuery,
    handleRowClick,
    handleQueryPage,
    initPage,
    reloadPage
  }
}

const {selectedIds, attachmentRowSelectionType ,attachmentColumn, attachmentTotal, attachmentList, tableLoad, attachmentQuery, handleRowClick, handleQueryPage, reloadPage, initPage} = initSearch()
</script>
