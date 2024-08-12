<template>
 <div>
   <a-flex :gap="16" vertical>
<!--     检索条件-->
     <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
      <a-form :colon="false">
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="日志描述">
              <a-input v-model:value="logQuery.description" placeholder="请输入日志描述"/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="操作人员">
              <a-input v-model:value="logQuery.createName" placeholder="请输入操作人员"/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="操作类型">
              <a-select v-model:value="logQuery.typeCode" placeholder="请选择" style="width: 120px">
                <a-select-option :value="item.value" v-for="item in logTypeOption">{{item.label}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="执行状态">
              <a-select v-model:value="logQuery.executeStatus" placeholder="请选择" style="width: 120px" allow-clear>
                <a-select-option :value="item.value" v-for="item in sys_log_status">{{item.label}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="操作时间">
              <a-range-picker allowClear v-model:value="logQuery.createTimeList"/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item>
              <a-space size="small">
                <a-button type="primary" @click="queryPage" :loading="tableLoad">
                  <template #icon>
                    <SearchOutlined />
                  </template>
                  查 询
                </a-button>
                <a-button :loading="tableLoad">
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
<!--     列表-->
     <a-table
         :pagination="false"
         :columns="logColumn"
         :data-source="logList"
         :loading="tableLoad"
         :row-selection="logRowSelectionType"
         row-class-name="hover-cursor-pointer"
         :custom-row="handleRowClick"
         row-key="id"
     >
      <template #bodyCell="{column,record,text}">
        <template v-if="column.key === 'executeStatus'">
          <dict-tag :dict-data-option="sys_log_status" :dict-data-value="text"></dict-tag>
        </template>
        <template v-if="column.key === 'createTime'">
          {{dayjs(text).format('YYYY-MM-DD HH:mm:ss')}}
        </template>
        <template v-if="column.key === 'executeTime'">
          {{text}} 毫秒
        </template>
      </template>
     </a-table>
   </a-flex>
 </div>
</template>

<script setup lang="ts">
import {initDict} from "@/utils/Dict"
import {ref} from "vue";
import {findPage, getLogTypeOption} from "@/api/system/log/Log.ts";
import {message} from "ant-design-vue";
import DictTag from "@/components/dict-tag/index.vue";
import type {SysLog, SysLogDTO} from "@/api/system/log/type/SysLog.ts";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import dayjs from "dayjs";
const {sys_log_status} = initDict("sys_log_status")


const initSearch = () => {
  // 操作类型选项
  const logTypeOption = ref<Array<{ value: string, label: string }>>([])
  const initLogTypeOption = async () => {
    const resp = await getLogTypeOption();
    if (resp.code === 200) {
      logTypeOption.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }
  initLogTypeOption()

  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const logRowSelectionType = {
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
  const handleRowClick = (record:SysLog) => {
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

  const logColumn: ColumnsType = [
    {
      title: '日志描述',
      key: 'description',
      dataIndex: 'description'
    },
    {
      title: '业务类型',
      key: 'typeMsg',
      dataIndex: 'typeMsg'
    },
    {
      title: '操作人员',
      key: 'createName',
      dataIndex: 'createName',
      align: 'center'
    },
    {
      title: 'ip 地址',
      key: 'ipAddress',
      dataIndex: 'ipAddress',
      align: 'center'
    },
    {
      title: '执行结果',
      key: 'executeStatus',
      dataIndex: 'executeStatus',
      align: 'center'
    },
    {
      title: '操作时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center'
    },
    {
      title: '操作耗时',
      key: 'executeTime',
      dataIndex: 'executeTime',
      align: 'center'
    }
  ]

  const logQuery = ref<SysLogDTO>({
    pageNum: 1,
    pageSize: 10
  })

  const logTotal = ref<number>()
  const logList = ref<Array<SysLog>>([])
  const tableLoad = ref<boolean>(false)

  const queryPage = async () => {
    logQuery.value.pageNum = 1
    await initPage()
  }

  const initPage = async () => {
    tableLoad.value = true
    const resp = await findPage(logQuery.value)
    if (resp.code === 200) {
      logTotal.value = resp.data.total
      logList.value = resp.data.records
      tableLoad.value = false
    } else {
      message.error(resp.msg)
    }
  }
  queryPage()
  return {
    logTypeOption,
    logColumn,
    tableLoad,
    logList,
    logQuery,
    logRowSelectionType,
    handleRowClick,
    queryPage
  }
}

const {logTypeOption, logColumn, tableLoad, logList, logQuery, logRowSelectionType, handleRowClick, queryPage} = initSearch()

</script>


<style scoped>

</style>