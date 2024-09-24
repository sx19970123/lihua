<template>
  <a-flex :gap="16" vertical>
    <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
      <a-form :colon="false">
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="用户名">
              <a-input placeholder="请输入用户名" v-model:value="queryParam.username"  allow-clear/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="用户昵称">
              <a-input placeholder="请输入用户昵称"  v-model:value="queryParam.nickname"  allow-clear/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item>
              <a-space size="small">
                <a-button type="primary" @click="queryList" :loading="queryLoading">
                  <template #icon>
                    <SearchOutlined />
                  </template>
                  查 询
                </a-button>
                <a-button @click="resetList" :loading="queryLoading">
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

    <a-table
      row-class-name="hover-cursor-pointer"
      row-key="cacheKey"
      :row-selection="userRowSelectionType"
      :custom-row="handleRowClick"
      :data-source="currentPage"
      :columns="userColumn"
      :pagination="false"
      :loading="queryLoading"
    >
      <template #title>
        <a-popconfirm title="是否强退选中用户？"
                      ok-text="确 定"
                      cancel-text="取 消"
                      @confirm="handleConfirm(undefined)"
        >
          <a-button danger>
            <template #icon>
              <DeleteOutlined />
            </template>
            强 退
            <span v-if="logoutCacheKeys && logoutCacheKeys.length > 0" style="margin-left: 4px"> {{logoutCacheKeys.length}} 项</span>
          </a-button>
        </a-popconfirm>
      </template>
      <template #bodyCell="{column,record,text}">
        <template v-if="column.key === 'cacheKey'">
          <a-tooltip placement="topLeft">
            <template #title>
              {{text}}
            </template>
            {{text}}
          </a-tooltip>
        </template>
        <template v-if="column.key === 'loginTime'">
          {{dayjs(text).format('YYYY-MM-DD HH:mm')}}
        </template>
        <template v-if="column.key === 'action'">
          <a-popconfirm title="是否强退该用户？"
                        placement="bottomRight"
                        ok-text="确 定"
                        cancel-text="取 消"
                        @confirm="handleConfirm(record.cacheKey)"
          >
            <a-button type="link" size="small" danger @click="(event:MouseEvent) => event.stopPropagation()">
              <template #icon>
                <DeleteOutlined />
              </template>
              强退
            </a-button>
          </a-popconfirm>
        </template>
      </template>
      <template #footer>
        <a-flex justify="flex-end">
          <a-pagination v-model:current="pagination.pageNum"
                        v-model:page-size="pagination.pageSize"
                        show-size-changer
                        :total="pagination.total"
                        :show-total="(total:number) => `共 ${total} 条`"
                        @change="changePage"/>
        </a-flex>
      </template>
    </a-table>
  </a-flex>
</template>

<script setup lang="ts">
import {ref} from "vue";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import type {LoggedUserType} from "@/api/monitor/logged-user/type/LoggedUserType.ts";
import {findList, forceLogout} from "@/api/monitor/logged-user/LoggedUser.ts";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
const initSearch = () => {
  // 选中的数据id集合
  const logoutCacheKeys = ref<Array<string>>([])
  // 列表勾选对象
  const userRowSelectionType = {
    columnWidth: '55px',
    type: 'checkbox',
    // 支持跨页勾选
    preserveSelectedRowKeys: true,
    // 指定选中key的数据集合，操作完后可手动清空
    selectedRowKeys: logoutCacheKeys,
    onChange: (keys: Array<string>) => {
      logoutCacheKeys.value = keys
    }
  }

  const handleRowClick = (record:LoggedUserType) => {
    return {
      onClick: () => {
        if (record.cacheKey) {
          const selected = logoutCacheKeys.value
          if (selected.includes(record.cacheKey)) {
            selected.splice(selected.indexOf(record.cacheKey),1)
          } else {
            selected.push(record.cacheKey)
          }
        }
      }
    }
  }

  const userColumn: ColumnsType = [
    {
      title: '缓存键值',
      key: 'cacheKey',
      dataIndex: 'cacheKey',
      ellipsis: true
    },
    {
      title: '用户名',
      key: 'username',
      dataIndex: 'username'
    },
    {
      title: '昵称',
      key: 'nickname',
      dataIndex: 'nickname'
    },
    {
      title: '登录ip',
      key: 'ip',
      dataIndex: 'ip',
      align: 'center'
    },
    {
      title: '登录时间',
      key: 'loginTime',
      dataIndex: 'loginTime',
      align: 'center'
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '190px'
    }
  ]

  // 检索条件
  const queryParam = ref<{
    username?: string,
    nickname?: string
  }>({})
  const queryLoading = ref<boolean>(false)
  // 全部数据
  const allDataList = ref<LoggedUserType[]>([])
  // 当前页数据
  const currentPage = ref<LoggedUserType[]>([])
  // 分页相关
  const pagination = ref<{
    pageNum: number,
    pageSize: number,
    total: number
  }>({
    pageNum: 1,
    pageSize: 10,
    total: 0
  })

  // 加载查询数据
  const initList = async () => {
    queryLoading.value = true
    const resp = await findList(queryParam.value.username, queryParam.value.nickname)
    if (resp.code === 200) {
      allDataList.value = resp.data
      pagination.value.total = resp.data.length
      // 每次执行 initList 后都从第一页开始
      changePage(pagination.value.pageNum, pagination.value.pageSize)
      queryLoading.value = false
    } else {
      message.error(resp.msg)
    }

  }

  // 点击查询按钮查询列表（页码从第一页开始）
  const queryList = () => {
    pagination.value.pageNum = 1
    initList()
  }

  // 重置查询（所有条件重置）
  const resetList = () => {
    pagination.value.pageNum = 1
    pagination.value.pageSize = 10
    queryParam.value = {}
    initList()
  }

  // 分页渲染数据
  const changePage = (page: number, pageSize: number) => {
    const startIndex = pageSize * page - pageSize
    const endIndex = pageSize * page
    currentPage.value = allDataList.value.slice(startIndex, endIndex)
  }

  initList()
  return {
    queryParam,
    currentPage,
    userColumn,
    pagination,
    queryLoading,
    userRowSelectionType,
    logoutCacheKeys,
    handleRowClick,
    initList,
    queryList,
    changePage,
    resetList
  }
}

const {queryParam, currentPage, userColumn, pagination, queryLoading, userRowSelectionType, logoutCacheKeys, handleRowClick, initList, queryList, changePage, resetList} = initSearch()

// 用户强制退出
const handleConfirm = async (cacheKey?: string) => {
  const targetLogoutCacheKeys = []
  if (cacheKey) {
    targetLogoutCacheKeys.push(cacheKey)
  } else {
    targetLogoutCacheKeys.push(... logoutCacheKeys.value)
  }

  if (targetLogoutCacheKeys.length === 0) {
    message.error("请选择数据")
    return
  }

  const resp = await forceLogout(targetLogoutCacheKeys);
  if (resp.code === 200) {
    await initList()
    message.success(resp.msg)
  } else {
    message.error(resp.msg)
  }
}
</script>

<style scoped>

</style>
