<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--        检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="所属部门">
                <a-tree-select
                    :tree-data="deptTree"
                    style="width: 180px"
                    :fieldNames="{children:'children', label:'name', value: 'id' }"
                    placeholder="请选择部门"
                    allow-clear
                    v-model:value="postQuery.deptId"
                />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="岗位名称">
                <a-input placeholder="请输入岗位名称" v-model:value="postQuery.name" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="岗位编码">
                <a-input placeholder="请输入岗位编码" v-model:value="postQuery.code" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="岗位状态">
                <a-select placeholder="请选择" v-model:value="postQuery.status" allow-clear>
                  <a-select-option :value="item.value" v-for="item in sys_status">{{item.label}}</a-select-option>
                </a-select>
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
                  <a-button :loading="tableLoad" @click="reloadPage">
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
      <!--        列表-->
      <a-table :columns="postColumn" :data-source="postList" :pagination="false" :loading="tableLoad">
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary" @click="handleModalStatus('新增岗位')">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
          </a-flex>
        </template>

        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'status'">
            <dict-tag  :dict-data-option="sys_status" :dict-data-value="text"/>
          </template>

          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" @click="selectById(record.id)">
              <template #icon>
                <EditOutlined />
              </template>
              编辑
            </a-button>
            <a-divider type="vertical"/>
            <a-popconfirm ok-text="确 定"
                          cancel-text="取 消"
                          @confirm="handleDelete(record.id)"
                          placement="bottomRight"
            >
              <template #title>
                数据删除后不可恢复，是否删除？
              </template>
              <a-button type="link" danger size="small">
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
            <a-pagination v-model:current="postQuery.pageNum"
                          v-model:page-size="postQuery.pageSize"
                          :total="postTotal"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="initPage"/>
          </a-flex>
        </template>
      </a-table>
    </a-flex>

    <a-modal v-model:open="modalActive.open" @ok="savePost" :confirm-loading="modalActive.saveLoading">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>

      <a-form :colon="false" :model="sysPost" ref="formRef" :label-col="{span: 4}" :rules="postRoles">
        <a-form-item label="所属部门" :wrapper-col="{span: 16}" name="deptId">
          <a-tree-select
              :tree-data="deptTree"
              :fieldNames="{children:'children', label:'name', value: 'id' }"
              placeholder="请选择部门"
              allow-clear
              v-model:value="sysPost.deptId"
          />
        </a-form-item>
        <a-form-item label="岗位名称" :wrapper-col="{span: 16}" name="name">
          <a-input placeholder="请输入岗位名称" v-model:value="sysPost.name" allow-clear :maxlength="60" show-count/>
        </a-form-item>
        <a-form-item label="岗位编码" :wrapper-col="{span: 16}" name="code">
          <a-input placeholder="请输入岗位编码" v-model:value="sysPost.code" allow-clear :maxlength="100" show-count/>
        </a-form-item>
        <a-form-item label="排序" name="sort">
          <a-input-number placeholder="升序排列" v-model:value="sysPost.sort"/>
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-radio-group v-model:value="sysPost.status">
            <a-radio :value="item.value" v-for="item in sys_status">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-row>
          <a-col :span="12">
            <a-form-item label="负责人" :label-col="{span: 8}" name="manager">
              <a-input placeholder="请输入负责人" v-model:value="sysPost.manager" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系电话" :label-col="{span: 8}" name="phoneNumber">
              <a-input placeholder="请输入联系电话" v-model:value="sysPost.phoneNumber" allow-clear/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="传真" :label-col="{span: 8}" name="fax">
              <a-input placeholder="请输入传真号码" v-model:value="sysPost.fax" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="电子邮箱" :label-col="{span: 8}" name="email">
              <a-input placeholder="请输入电子邮箱" v-model:value="sysPost.email" allow-clear/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="备注" name="remark">
          <a-textarea type="textarea" placeholder="请输入备注" v-model:value="sysPost.remark" :maxlength="300" show-count allow-clear/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">

import {getDeptOption} from "@/api/system/dept/Dept.ts";
import {reactive, ref, watch} from "vue";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {initDict} from "@/utils/dict.ts";
import {deleteByIds, findById, findPage, save} from "@/api/system/post/Post.ts";
import {useRoute} from "vue-router";
import DictTag from "@/components/dict-tag/index.vue";
import type {Rule} from "ant-design-vue/es/form";
import {flattenTreeData} from "@/utils/Tree.ts";
import {message} from "ant-design-vue";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import type {SysPost, SysPostDTO} from "@/api/system/post/type/SysPost.ts";
const {sys_status} = initDict("sys_status")
const route = useRoute();

// 监听传入deptId变化进行部门赋值
watch(() => route.query.deptId, (value) => {
  postQuery.value.deptId = value as string | undefined;
  initPage()
})


// 初始化部门树
const initDept = () => {
  const deptTree = ref<Array<SysDept>>([])
  const initDeptTree = async () => {
    const resp = await getDeptOption()
    if (resp.code === 200 ) {
      deptTree.value = resp.data
    }
  }
  initDeptTree()

  return {
    deptTree
  }
}

const { deptTree } = initDept()

// 查询列表
const initSearch = () => {
  const postColumn: ColumnsType = [
    {
      title: '岗位名称',
      key: 'name',
      dataIndex: 'name'
    },
    {
      title: '岗位编码',
      key: 'code',
      dataIndex: 'code'
    },
    {
      title: '排序',
      key: 'sort',
      dataIndex: 'sort',
      align: 'right',
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center',
    },
    {
      title: '负责人',
      key: 'manager',
      dataIndex: 'manager',
      align: 'center',
    },
    {
      title: '所属部门',
      key: 'deptName',
      dataIndex: 'deptName'
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '180px'
    }
  ]

  const postQuery = ref<SysPostDTO>({
    deptId: route.query.deptId as string | undefined,
    pageNum: 1,
    pageSize: 10,
  })
  const postTotal = ref<number>()
  const postList = ref<Array<SysPost>>()
  const tableLoad = ref<boolean>(false)

  const queryPage = async () => {
    postQuery.value.pageNum = 1
    await initPage()
  }

  const reloadPage = async () => {
    postQuery.value = {
      pageNum: 1,
      pageSize: 10
    }
    await initPage()
  }
  
  const initPage = async () => {
    tableLoad.value = true
    const resp = await findPage(postQuery.value)
    if (resp.code === 200) {
      postList.value = resp.data.records
      postTotal.value = resp.data.total
      tableLoad.value = false
    } else {
      message.error(resp.msg)
    }
  }
  queryPage()
  return {
    postColumn,
    postList,
    postQuery,
    queryPage,
    initPage,
    reloadPage,
    tableLoad,
    postTotal
  }
}
const { postColumn,postList,postQuery,initPage,queryPage,reloadPage,tableLoad,postTotal } = initSearch()


// 保存岗位
const initSave = () => {

  const formRef = ref()

  const sysPost = ref<SysPost>({
    status: '0'
  })

  // 表单验证
  const postRoles: Record<string, Rule[]> = {
    deptId: [
      {required: true, message: "请选择所属部门", trigger: "change"}
    ],
    name: [
      {required: true, message: "请输入岗位名称", trigger: "change"}
    ],
    code: [
      {required: true, message: "请输入岗位编码", trigger: "change"}
    ],
    sort: [
      {required: true, message: "请输入排序", trigger: "change"}
    ],
    phoneNumber: [
      {pattern: /^1[3456789]\d{9}$/, message: "请输入正确的手机号码", trigger: "change"}
    ],
    email: [
      {pattern: /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/, message: "请输入正确的邮箱", trigger: "change"}
    ],
    fax: [
      {pattern: /^\d{3,20}$/, message: "请输入正确的传真", trigger: "change"}
    ]
  }

  // 模态框状态
  type modalActiveType = {
    open: boolean,
    saveLoading: boolean,
    title: string,
  }
  const modalActive = reactive<modalActiveType>({
    open: false,
    saveLoading: false,
    title: ''
  })

  // 修改模态框状态
  const handleModalStatus = (title?: string) => {
    modalActive.open = !modalActive.open

    if (title) {
      modalActive.title = title
    }

    sysPost.value = {
      status: '0'
    }
  }

  const selectById = async (id: string) => {
    const resp = await findById(id)
    if (resp.code === 200) {
      handleModalStatus('修改岗位')
      sysPost.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }

  // 保存岗位数据
  const savePost = async () => {
    await formRef.value.validate()

    // 设置岗位编码
    const flattenTreeList: Array<SysDept> = []
    flattenTreeData(deptTree.value, flattenTreeList)
    flattenTreeList.forEach(item => {
      if (item.id === sysPost.value.deptId) {
        sysPost.value.deptCode = item.code
      }
    })

    modalActive.saveLoading = true
    const resp = await save(sysPost.value)
    if (resp.code === 200) {
      message.success(resp.msg)
      modalActive.open = false
      await initPage()
    } else {
      message.error(resp.msg)
    }
    modalActive.saveLoading = false
  }

  return {
    modalActive,
    sysPost,
    postRoles,
    formRef,
    selectById,
    savePost,
    handleModalStatus,
  }
}
const { modalActive, sysPost, postRoles, formRef, selectById ,savePost ,handleModalStatus } = initSave()

// 删除岗位
const handleDelete = async (id: string) => {
  const resp = await deleteByIds([id])
  if (resp.code === 200) {
    message.success(resp.msg)
    await initPage()
  } else {
    message.error(resp.msg)
  }
}
</script>

<style scoped>

</style>