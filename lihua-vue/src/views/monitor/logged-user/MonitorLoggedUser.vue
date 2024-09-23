<template>
  <a-flex :gap="16" vertical>
    <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
      <a-form :colon="false">
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="用户名">
              <a-input placeholder="请输入用户名"  allow-clear/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="用户昵称">
              <a-input placeholder="请输入用户昵称"  allow-clear/>
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

    <a-table
      row-class-name="hover-cursor-pointer"
      row-key="id"
      :data-source="currentPage"
      :columns="userColumn"
      :pagination="false"
    >
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
import {findList} from "@/api/monitor/logged-user/LoggedUser.ts";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
const initSearch = () => {
  // 选中的数据id集合
  const cacheKeys = ref<Array<string>>([])
  // 列表勾选对象
  const userRowSelectionType = {
    columnWidth: '55px',
    type: 'checkbox',
    // 支持跨页勾选
    preserveSelectedRowKeys: true,
    // 指定选中key的数据集合，操作完后可手动清空
    selectedRowKeys: cacheKeys,
    onChange: (keys: Array<string>) => {
      cacheKeys.value = keys
    }
  }

  const handleRowClick = (record:LoggedUserType) => {
    return {
      onClick: () => {
        if (record.cacheKey) {
          const selected = cacheKeys.value
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

  const allDataList = ref<LoggedUserType[]>([])
  const currentPage = ref<LoggedUserType[]>([])
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
    const resp = await findList()

    if (resp.code === 200) {
      allDataList.value = resp.data
      pagination.value.total = resp.data.length
      // 每次执行 initList 后都从第一页开始
      changePage(1, pagination.value.pageSize)
    } else {
      message.error(resp.msg)
    }

  }

  // 分页渲染数据
  const changePage = (page: number, pageSize: number) => {
    const startIndex = pageSize * page - pageSize
    const endIndex = pageSize * page
    currentPage.value = allDataList.value.slice(startIndex, endIndex)
  }

  initList()
  return {
    currentPage,
    userColumn,
    pagination,
    changePage,
    initList
  }
}

const {currentPage, userColumn, pagination, changePage, initList} = initSearch()
</script>

<style scoped>

</style>
