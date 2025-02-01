<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--      检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="附件名称">
                <a-input placeholder="请输入附件名称"/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="存储路径">
                <a-input placeholder="请输入存储路径"/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="上传日期">
                <a-range-picker></a-range-picker>
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
      <a-table :pagination="false"
               :columns="attachmentColumn">
      </a-table>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

// 查询列表
import type {ColumnsType} from "ant-design-vue/es/table/interface";

const initSearch = () => {

  const attachmentColumn: ColumnsType = [
    {
      title: '附件名称',
      key: 'originalName',
      dataIndex: 'originalName'
    },
    {
      title: '后缀名',
      key: 'extensionName',
      dataIndex: 'extensionName'
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
    },
    {
      title: '上传状态',
      key: 'uploadStatus',
      dataIndex: 'uploadStatus'
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '190px'
    }
  ]

  return {
    attachmentColumn
  }
}

const {attachmentColumn} = initSearch()
</script>
