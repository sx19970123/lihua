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
            <a-button type="link" size="small" @click="getUserInfo(record.id)">
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
      <a-form ref="formRef" :rules="userRules" :model="sysUserDTO" :label-col="{span: 4}" :colon="false">
<!--        显示基本信息-->
        <div v-show="segmented === 'basic'">
          <a-form-item label="用户名" name="username" :wrapper-col="{span: 16}">
            <a-input v-model:value="sysUserDTO.username" placeholder="请输入用户名" :maxlength="30" show-count/>
          </a-form-item>
          <a-form-item label="密码" name="password" :wrapper-col="{span: 16}" v-if="!sysUserDTO.id">
            <a-input-password v-model:value="sysUserDTO.password" placeholder="请输入默认密码"/>
          </a-form-item>
          <a-form-item label="昵称" name="nickname" :wrapper-col="{span: 16}">
            <a-input v-model:value="sysUserDTO.nickname" placeholder="请输入昵称" :maxlength="20" show-count/>
          </a-form-item>
            <a-form-item label="性别">
              <a-radio-group v-model:value="sysUserDTO.gender">
                <a-radio :value="item.value" v-for="item in user_gender">{{item.label}}</a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item label="状态">
              <a-radio-group v-model:value="sysUserDTO.status">
                <a-radio :value="item.value" v-for="item in sys_status">{{item.label}}</a-radio>
              </a-radio-group>
            </a-form-item>
          <a-row>
            <a-col :span="12">
              <a-form-item label="手机号" :label-col="{span: 8}" name="phoneNumber">
                <a-input v-model:value="sysUserDTO.phoneNumber"  placeholder="请输入手机号码"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="邮箱" :label-col="{span: 8}" name="email">
                <a-input v-model:value="sysUserDTO.email" placeholder="请输入邮箱"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-form-item label="备注">
            <a-textarea v-model:value="sysUserDTO.remark" placeholder="请输入备注"></a-textarea>
          </a-form-item>
        </div>
<!--        显示权限信息-->
        <div v-show="segmented === 'perm'">
          <div v-show="permStep === 'dept'">
            <a-form-item label="角色">
              <a-select
                  v-model:value="sysUserDTO.roleIdList"
                  placeholder="请选择用户角色"
                  :options="sysRoleList"
                  mode="multiple"
                  optionFilterProp="name"
                  :fieldNames="{label: 'name', value: 'id'}"/>
            </a-form-item>
            <a-form-item label="部门" class="form-item-single-line">

              <div style="margin-top: 0px">
                <a-checkable-tag v-model:checked="deptTreeSetting.checked" @click="handleCheckedAllKeys">全选/全不选</a-checkable-tag>
                <a-checkable-tag v-model:checked="deptTreeSetting.expand" @click="handleExpanded">展开/折叠</a-checkable-tag>
                <a-checkable-tag v-model:checked="deptTreeSetting.checkStrictly" @click=" deptTreeSetting.checkStrictly ? sysUserDTO.deptIdList = [] : ''">父子关联</a-checkable-tag>
              </div>
            </a-form-item>
            <a-form-item label=" ">
<!--              部门树-->
              <div class="dept-card">
                <a-input placeholder="检索部门树" v-model:value="deptKeyword" style="margin-bottom: 8px; height: 28px"/>
                <a-tree
                    :tree-data="sysDeptList"
                    :field-names="{children:'children', title:'name', key: 'id' }"
                    :check-strictly="!deptTreeSetting.checkStrictly"
                    v-model:checked-keys="sysUserDTO.deptIdList"
                    v-model:expanded-keys="deptTreeSetting.expandKeys"
                    :selectable="false"
                    checkable
                >
                  <template  #title="{ name }">
                    <span v-if="name.indexOf(deptKeyword) > -1">
                      {{name.substring(0,name.indexOf(deptKeyword))}}
                      <span :style="'color:' + themeStore.colorPrimary">{{deptKeyword}}</span>
                      {{name.substring(name.indexOf(deptKeyword) + deptKeyword.length)}}
                    </span>
                    <span v-else>{{ name }}</span>
                  </template>
                </a-tree>
              </div>
            </a-form-item>
          </div>
          <div v-show="permStep === 'post'">
            <a-form-item>
<!--              <template #label>-->
<!--                <a-typography-text>默认部门<br/> 岗位</a-typography-text>-->
<!--              </template>-->
              <card-select
                  :data-source="sysPostList"
                  empty-description="请选择部门"
                  item-key="deptId"
                  v-model="sysUserDTO.defaultDeptId"
                  :max-height="600"
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
              </card-select>
            </a-form-item>
          </div>
        </div>
      </a-form>
      <template #footer>
        <a-button>关 闭</a-button>
        <a-popover content="角色&部门配置">
          <a-button type="primary" v-show="segmented === 'perm' && permStep === 'post'" @click="permStep = 'dept'">
            <template #icon>
              <LeftOutlined />
            </template>
          </a-button>
        </a-popover>
        <a-button type="primary">保 存</a-button>
        <a-popover content="岗位&默认部门配置">
          <a-button type="primary" v-show="segmented === 'perm' && permStep === 'dept'" @click="toPostForm">
            <template #icon>
              <RightOutlined />
            </template>
          </a-button>
        </a-popover>
      </template>
    </a-modal>
  </div>
</template>

<script setup lang="ts">

// 列表查询
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {findPage, findById} from "@/api/system/user/user.ts"
import {initDict} from "@/utils/dict"
import {reactive, ref, watch} from "vue";
import DictTag from "@/components/dict-tag/index.vue"
import CardSelect from "@/components/card-select/index.vue"
import dayjs from "dayjs";
import {getDeptOption} from "@/api/system/dept/dept.ts";
import {getRoleOption} from "@/api/system/role/role.ts";
import {getPostOptionByDeptId} from "@/api/system/post/post.ts";
import {TreeSelect} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';
import {flattenTreeData} from "@/utils/tree.ts";
import type {Rule} from "ant-design-vue/es/form";
import {useThemeStore} from "@/stores/modules/theme.ts";
const themeStore = useThemeStore();
const {sys_status,user_gender} = initDict("sys_status", "user_gender")
const SHOW_ALL = TreeSelect.SHOW_ALL;
// 没有进行双向绑定的单位树
let originDeptTree: Array<SysDept> = ([])

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
  // 表单实例
  const formRef = ref()

  // 表单验证
  const userRules: Record<string, Rule[]> = {
    username: [
      {required: true, message: "请填写用户名", trigger: "change"}
    ],
    password: [
      {required: true, message: "请填写密码", trigger: "change"},
      { min: 6, max: 30, message: '密码长度6-30位', trigger: 'change' }
    ],
    nickname: [
      {required: true, message: "请填写昵称", trigger: "change"}
    ],
    phoneNumber: [
      {pattern: /^1[3456789]\d{9}$/, message: "请输入正确的手机号码", trigger: "change"}
    ],
    email: [
      {pattern: /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/, message: "请输入正确的邮箱", trigger: "change"}
    ]
  }

  // 定义保存用户信息
  const sysUserDTO = ref<SysUserDTO>({
    gender: '1',
    status: '0'
  })

  // 表单滑块选项
  const segmentedOption = reactive([{
    value: 'basic',
    label: '基础信息',
  }, {
    value: 'perm',
    label: '权限信息'
  }])

  const segmented = ref<string>(segmentedOption[0].value)

  // 权限步骤
  const permStep = ref<'dept'|'post'>('dept')

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

    if (modalActive.open) {
      resetForm()
    }
  }

  // 重置表单
  const resetForm = () => {
    // 重置表单
    sysUserDTO.value = {
      gender: '1',
      status: '0',
      deptIdList: []
    }
    // 分段器设置为默认
    segmented.value = 'basic'
    // 重制部门岗位
    initPostByDeptIds([])
    initPostTag([])
  }

  // 根据id查询用户信息
  const getUserInfo = async (userId: string) => {
    const resp = await findById(userId)
    if (resp.code === 200) {
      handleModelStatus("编辑用户")
      // 表单数据赋值
      sysUserDTO.value = resp.data
      // 默认部门 / 岗位 回显
      const deptIds = sysUserDTO.value.deptIdList
      const postIds = sysUserDTO.value.postIdList
      if (deptIds) {
        await initPostByDeptIds(deptIds)
      }
      if (postIds) {
        initPostTag(postIds)
      }
    }
  }
  return {
    modalActive,
    segmentedOption,
    segmented,
    sysUserDTO,
    userRules,
    formRef,
    permStep,
    resetForm,
    handleModelStatus,
    getUserInfo
  }

}
const {modalActive,segmented,segmentedOption,sysUserDTO,userRules,formRef,permStep,handleModelStatus,getUserInfo} = initSave()

// 加载表单需要的选项 角色/部门/岗位
const initOptions = () => {
  // 角色信息
  const sysRoleList = ref<Array<SysRole>>([])

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

  // 加载岗位信息，通过 选中部门/表单回显写入部门id 进行加载
  const initPostByDeptIdOption = async (deptIds: string[], option: Array<{label: string, value: string}>) => {
    // 记录原始部门ID集合
    const originDeptIds = sysPostList.value.map(post => post.deptId)
    // 删除没有被选中的id
    originDeptIds.forEach(deptId => {
      if (!deptIds.includes(deptId)) {
        const index = sysPostList.value.findIndex(item => item.deptId === deptId)
        if (index > -1) {
          sysPostList.value.splice(index, 1)
        }
      }
    })

    // 如果新旧ID集合长度相同，直接返回
    if (deptIds.length === sysPostList.value.length) {
      return;
    }

    // 新选中的部门id集合
    const newDeptIds = deptIds.filter(item => !originDeptIds.includes(item))

    // 后端查询新部门及岗位数据
    const resp = await getPostOptionByDeptId(newDeptIds)
    if (resp.code === 200) {
      const data = resp.data
      newDeptIds.forEach(deptId => {
        sysPostList.value.push({
          deptId: deptId,
          deptName: cloneDeep(option).find((item:{label: string, value: string}) => item.value === deptId).label,
          postList: sysPostsToPostOptional(data[deptId])
        })
      })
    }
  }

  // 将 SysPost 集合  转换为 PostOptional 集合
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

  // 通过选中部门加载岗位信息
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
      await initPostByDeptIdOption(value, option)
    } else {
      sysPostList.value = []
    }
  }

  // 通过表单回写部门id加载岗位信息
  const initPostByDeptIds = async (value: Array<string>) => {
    const flattenTree:Array<SysDept> = []
    flattenTreeData(sysDeptList.value,flattenTree)
    const labelList: Array<string> = []
    value.forEach(item => {
      flattenTree.forEach(dept => {
        if (dept.id === item && dept.name) {
          labelList.push(dept.name)
        }
      })
    })
    await handleChangeDept(value, labelList)
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

  // 回显岗位标签
  const initPostTag = (postIds: Array<String>) => {
    // 部门岗位中postId 与 postIds 相同时 checked 设置为true
    const postDeptOption = sysPostList.value
    postDeptOption.forEach(postDept => {
      if (postDept.postList && postDept.postList.length > 0) {
        postDept.postList.forEach(post => {
          if (post.id && postIds.includes(post.id)) {
            post.checked = true
          }
        })
      }
    })
  }

  initRole()
  return {
    sysRoleList,
    sysPostList,
    handleChangeDept,
    handleSelectPostId,
    initPostByDeptIds,
    initPostTag
  }
}

const {sysRoleList,sysPostList,handleChangeDept,handleSelectPostId,initPostByDeptIds,initPostTag} = initOptions()

// 处理部门树数据
const initDeptData = () => {
  // 部门信息
  const sysDeptList = ref<Array<SysDept>>([])

  // 扁平化部门树
  const flattenDeptList: Array<SysDept> = []
  // 部门树检索关键字
  const deptKeyword = ref<string>('')
  // 所有树型节点id
  const deptIds: string[] = []

  type DeptTreeSettingType = {
    // 展开的节点
    expandKeys: string[],
    // 是否全部展开
    expand: boolean,
    // 父子联动
    checkStrictly: boolean
    // 是否全部选中
    checked: boolean,
  }
  // 部门树配置信息
  const deptTreeSetting = ref<DeptTreeSettingType>({
    expandKeys: [],
    expand: false,
    checkStrictly: true,
    checked: false
  })

  // 处理展开折叠
  const handleExpanded = () => {
    // 全部展开
    deptTreeSetting.value.expandKeys = []
    if (deptTreeSetting.value.expand) {
      deptTreeSetting.value.expandKeys.push(... deptIds)
    }
  }

  // 处理全选
  const handleCheckedAllKeys = () => {
    sysUserDTO.value.deptIdList = []
    if (deptTreeSetting.value.checked) {
      sysUserDTO.value.deptIdList.push(... deptIds)
    }
  }

  // 处理关键词过滤
  const filterTreeByLabel = (tree: Array<SysDept>, keyword: string): Array<SysDept> => {
    const cloneTree = cloneDeep(tree);

    const filterNode = (node: SysDept): SysDept | null => {
      if (node.children) {
        node.children = node.children.map(filterNode).filter((child): child is SysDept => child !== null);
      }
      return node.name?.includes(keyword) || (node.children && node.children.length > 0) ? node : null;
    };

    return cloneTree.map(filterNode).filter((node: SysDept) => node !== null);
  };

  // 加载部门信息
  const initDept = async () => {
    const resp = await getDeptOption()
    if (resp.code === 200) {
      // 单位树
      sysDeptList.value = resp.data
      // 未双向绑定的单位树
      originDeptTree = resp.data
      // 处理为扁平化数据
      flattenTreeData(resp.data, flattenDeptList)
      // 获取全部部门id
      const mapIds = flattenDeptList.filter(item => item.id).map(item => item.id)
      deptIds.push(... (mapIds as string[]))
    }
  }
  initDept()
  return {
    sysDeptList,
    deptTreeSetting,
    deptKeyword,
    flattenDeptList,
    handleExpanded,
    handleCheckedAllKeys,
    filterTreeByLabel
  }
}

const {sysDeptList,deptTreeSetting,deptKeyword,flattenDeptList,handleExpanded, handleCheckedAllKeys,filterTreeByLabel} = initDeptData()

const toPostForm = () => {
  const obj = sysUserDTO.value.deptIdList as {checked: string[]} | string[]
  let ids = []
  if ((obj as {checked: string[]}).checked) {
    ids = (obj as {checked: string[]}).checked
  } else {
    ids = (obj as string[])
  }
  initPostByDeptIds(ids)
  permStep.value = 'post'
}


// 监听关键词筛选
watch(() => deptKeyword.value, (value) => {
  if (value) {
    // 关键词输入时全部展开
    if (!deptTreeSetting.value.expand) {
      deptTreeSetting.value.expand = true
      handleExpanded()
    }
    // 对树型结构进行过滤
    sysDeptList.value = filterTreeByLabel(originDeptTree, value)
  } else {
    // value 为空时，还原树
    sysDeptList.value = cloneDeep(originDeptTree)
  }
})

// 坚挺反向选中标签
watch(() => sysUserDTO.value.deptIdList, (value) => {
  let ids: string[] = [];

  if (Array.isArray(value)) {
    // 如果 value 是数组
    ids = value;
  } else if (value && typeof value === 'object' && 'checked' in value) {
    // 如果 value 是包含 checked 属性的对象
    ids = (value as { checked: string[] }).checked;
  }

  // 更新 deptTreeSetting 的 checked 状态
  deptTreeSetting.value.checked = ids.length === flattenDeptList.length;
});
</script>

<style>
.dept-card {
  border-radius: 8px;
  padding: 16px;
  margin-top: 4px;
}
[data-theme="light"] {
  .dept-card {
    border: 1px solid #d9d9d9;
  }
}
[data-theme="dark"] {
  .dept-card {
    border: 1px solid #424242;
  }
}
</style>