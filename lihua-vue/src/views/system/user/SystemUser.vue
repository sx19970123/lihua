<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--      筛选条件-->
      <a-card :style="{border: 'none'}">
        <a-form :colon="false">
          <a-row :span="24" :gutter="16">
            <a-col :span="6">
              <a-form-item label="部门">
                <a-tree-select
                    placeholder="请选择部门"
                    v-model:value="userQuery.deptIdList"
                    :show-checked-strategy="SHOW_ALL"
                    multiple
                    :tree-data="sysDeptList"
                    :fieldNames="{children:'children', label:'name', value: 'id' }"
                    tree-node-filter-prop="label"
                    allowClear
                />
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
      <a-segmented v-model:value="segmented" :options="segmentedOption" style="margin-bottom: 16px"/>
      <a-form :model="sysUserDTO" :label-col="{span: 4}" :colon="false">
<!--        显示基本信息-->
        <div v-show="segmented === 'basic'">
          <a-form-item label="用户名" :wrapper-col="{span: 16}">
            <a-input placeholder="请输入用户名"/>
          </a-form-item>
          <a-form-item label="密码" :wrapper-col="{span: 16}">
            <a-input-password placeholder="请输入默认密码"/>
          </a-form-item>
          <a-form-item label="昵称" :wrapper-col="{span: 16}">
            <a-input placeholder="请输入昵称"/>
          </a-form-item>
            <a-form-item label="性别">
              <a-radio-group>
                <a-radio :value="item.value" v-for="item in user_gender">{{item.label}}</a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item label="状态">
              <a-radio :value="item.value" v-for="item in sys_status">{{item.label}}</a-radio>
            </a-form-item>
          <a-row>
            <a-col :span="12">
              <a-form-item label="手机号" :label-col="{span: 8}">
                <a-input  placeholder="请输入手机号码"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="邮箱" :label-col="{span: 8}">
                <a-input  placeholder="请输入邮箱"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-form-item label="备注">
            <a-textarea  placeholder="请输入备注"></a-textarea>
          </a-form-item>
        </div>
<!--        显示权限信息-->
        <div v-show="segmented === 'perm'">
          <a-form-item label="角色">
            <a-select
                v-model:value="sysUserDTO.roleIdList"
                placeholder="请选择用户角色"
                :options="sysRoleList"
                mode="multiple"
                optionFilterProp="name"
                :fieldNames="{label: 'name', value: 'id'}"/>
          </a-form-item>
          <a-form-item label="部门">
            <a-tree-select
                placeholder="请选择部门"
                v-model:value="sysUserDTO.deptIdList"
                :show-checked-strategy="SHOW_ALL"
                :fieldNames="{children:'children', label:'name', value: 'id' }"
                :tree-data="sysDeptList"
                @change="handleChangeDept"
                multiple
                show-search
            >
            </a-tree-select>
          </a-form-item>
          <a-form-item>
            <template #label>
              <a-typography-text>默认部门<br/> 岗位</a-typography-text>
            </template>
            <select-card
              :data-source="sysPostList"
              empty-description="请选择部门"
              item-key="deptId"
              vertical
            >
              <template #content="{item, isSelected, color}">
                <a-flex align="center" justify="space-between">
                  <a-typography-title :level="5" style="margin: 0">{{item.deptName}}</a-typography-title>
                  <a-tag v-if="isSelected" :color="color">默认</a-tag>
                </a-flex>

                <div style="margin-top: 16px;">
                  <div v-if="item.postList && item.postList.length > 0">
                    <a-checkable-tag v-for="post in item.postList"
                                     @change="(checked: boolean) => handleSelectPostId(post.id, checked)"
                                     @click.stop="() => {}"
                                     :key="post.id"
                                     v-model:checked="post.checked">
                      {{post.name}}
                    </a-checkable-tag>
                  </div>
                  <div v-else>
                    <a-typography-text type="secondary">当前部门下暂无岗位数据</a-typography-text>
                  </div>
                </div>
              </template>
            </select-card>
          </a-form-item>

        </div>
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
import SelectCard from "@/components/select-card/index.vue"
import dayjs from "dayjs";
import {getDeptOption} from "@/api/system/dept/dept.ts";
import {getRoleOption} from "@/api/system/role/role.ts";
import {getPostOptionByDeptId} from "@/api/system/post/post.ts";
import {TreeSelect} from "ant-design-vue";
const {sys_status,user_gender} = initDict("sys_status", "user_gender")
const SHOW_ALL = TreeSelect.SHOW_ALL;

const initSearch = () => {
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
  return {
    userColumn,
    userQuery,
    userList,
    total
  }
}

const {userColumn,userQuery,userList,total } = initSearch()


// 数据保存相关
const initSave = () => {

  // 定义保存用户信息
  const sysUserDTO = ref<SysUserDTO>({})

  const segmentedOption = reactive([{
    value: 'basic',
    label: '基础信息',
  }, {
    value: 'perm',
    label: '权限信息'
  }])
  const segmented = ref<string>(segmentedOption[0].value)

  // modal 相关属性定义
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string, // 模态框标题
  }

  // 模态框状态
  const modalActive = reactive<modalActiveType>({
    open: false,
    saveLoading: false,
    title: ''
  })

  // 修改模态框状态等信息
  const handleModelStatus = (title?:string) => {
    modalActive.open = !modalActive.open
    if (title) {
      modalActive.title = title
    }
  }

  return {
    modalActive,
    segmentedOption,
    segmented,
    sysUserDTO,
    handleModelStatus
  }

}
const {modalActive,segmented,segmentedOption,sysUserDTO,handleModelStatus} = initSave()

// 加载表单需要的选项 角色/部门/岗位
const initFormOptions = () => {
  // 角色信息
  const sysRoleList = ref<Array<SysRole>>([])
  // 部门信息
  const sysDeptList = ref<Array<SysDept>>([])

  type PostOptional = {
    id?: string,
    name?: string,
    checked: boolean
  }

  type PostType = {
    deptName: string,
    deptId: string,
    postList: Array<PostOptional>,
  }
  // 岗位信息
  const sysPostList = ref<Array<PostType>>([])

  // 加载角色信息
  const initRole = async () => {
    const resp = await getRoleOption()
    if (resp.code === 200) {
      sysRoleList.value = resp.data
    }
  }
  // 加载部门信息
  const initDept = async () => {
    const resp = await getDeptOption()
    if (resp.code === 200) {
      sysDeptList.value = resp.data
    }
  }
  // 根据部门id加载岗位信息
  const initPostByDeptId = async (deptIds: string[], option: Array<{label: string, value: string}>) => {
    const resp = await getPostOptionByDeptId(deptIds)
    if (resp.code === 200) {
      const data = resp.data
      deptIds.forEach(deptId => {
        const dataForDeptId = data[deptId]
        // push 单位-岗位数据
        // 1 添加的情况
        // 判断 deptId 在 sysPostList 元素中对应的 deptId 是否存在
        // 存在：保持不变
        // 不存在： 向 sysPostList 中 push 数据
        if (deptIds.length > sysPostList.value.length) {
          const ids = sysPostList.value.map(post => post.deptId)
          if (!ids.includes(deptId)) {
            console.log('新增的=', deptId)
            console.log('新增的=', option.filter(item => item.value = deptId)[0].label)
            console.log("option=",option)
            sysPostList.value.push({
              deptName: option.filter(item => item.value = deptId)[0].label,
              deptId: deptId,
              postList: sysPostsToPostOptional(dataForDeptId)
            })
          }
        }
        // 2 减少的情况
        // 判断 sysPostList 中是否存在 deptIds 中不存在的 id
        // 存在： 删除 sysPostList 中对应 deptId 的数据
        // 不存在： 保持不变
        if (deptIds.length < sysPostList.value.length) {
          const deptIds = sysPostList.value.map(post => post.deptId)
          if (deptIds.includes(deptId)) {
            sysPostList.value.splice(sysPostList.value.findIndex(item => item.deptId === deptId), 1)
          }
        }
      })
    }
  }

  // 将 SysPost 集合 转换为 PostOptional 集合
  const sysPostsToPostOptional = (postList: Array<SysPost>): Array<PostOptional> => {
    const resp: Array<PostOptional> = []
    if (postList) {
      postList.forEach(post => {
        resp.push({
          id: post.id,
          name: post.name,
          checked: false
        })
      })
    }
    return resp
  }

  // 选择部门
  const handleChangeDept = async (value: Array<string>, label: Array<string>) => {
    const option:Array<{
      value: string,
      label: string
    }> = []
    for (let i = 0; i < value.length; i++) {
      option.push({
        value: value[i],
        label: label[i]
      })
    }
    if (value.length > 0) {
      await initPostByDeptId(value, option)
    } else {
      sysPostList.value = []
    }

  }

  // 处理选中/取消选中 岗位标签
  const handleSelectPostId = (tag: string, checked: boolean) => {
    // 初始化 postIdList 为数组，如果它还没有被初始化
    if (!sysUserDTO.value.postIdList) {
      sysUserDTO.value.postIdList = [];
    }

    // 如果 checked 为 true，则添加 tag，否则删除它
    if (checked) {
      // 确保 tag 不会被重复添加
      if (!sysUserDTO.value.postIdList.includes(tag)) {
        sysUserDTO.value.postIdList.push(tag);
      }
    } else {
      // 找到 tag 的索引并将其删除
      const index = sysUserDTO.value.postIdList.indexOf(tag);
      if (index > -1) {
        sysUserDTO.value.postIdList.splice(index, 1);
      }
    }
  };

  initRole()
  initDept()
  return {
    sysRoleList,
    sysDeptList,
    sysPostList,
    initRole,
    initDept,
    initPostByDeptId,
    handleChangeDept,
    handleSelectPostId
  }
}

const {sysRoleList,sysDeptList,sysPostList,handleChangeDept,handleSelectPostId} = initFormOptions()
</script>

<style scoped>

</style>