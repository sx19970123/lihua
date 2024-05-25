<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--      筛选条件-->
      <a-card :style="{border: 'none'}">
        <a-form :colon="false">
          <a-row :span="24" :gutter="16">
            <a-col :span="6">
              <a-form-item label="部门">
                <a-tree-select placeholder="请选择部门"
                               :tree-data="option"
                               v-model:value="userQuery.deptIdList"
                                :fieldNames="{children:'children', label:'name', value: 'id' }"
                               allowClear/>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="昵称">
                <a-input placeholder="请输入昵称" v-model:value="userQuery.nickname" allowClear/>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="用户名">
                <a-input placeholder="请输入用户名" v-model:value="userQuery.username" allowClear/>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="手机号码">
                <a-input placeholder="请输入手机号码" v-model:value="userQuery.phoneNumber" allowClear/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :span="24" :gutter="16">
            <a-col :span="6">
              <a-form-item label="创建时间" class="form-item-single-line">
                <a-range-picker allowClear v-model:value="userQuery.createTimeList"/>
              </a-form-item>
            </a-col>
            <a-col :span="3">
              <a-form-item label="状态"  class="form-item-single-line" >
                <a-select placeholder="请选择" v-model:value="userQuery.status">
                  <a-select-option :value="item.value" v-for="item in sys_status" allowClear>{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item  class="form-item-single-line">
                <a-space size="small">
                  <a-button type="primary">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button >
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
      <!--    表格-->
      <a-table :columns="userColumn" :data-source="userList">
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary" @click="handleModelStatus('新增用户')">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
          </a-flex>
        </template>
        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'deptLabelList'">
            {{text.join('、')}}
          </template>
          <template v-if="column.key === 'createTime'">
            {{dayjs(text).format('YYYY-MM-DD HH:mm')}}
          </template>
          <template v-if="column.key === 'status'">
            <dict-tag :dict-data-value="text" :dict-data-option="sys_status"/>
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small">
              <template #icon>
                <EditOutlined />
              </template>
              编辑
            </a-button>
            <a-divider type="vertical"/>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          ok-text="确 定"
                          cancel-text="取 消"
            >
              <a-button type="link" danger size="small" @click="(event: any) => event.stopPropagation()">
                <template #icon>
                  <DeleteOutlined />
                </template>
                删除
              </a-button>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </a-flex>

    <a-modal v-model:open="modalActive.open">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>

      <a-form>
        <a-row :gutter="16">
          <a-col :span="12">

          </a-col>
          <a-col :span="12">

          </a-col>
        </a-row>
        <a-form-item label="用户名">

        </a-form-item>
        <a-form-item label="昵称">

        </a-form-item>
        <a-form-item label="性别">

        </a-form-item>
        <a-form-item label="状态">

        </a-form-item>
        <a-form-item label="邮箱">

        </a-form-item>
        <a-form-item label="手机号码">

        </a-form-item>
        <a-form-item label="角色">

        </a-form-item>
        <a-form-item label="部门">

        </a-form-item>
        <a-form-item label="岗位">

        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">

// 列表查询
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {findPage} from "@/api/system/user/user.ts"
import {initDict} from "@/utils/dict"
import {reactive, ref} from "vue";
import DictTag from "@/components/dict-tag/index.vue"
import dayjs from "dayjs";
import {deptOption} from "@/api/system/dept/dept.ts";

const {sys_status} = initDict("sys_status")
const initSearch = () => {

  const option = ref<Array<SysDept>>([])
  // 初始化单位选项
  const initDeptOption = async () => {
    const resp = await deptOption()
    if (resp.code === 200) {
      resp.data.forEach(dept => {
        option.value.push(dept)
      })
    }
  }

  const userColumn: ColumnsType = [
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
      title: '所属部门',
      key: 'deptLabelList',
      dataIndex: 'deptLabelList'
    },
    {
      title: '手机号码',
      key: 'phoneNumber',
      dataIndex: 'phoneNumber',
      align: 'center'
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center'
    },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center'
    },
    {
      title: '操作',
      key: 'action',
      dataIndex: 'action',
      align: 'center'
    }
  ]

  const userQuery = ref<SysUserDTO>({
    deptIdList: [],
    nickname: undefined,
    username: undefined,
    phoneNumber: undefined,
    status: undefined,
    createTimeList: [],
    pageNum: 1,
    pageSize: 10,
  })

  const userList = ref<SysUserVO[]>([])

  const total = ref<Number>(0)

  const initPage = async () => {
    const resp = await findPage(userQuery.value)
    if (resp.code === 200) {
      userList.value = resp.data.records
      total.value = resp.data.total
    }
  }

  initPage()
  initDeptOption()

  return {
    userColumn,
    userQuery,
    userList,
    option,
    total
  }
}

const {userColumn,userQuery,userList,option,total } = initSearch()


// 数据保存相关
const initSave = () => {

  // modal 相关属性定义
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string, // 模态框标题
  }

  const modalActive = reactive<modalActiveType>({
    open: false,
    saveLoading: false,
    title: ''
  })

  const handleModelStatus = (title?:string) => {
    modalActive.open = !modalActive.open
    if (title) {
      modalActive.title = title
    }
  }

  return {
    modalActive,
    handleModelStatus
  }

}

const {modalActive,handleModelStatus} = initSave()
</script>

<style scoped>

</style>