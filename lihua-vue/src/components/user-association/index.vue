<template>
  <div>
    <a-table :columns="userColumn"
             :data-source="userList"
             :pagination="false"
             :loading="listLoading"
             row-key="id">
      <template #title>
        <a-flex :gap="8">
          <a-button type="primary" :loading="modalActive.queryLoading" @click="handleShowUnAssociationUser">
            <template #icon>
              <PlusOutlined />
            </template>
            添 加
          </a-button>
        </a-flex>
      </template>
      <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'status'">
            <dict-tag :dict-data-value="text" :dict-data-option="sys_status"/>
        </template>
        <template v-if="column.key === 'action'">
          <a-popconfirm title="是否解绑该用户？" @confirm="handleDetach(record.id)">
            <a-button type="link" danger size="small">
              <template #icon>
                <DisconnectOutlined />
              </template>
              解 绑
            </a-button>
          </a-popconfirm>
        </template>
      </template>
      <template #footer>
        <a-flex justify="flex-end">
          <a-pagination v-model:current="userQuery.pageNum"
                        v-model:page-size="userQuery.pageSize"
                        :total="userTotal"
                        :show-total="(total:number) => `共 ${total} 条`"
                        @change="queryPage"
          />
        </a-flex>
      </template>
    </a-table>
    <a-modal v-model:open="modalActive.open"
             width="800px"
             :confirm-loading="modalActive.saveLoading"
             :title="modalActive.title"
             @ok="handleAssociate"
    >
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
        <a-table :columns="unAssociationUserColumn"
                 :data-source="unAssociationList"
                 :row-selection="roleRowSelection"
                 :custom-row="handleRowClick"
                 :loading="modalActive.queryLoading"
                 :pagination="false"
                 row-key="id"
        >
          <template #bodyCell = {column,text}>
            <template v-if="column.key === 'gender'">
              <dict-tag :dict-data-value="text" :dict-data-option="sys_gender"/>
            </template>
            <template v-if="column.key === 'status'">
              <dict-tag :dict-data-value="text" :dict-data-option="sys_status"/>
            </template>
          </template>
          <template #footer>
            <a-flex justify="flex-end">
              <a-pagination v-model:current="unAssociationQuery.pageNum"
                            v-model:page-size="unAssociationQuery.pageSize"
                            :total="unAssociationTotal"
                            :show-total="(total:number) => `共 ${total} 条`"
                            @change="queryUnassociatedUserPage"
              />
            </a-flex>
          </template>
        </a-table>
      <template #okText>
        {{selectedIds.length > 0 ? '绑定 ' + selectedIds.length + ' 位用户' : '绑 定'}}
      </template>
    </a-modal>
  </div>

</template>

<script setup lang="ts">
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {associate, detach, findAssociatedUserPage, findUnassociatedUserPage} from "@/api/system/user/user.ts";
import {ref, watch} from "vue";
import {message} from "ant-design-vue";
import DictTag from "@/components/dict-tag/index.vue";
import {initDict} from "@/utils/dict.ts";
const {sys_gender, sys_status} = initDict("sys_gender","sys_status")
const props = defineProps<{
  // 业务编码
  code: string,
  // 对应业务id
  id: string | undefined,
}>()

const initSearch = () => {
  // 绑定列表查询
  const userColumn: ColumnsType = [
    {
      title: '用户名',
      dataIndex: 'username',
      key: 'username'
    },
    {
      title: '用户昵称',
      dataIndex: 'nickname',
      key: 'nickname'
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      align: 'center',
      width: 100,
    },
    {
      title: '操作',
      key: 'action',
      width: 100,
      align: "center"
    }
  ]
  const userQuery = ref<AssociationUserDTO>({
    code: props.code,
    id: props.id as string,
    pageNum: 1,
    pageSize: 10
  })

  // 列表数据
  const userList = ref<Array<SysUser>>([])
  const userTotal = ref<number>()
  const listLoading = ref<boolean>(false)
  // 查询
  const queryPage = async () => {
    listLoading.value = true
    const resp = await findAssociatedUserPage(userQuery.value)
    if (resp.code === 200) {
      if (resp.data.records.length === 0 && userQuery.value.pageNum && userQuery.value.pageNum !== 1) {
        userQuery.value.pageNum = userQuery.value.pageNum - 1
        await queryPage()
      } else {
        userList.value = resp.data.records
        userTotal.value = resp.data.total
        listLoading.value = false
      }
    }
  }

  return {
    userColumn,
    userList,
    userTotal,
    userQuery,
    listLoading,
    queryPage
  }
}

const {userColumn, userList, userTotal, listLoading, userQuery, queryPage} = initSearch()

// 解绑
const handleDetach = async (userId: string) => {
  const data: AssociationUserDTO = {
    id: userQuery.value.id,
    code: userQuery.value.code,
    userId: userId
  }
  const resp = await detach(data)
  if (resp.code === 200) {
    message.success(resp.msg)
    await queryPage()
  } else {
    message.error(resp.msg)
  }
}

// 未绑定列表查询
const initUnAssociationSearch = () => {
  // 列表勾选
  const selectedIds = ref<string[]>([])
  const roleRowSelection = {
    columnWidth: '55px',
    type: 'checkbox',
    // 支持跨页
    preserveSelectedRowKeys: true,
    selectedRowKeys: selectedIds,
    // 全选
    onChange: (ids: Array<string>) => {
      selectedIds.value = ids
    }
  }
  // 点击行选中数据
  const handleRowClick = (record: SysUser) => {
    return {
      onClick: () => {
        const selected = selectedIds.value
        if (record.id) {
          if (selected.includes(record.id)) {
            selected.splice(selected.indexOf(record.id),1)
          } else {
            selected.push(record.id)
          }
        }
      }
    }
  }

  // 模态框属性
  type modalActiveType = {
    open: boolean,
    title: string,
    saveLoading: boolean,
    queryLoading: boolean,
  }
  const modalActive = ref<modalActiveType>({
    open: false,
    title: "绑定用户",
    saveLoading: false,
    queryLoading: false,
  })
  // 绑定列表查询
  const unAssociationUserColumn: ColumnsType = [
    {
      title: '用户名',
      dataIndex: 'username',
      key: 'username'
    },
    {
      title: '用户昵称',
      dataIndex: 'nickname',
      key: 'nickname'
    },
    {
      title: '性别',
      dataIndex: 'gender',
      key: 'gender',
      align: 'center',
      width: 60
    },
    {
      title: '手机号码',
      dataIndex: 'phoneNumber',
      key: 'phoneNumber',
      align: 'center',
      width: 100
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email'
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      align: 'center',
      width: 100
    },
  ]

  // 查询条件
  const unAssociationQuery = ref<AssociationUserDTO>({
    code: userQuery.value.code,
    id: userQuery.value.id,
    pageNum: 1,
    pageSize: 5
  })

  // 列表和全部用户总数
  const unAssociationList = ref<Array<SysUser>>([])
  const unAssociationTotal = ref<number>()

  // 打开选择用户模态框
  const handleShowUnAssociationUser = async () => {
    unAssociationQuery.value.pageNum = 1
    await queryUnassociatedUserPage()
    selectedIds.value = []
    modalActive.value.open = true
  }

  // 查询用户
  const queryUnassociatedUserPage = async () => {
    modalActive.value.queryLoading = true
    const resp = await findUnassociatedUserPage(unAssociationQuery.value)
    if (resp.code === 200) {
      unAssociationList.value = resp.data.records
      unAssociationTotal.value = resp.data.total
    } else {
      message.error(resp.msg)
    }
    modalActive.value.queryLoading = false
  }

  return {
    modalActive,
    unAssociationUserColumn,
    unAssociationList,
    unAssociationTotal,
    roleRowSelection,
    unAssociationQuery,
    selectedIds,
    queryUnassociatedUserPage,
    handleRowClick,
    handleShowUnAssociationUser
  }
}

const {modalActive, unAssociationUserColumn, unAssociationList, unAssociationTotal, roleRowSelection, unAssociationQuery, selectedIds, queryUnassociatedUserPage, handleRowClick, handleShowUnAssociationUser } = initUnAssociationSearch()

// 批量绑定
const handleAssociate = async () => {
  if (selectedIds.value.length === 0) {
    message.warn("请勾选要绑定的用户数据")
    return;
  }

  // 组合数据
  const data:AssociationUserDTO = {
    code: userQuery.value.code,
    id: userQuery.value.id,
    userIdList: selectedIds.value
  }

  // 调用接口
  modalActive.value.saveLoading = true
  const resp = await associate(data)
  if (resp.code === 200) {
    await queryPage()
    message.success(resp.msg)
    modalActive.value.open = false
  } else {
    message.error(resp.msg)
  }
  modalActive.value.saveLoading = false

}

// 传入id发生变化时重新查询列表
watch(() => props.id, (value) => {
  if (value) {
    userQuery.value.id = value
    unAssociationQuery.value.id = value
    queryPage()
  }
})
</script>
