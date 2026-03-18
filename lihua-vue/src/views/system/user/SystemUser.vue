<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--      筛选条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="部门">
                <a-tree-select
                    class="default-input-width"
                    placeholder="请选择部门"
                    v-model:value="userQuery.deptIdList"
                    show-checked-strategy="SHOW_ALL"
                    :maxTagCount="3"
                    :tree-data="sysDeptList"
                    :fieldNames="{children:'children', label:'name', value: 'id' }"
                    tree-node-filter-prop="label"
                    multiple
                    allowClear
                />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="用户名">
                <a-input placeholder="请输入用户名" v-model:value="userQuery.username" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="昵称">
                <a-input placeholder="请输入昵称" v-model:value="userQuery.nickname" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="状态">
                <a-select placeholder="请选择 " v-model:value="userQuery.status" allowClear>
                  <a-select-option :value="item.value" v-for="item in sys_status">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="创建时间">
                <a-range-picker allowClear v-model:value="userQuery.createTimeList"/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item>
                <a-space size="small">
                  <a-button type="primary" @click="handleQueryPage" :loading="queryLoading">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button @click="resetPage" :loading="queryLoading">
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
      <a-table :columns="userColumn"
               :data-source="userList"
               :loading="queryLoading"
               :pagination="false"
               :row-selection="userRowSelectionType"
               row-class-name="hover-cursor-pointer"
               :custom-row="handleRowClick"
               row-key="id"
               :scroll="{x: 1500}"
      >
        <template #title>
          <a-flex :gap="8" wrap="wrap" >
            <a-button type="primary" @click="handleModelStatus('新增用户')">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          :open="openDeletePopconfirm"
                          ok-text="确 定"
                          cancel-text="取 消"
                          @confirm="handleDelete(undefined)"
                          @cancel="closePopconfirm"
                          @open-change="(open: boolean) => !open ? closePopconfirm(): ''"
            >
              <a-button danger @click="openPopconfirm">
                <template #icon>
                  <DeleteOutlined />
                </template>
                删 除
                <span v-if="selectedIds && selectedIds.length > 0" style="margin-left: var(--lihua-space-xs)"> {{selectedIds.length}} 项</span>
              </a-button>
            </a-popconfirm>
            <a-dropdown>
              <a-button type="primary" ghost>
                更多
                <DownOutlined />
              </a-button>
              <template #overlay>
                <a-menu>
                  <a-menu-item key="export" @click="handleExportExcel">批量导出</a-menu-item>
                  <a-menu-item key="import">
                    <a-upload :customRequest="handleCustomRequest"
                              :beforeUpload="handleBeforeUpdate"
                              :showUploadList="false"
                              accept=".xlsx,.xls"
                              style="width: 100%;"
                    >
                      批量导入
                    </a-upload>
                  </a-menu-item>
                  <a-menu-item @click="handleDownloadExcelTemplate">模板下载</a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
            <!--            表格设置-->
            <table-setting v-model="userColumn"/>
          </a-flex>
        </template>
        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'deptLabelList'">
            {{text ? text.join('、') : ''}}
          </template>
          <template v-if="column.key === 'createTime'">
            {{dayjs(text).format('YYYY-MM-DD HH:mm')}}
          </template>
          <template v-if="column.key === 'registerType'">
            <dict-tag :dict-data-option="sys_user_register_type" :dict-data-value="text"></dict-tag>
          </template>
          <template v-if="column.key === 'status'">
            <a-switch v-model:checked="record.statusIsNormal"
                      :disabled="record.username === 'admin'"
                      @change="(checked: boolean | string | number, event: MouseEvent) => handleUpdateStatus(event,record.id, text)"
                      @click="(checked: boolean | string | number, event: MouseEvent) => { event.stopPropagation(); record.updateStatusLoading = true }"
                      :loading="record.updateStatusLoading">
              <template #checkedChildren>
                {{sys_status.filter(item => item.value === text)[0]?.label}}
              </template>
              <template #unCheckedChildren>
                {{sys_status.filter(item => item.value === text)[0]?.label}}
              </template>
            </a-switch>
          </template>
          <template v-if="column.key === 'action' && record.username !== 'admin'">
            <a-button type="link" size="small" @click="(event: MouseEvent) => getUserInfo(event, record.id)">
              <template #icon>
                <EditOutlined />
              </template>
              编辑
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small" @click="(event: MouseEvent) => handleOpenResetPasswordModel(event, record)">
              <template #icon>
                <KeyOutlined />
              </template>
              重置密码
            </a-button>
            <a-divider type="vertical"/>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          placement="bottomRight"
                          ok-text="确 定"
                          cancel-text="取 消"
                          @confirm="handleDelete(record.id)"
            >
              <a-button type="link" danger size="small" @click="(event: MouseEvent) => event.stopPropagation()">
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
            <a-pagination v-model:current="userQuery.pageNum"
                          v-model:page-size="userQuery.pageSize"
                          show-size-changer
                          :total="userTotal"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="initPage"
            />
          </a-flex>
        </template>
      </a-table>
    </a-flex>

    <a-modal v-model:open="modalActive.open">
      <template #title>
        <div style="margin-bottom: var(--lihua-space-lg)" v-draggable>
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
      <a-segmented v-model:value="segmented" :options="segmentedOption" style="margin-bottom: var(--lihua-space-base)" @change="changeSegmented"/>
      <a-form ref="formRef" :rules="userRules" :model="sysUserDTO" :label-col="{span: 4}" :colon="false">
<!--        显示基本信息-->
        <div v-show="segmented === 'basic'">
          <a-form-item label="用户名" name="username" :wrapper-col="{span: 16}">
            <a-input v-model:value="sysUserDTO.username" placeholder="用于用户登录" :maxlength="30" show-count allowClear/>
          </a-form-item>
          <a-form-item label="密码" name="password" :wrapper-col="{span: 16}" v-if="!sysUserDTO.id">
            <a-input-password v-model:value="sysUserDTO.password" placeholder="请输入密码" allowClear/>
          </a-form-item>
          <a-form-item label="昵称" name="nickname" :wrapper-col="{span: 16}">
            <a-input v-model:value="sysUserDTO.nickname" placeholder="请输入昵称" :maxlength="20" show-count allowClear/>
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
                <a-input v-model:value="sysUserDTO.phoneNumber"  placeholder="请输入手机号码" allowClear/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="邮箱" :label-col="{span: 5}" name="email">
                <a-input v-model:value="sysUserDTO.email" placeholder="请输入邮箱" allowClear/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-form-item label="备注">
            <a-textarea v-model:value="sysUserDTO.remark" placeholder="请输入备注" allowClear :maxlength="480" show-count></a-textarea>
          </a-form-item>
        </div>
<!--        显示权限信息-->
        <div v-show="segmented === 'dept'">
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
            <easy-tree-select :tree-data="sysDeptList"
                              v-model="sysUserDTO.deptIdList"
                              :field-names="{ children:'children', title:'name', key: 'id' }"
                              ref="easyTreeSelectRef"/>
          </a-form-item>
        </div>
        <div v-show="segmented === 'post'">
          <a-form-item>
            <selectable-card
                :data-source="sysPostList"
                empty-description="请选择部门"
                item-key="deptId"
                v-model="sysUserDTO.defaultDeptId"
                :max-height="600"
                :loading="postLoading"
                vertical
            >
              <template #content="{item, isSelected, color}">
                <a-flex align="center" justify="space-between">
                  <a-typography-title :level="5" style="margin: 0">{{item?.deptName}}</a-typography-title>
                  <a-tag v-if="isSelected" :color="color">默认</a-tag>
                </a-flex>
                <div style="margin-top: var(--lihua-space-base)">
                  <div v-if="item?.postList && item?.postList.length > 0">
                    <a-checkable-tag v-for="post in item?.postList"
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
            </selectable-card>
          </a-form-item>
        </div>
      </a-form>
      <template #footer>
        <a-button @click="modalActive.open = false">关 闭</a-button>
        <a-button type="primary" @click="saveUser" :loading="modalActive.saveLoading">保 存</a-button>
<!--        前往下一选项卡-->
        <a-popover v-if="segmented !== 'post'"
                   :content="segmentedOption[segmentedOption.findIndex(item => item.value === segmented) + 1]?.label">
          <a-button v-if="segmented !== 'post'"
                    type="primary"
                    @click="toNextForm(segmentedOption[segmentedOption.findIndex(item => item.value === segmented) + 1]?.value)">
            <template #icon>
              <RightOutlined />
            </template>
          </a-button>
        </a-popover>
      </template>
    </a-modal>

<!--    重置密码-->
     <a-modal v-model:open="showResetPassword" width="400px">
       <template #title>
         <div style="margin-bottom: var(--lihua-space-lg)" v-draggable>
           <a-typography-title :level="4">重置{{targetUserInfo.nickname ? targetUserInfo.nickname + '的' : ''}}密码</a-typography-title>
         </div>
       </template>
       <a-form :model="resetPasswordForm" :rules="defaultPasswordRules" ref="resetPasswordRef">
         <a-form-item name="password" class="form-item-single-line">
           <password-input v-model:value="resetPasswordForm.password"
                           :show-progress="!!resetPasswordForm.password && resetPasswordForm.password.length >= 6"
                           placeholder="请输入密码"
                           :size="116"/>
         </a-form-item>
         <div style="margin-top: var(--lihua-space-sm);">
           <a-checkbox v-model:checked="useDefaultPassword" @change="handleChangeUseDefaultPassword">使用默认密码</a-checkbox>
         </div>
       </a-form>
       <template #footer>
         <a-button @click="showResetPassword = false">关 闭</a-button>
         <a-button type="primary" @click="handleResetPassword" :loading="resetPasswordLoading">保 存</a-button>
       </template>
     </a-modal>
  </div>
</template>

<script setup lang="ts">

// 列表查询
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {
  deleteByIds,
  excelTemplate,
  exportExcel,
  importExcel,
  queryById,
  queryPage,
  resetPassword,
  save,
  updateStatus
} from "@/api/system/user/User.ts"
import {initDict} from "@/utils/Dict.ts"
import {h, onMounted, reactive, ref, useTemplateRef} from "vue";
import SelectableCard from "@/components/selectable-card/index.vue"
import PasswordInput from "@/components/password-input/index.vue"
import DictTag from "@/components/dict-tag/index.vue"
import EasyTreeSelect from "@/components/easy-tree-select/index.vue"
import TableSetting from "@/components/table-setting/index.vue";
import dayjs from "dayjs";
import {getDeptOption} from "@/api/system/dept/Dept.ts";
import {getRoleOption} from "@/api/system/role/Role.ts";
import {getPostOptionByDeptId} from "@/api/system/post/Post.ts";
import {type FormInstance, message, Modal} from "ant-design-vue";
import {cloneDeep} from 'lodash-es';
import {traverse} from "@/utils/Tree.ts";
import type {Rule} from "ant-design-vue/es/form";
import type {SysUserDTO, SysUserVO} from "@/api/system/user/type/SysUser.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import type {SysRole} from "@/api/system/role/type/SysRole.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";
import type {UploadRequestOption} from "ant-design-vue/lib/vc-upload/interface";
import Spin from "@/components/spin";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import {useSettingStore} from "@/stores/setting.ts";
import {type BaseModalActiveType} from "@/api/global/Type.ts";
import {download} from "@/utils/AttachmentDownload.ts";
import {useUserStore} from "@/stores/user.ts";
import {refreshUserData} from "@/utils/AppInit.ts";
import {useRoute} from "vue-router";

const easyTreeSelectRef = useTemplateRef<InstanceType<typeof EasyTreeSelect>>("easyTreeSelectRef")
const settingStore = useSettingStore()
const userStore = useUserStore()
const route = useRoute()
const {sys_status, user_gender, sys_user_register_type} = initDict("sys_status", "user_gender", "sys_user_register_type")
// 默认密码
const defaultPassword = ref<string>()
// 列表查询
const initSearch = () => {
  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const userRowSelectionType = {
    columnWidth: '55px',
    type: 'checkbox',
    // 支持跨页勾选
    preserveSelectedRowKeys: true,
    // 指定选中id的数据集合，操作完后可手动清空
    selectedRowKeys: selectedIds,
    onChange: (ids: Array<string>) => {
      selectedIds.value = ids
    },
    // 设置禁选数据
    getCheckboxProps: (record: SysUserVO) => ({
      disabled: record.username === 'admin',
    })
  }
  // 点击数据行选中
  const handleRowClick = (record:SysUserVO) => {
    if (record.username === 'admin') {
      return
    }
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

  const userColumn = ref<ColumnsType>([
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
      ellipsis: true,
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
      title: '注册类型',
      key: 'registerType',
      dataIndex: 'registerType',
      align: 'center'
    },
    {
      title: '备注',
      key: 'remark',
      ellipsis: true,
      dataIndex: 'remark',
    },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center',
      width: 200
    },
    {
      title: '操作',
      key: 'action',
      dataIndex: 'action',
      align: 'center',
      width: '292px',
      fixed: 'right'
    }
  ])

  const userQuery = ref<SysUserDTO>({
    deptIdList: [],
    nickname: undefined,
    username: undefined,
    status: undefined,
    createTimeList: [],
    pageNum: 1,
    pageSize: 10,
  })

  const userList = ref<SysUserVO[]>([])
  const userTotal = ref<Number>(0)
  const queryLoading = ref<boolean>(false)

  // 列表页查询
  const initPage = async () => {
    queryLoading.value = true
    try {
      const resp = await queryPage(userQuery.value)
      if (resp.code === 200) {
        userList.value = resp.data.records
        userTotal.value = resp.data.total

        // 回显状态
        userList.value.forEach(user => {
          user.statusIsNormal = user.status === '0'
          user.updateStatusLoading = false
        })
      } else {
        message.error(resp.msg)
      }
    } finally {
      queryLoading.value = false
    }
  }

  // 条件检索初始页码设置为0
  const handleQueryPage = async () => {
    userQuery.value.pageNum = 1
    await initPage()
  }

  // 清空选项
  const resetPage = async () => {
    userQuery.value = {
      deptIdList: [],
      nickname: undefined,
      username: undefined,
      status: undefined,
      createTimeList: [],
      pageNum: 1,
      pageSize: 10
    }
    await initPage()
  }
  return {
    userColumn,
    userQuery,
    userList,
    userTotal,
    queryLoading,
    userRowSelectionType,
    selectedIds,
    handleRowClick,
    initPage,
    handleQueryPage,
    resetPage
  }
}
const {userColumn,userQuery,userList,userTotal,queryLoading,userRowSelectionType,selectedIds,handleRowClick,initPage,handleQueryPage,resetPage } = initSearch()

// 数据保存相关
const initSave = () => {
  // 表单实例
  const formRef = useTemplateRef<FormInstance>("formRef")

  // 表单验证
  const userRules: Record<string, Rule[]> = {
    username: [
      {required: true, message: "请填写用户名", trigger: "change"},
      {pattern: /^[a-zA-Z0-9@.]+$/, message: "用户名只允许大小写英文、数字、@、.", trigger: "change"}
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
  const segmentedOption = [{
    value: 'basic',
    label: '基础信息',
  }, {
    value: 'dept',
    label: '角色&部门'
  }, {
    value: 'post',
    label: '岗位&默认部门'
  }]

  const segmented = ref<string>(segmentedOption[0].value)

  // 模态框状态
  const modalActive = reactive<BaseModalActiveType>({
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

  // 切换分段器
  const changeSegmented = (value: string) => {
    if (value === 'post') {
      toPostForm()
    }
  }

  // 前往下一表单页面
  const toNextForm = (nextSegmentedValue?: string) => {
    if (nextSegmentedValue) {
      if (nextSegmentedValue === 'post') {
        toPostForm()
      }
      segmented.value = nextSegmentedValue
    }
  }

  // 重置表单
  const resetForm = () => {
    // 重置表单
    sysUserDTO.value = {
      gender: '1',
      status: '0',
      deptIdList: [],
      password: defaultPassword.value
    }
    // 分段器设置为默认
    segmented.value = 'basic'
    // 重置部门岗位
    initPostByDeptIds([])
    initPostTag([])
    // 重置树形选择组件
    easyTreeSelectRef.value?.reset()
  }

  // 保存用户信息
  const saveUser = async () => {
    modalActive.saveLoading = true

    // 表单验证
    try {
      await formRef.value?.validate()
    } catch (error) {
      // 出现表单验证信息后跳转到表单首页
      toNextForm('basic')
      return
    } finally {
      modalActive.saveLoading = false
    }

    const userDTO = cloneDeep(sysUserDTO.value)
    // 处理用户部门、手机号、邮箱
    userDTO.deptIdList = sysUserDTO.value.deptIdList
    userDTO.phoneNumber === "" ? sysUserDTO.value.phoneNumber = undefined : sysUserDTO.value.phoneNumber
    userDTO.email === "" ? sysUserDTO.value.email = undefined : sysUserDTO.value.email

    const userId = sysUserDTO.value.id
    try {
      // 调用保存接口
      const resp = await save(userDTO)
      if (resp.code === 200) {
        modalActive.open = false
        // 保存的用户如果为当前用户，则触发刷新数据，否则重新查询页面
        if (userId === userStore.userId) {
          await refreshUserData(route)
        } else {
          await initPage()
        }
        message.success(resp.msg)
      } else {
        message.error(resp.msg)
      }
    } finally {
      modalActive.saveLoading = false
    }
  }

  // 处理改变状态
  const handleUpdateStatus = async (event: MouseEvent, id: string, status: string) => {
    event.stopPropagation()
    let newStatus: string = status
    try {
      const resp = await updateStatus(id, status)
      if (resp.code === 200) {
        newStatus = resp.data
        message.success(resp.msg)
      } else {
        message.error(resp.msg)
      }
    } finally {
      // 重新赋值
      userList.value.some(user => {
        if (user.id === id) {
          user.status = newStatus
          user.statusIsNormal = user.status === '0'
          user.updateStatusLoading = false
          return
        }
      })
    }
  }

  // 根据id查询用户信息
  const getUserInfo = async (event: MouseEvent, userId: string) => {
    event.stopPropagation()
    const resp = await queryById(userId)
    if (resp.code === 200) {
      handleModelStatus("编辑用户")
      // 表单数据赋值
      sysUserDTO.value = cloneDeep(resp.data)
      // 默认部门 / 岗位 回显
      const deptIds = sysUserDTO.value.deptIdList
      const postIds = sysUserDTO.value.postIdList
      // 加载部门
      if (deptIds) {
        await initPostByDeptIds(deptIds)
      }
      // 岗位标签回显
      if (postIds) {
        initPostTag(postIds)
      }
      // 默认部门回显
      sysUserDTO.value.defaultDeptId = resp.data.defaultDeptId
    } else {
      message.error(resp.msg)
    }
  }
  return {
    modalActive,
    segmentedOption,
    segmented,
    sysUserDTO,
    userRules,
    formRef,
    handleUpdateStatus,
    handleModelStatus,
    getUserInfo,
    toNextForm,
    changeSegmented,
    saveUser
  }

}
const {modalActive,segmented,segmentedOption,sysUserDTO,userRules,formRef,handleUpdateStatus, handleModelStatus,getUserInfo,toNextForm,changeSegmented,saveUser} = initSave()

// 加载角色
const initRoleData = () => {
  // 角色信息
  const sysRoleList = ref<Array<SysRole>>([])
  // 加载角色信息
  const initRole = async () => {
    const resp = await getRoleOption()
    if (resp.code === 200) {
      sysRoleList.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }
  initRole()
  return {
    sysRoleList
  }
}
const {sysRoleList} = initRoleData()

// 加载部门
const initDeptData = () => {
  // 部门信息
  const sysDeptList = ref<Array<SysDept>>([])
  // 加载部门信息
  const initDept = async () => {
    const resp = await getDeptOption()
    if (resp.code === 200) {
      // 单位树
      sysDeptList.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }
  initDept()
  return {
    sysDeptList
  }
}
const {sysDeptList} = initDeptData()

// 加载岗位
const initPostData = () => {

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
  // 加载loading
  const postLoading = ref<boolean>(false)

  // 进入岗位页面加载部门
  const toPostForm = async () => {
    try {
      if (sysUserDTO.value.deptIdList) {
        postLoading.value = true
        await initPostByDeptIds(sysUserDTO.value.deptIdList)
      }
    } finally {
      postLoading.value = false
    }
  }

  // 根据部门ID初始化岗位
  const initPostByDeptIds = async (deptIds: string[]) => {
    if (!deptIds.length) {
      sysPostList.value = []
      return
    }

    // 获取部门id｜名称集合
    const deptOptions = getDeptOptions(deptIds)

    await updatePostList(deptIds, deptOptions)
  }

  // 获取部门选项集合
  const getDeptOptions = (deptIds: string[]) => {
    const options: { value: string; label: string }[] = []

    deptIds.forEach(id => {
      traverse(sysDeptList.value, (dept) => {
        if (dept.id === id && dept.name) {
          options.push({
            value: dept.id,
            label: dept.name
          })
          return true
        }
      })
    })

    return options
  }

  // 更新岗位列表
  const updatePostList = async ( deptIds: string[], deptOptions: { label: string; value: string }[] ) => {

    const originDeptIds = sysPostList.value.map(p => p.deptId)

    // 需要删除的部门
    const removeDeptIds = originDeptIds.filter(id => !deptIds.includes(id))
    // 删除旧数据
    sysPostList.value = sysPostList.value.filter(item => !removeDeptIds.includes(item.deptId))
    // 新增部门
    const newDeptIds = deptIds.filter(id => !originDeptIds.includes(id))

    if (!newDeptIds.length) return

    const resp = await getPostOptionByDeptId(newDeptIds)

    if (resp.code !== 200) {
      message.error(resp.msg)
      return
    }

    newDeptIds.forEach(deptId => {
      const dept = deptOptions.find(d => d.value === deptId)

      sysPostList.value.push({
        deptId,
        deptName: dept?.label ?? '',
        postList: toPostOptional(resp.data[deptId])
      })
    })
  }

  const toPostOptional = (postList?: SysPost[]): PostOptional[] => {
    if (!postList) return []

    return postList.map(post => ({
      id: post.id,
      name: post.name,
      checked: false
    }))
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

  return {
    sysPostList,
    postLoading,
    initPostTag,
    toPostForm,
    handleSelectPostId,
    initPostByDeptIds
  }
}
const {sysPostList, postLoading, initPostTag, toPostForm ,handleSelectPostId,initPostByDeptIds} = initPostData()

// 删除
const intiDelete = () => {
  // 显示删除提示
  const openDeletePopconfirm = ref<boolean>(false);
  // 打开删除提示框
  const openPopconfirm = () => {
    if (selectedIds.value && selectedIds.value.length > 0) {
      openDeletePopconfirm.value = true
    } else {
      message.warning("请勾选数据")
    }
  }
  // 关闭删除提示框
  const closePopconfirm = () => {
    openDeletePopconfirm.value = false
  }

  // 处理删除逻辑
  const handleDelete = async (id?: string) => {
    const deleteIds = id ? [id] : [...selectedIds.value];

    if (deleteIds.length === 0) {
      message.warning("请勾选数据")
      return
    }

    try {
      const resp = await deleteByIds(deleteIds)
      if (resp.code === 200) {
        message.success(resp.msg);
        // id 不存在则清空选中数据
        if (!id) {
          selectedIds.value = []
        } else {
          selectedIds.value = selectedIds.value.filter(item => item !== id)
        }
        await initPage()
      } else {
        message.error(resp.msg)
      }
    } finally {
      closePopconfirm()
    }
  }

  return {
    handleDelete,
    closePopconfirm,
    openPopconfirm,
    openDeletePopconfirm
  }
}
const {handleDelete,closePopconfirm,openPopconfirm,openDeletePopconfirm} = intiDelete()

// 初始化excel导入导出相关操作
const initExcel = () => {

  // 下载excel模板
  const handleDownloadExcelTemplate = async () => {
    const spinInstance = Spin.service({
      tip: '努力加载中...'
    });
    const blob = await excelTemplate()
    download(blob, "用户导入模板")
    spinInstance.close()
  }

  // 导出excel
  const handleExportExcel = async () => {
    const spinInstance = Spin.service({
      tip: '努力加载中...'
    });
    try {
      // blob转为url后进行下载
      const blob = await exportExcel(userQuery.value)
      download(blob, "导出用户")
    } catch (err) {
      message.error("导出失败")
    } finally {
      spinInstance.close()
    }
  }

  // 文件上传前校验格式
  const handleBeforeUpdate = (file: File) => {
    const fileName = file.name
    if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
      message.warn("请上传 .xls 或 .xlsx 类型的文件")
      return false
    }
  }

  // excel批量导入
  const handleCustomRequest = async (uploadRequest: UploadRequestOption) => {
    if (!uploadRequest) {
      return
    }
    const spinInstance = Spin.service({
      tip: '数据处理中，请稍等...'
    })
    // 将文件上传至后端
    try {
      const resp = await importExcel(uploadRequest.file)
      if (resp.code === 200) {
        await initPage()
      } else if (resp.code === 510) {
        const errMsgList = JSON.parse(resp.data) as string[]
        Modal.confirm({
          title: '导入失败',
          icon: h(ExclamationCircleOutlined),
          width: 600,
          content: h('div', errMsgList.map(item => h('div', item))),
          okCancel: false
        })
      } else {
        message.error(resp.msg)
      }
    } finally {
      spinInstance.close()
    }
  }

  return {
    handleDownloadExcelTemplate,
    handleExportExcel,
    handleBeforeUpdate,
    handleCustomRequest
  }
}
const { handleDownloadExcelTemplate, handleExportExcel, handleBeforeUpdate, handleCustomRequest } = initExcel()

// 重置密码
const initResetPassword = () => {
  // 控制重置密码模态框
  const showResetPassword = ref<boolean>(false)
  // 目标用户
  const targetUserInfo = ref<SysUserVO>({})
  // 使用默认密码开关
  const useDefaultPassword = ref<boolean>(true)
  // 正在保存按钮加载
  const resetPasswordLoading = ref<boolean>(false)
  // 表单实例
  const resetPasswordRef = useTemplateRef<FormInstance>("resetPasswordRef")
  // 是否使用默认密码
  const handleChangeUseDefaultPassword = () => {
    if (!useDefaultPassword.value) {
      resetPasswordForm.value.password = ''
      resetPasswordRef.value?.validate()
    } else {
      resetPasswordForm.value.password = defaultPassword.value
      resetPasswordRef.value?.clearValidate()
    }
  }
  // 表单验证
  const defaultPasswordRules: Record<string, Rule[]> = {
    password: [
      {required: true, message: "请填写密码", trigger: ['blur', 'change']},
      { min: 6, max: 22, message: '密码长度6-22位', trigger: ['blur', 'change']}
    ]
  }
  // 重置密码表单
  const resetPasswordForm = ref<{password?: string}>({password: ''})
  // 触发打开模态框
  const handleOpenResetPasswordModel = (event: MouseEvent, targetUser: SysUserVO) => {
    event.stopPropagation()
    resetPasswordForm.value.password = defaultPassword.value
    targetUserInfo.value = targetUser
    showResetPassword.value = true
  }
  // 处理修改密码
  const handleResetPassword = async () => {
    await resetPasswordRef.value?.validate()
    resetPasswordLoading.value = true
    const password = resetPasswordForm.value.password
    const id = targetUserInfo.value.id
    try {
      if (password && id) {
        // 修改密码
        const resp = await resetPassword(id, password)
        if (resp.code === 200) {
          showResetPassword.value = false
          message.success(resp.msg)
        } else {
          message.error(resp.msg)
        }
      }
    } finally {
      resetPasswordLoading.value = false
    }
  }

  return {
    showResetPassword,
    targetUserInfo,
    resetPasswordForm,
    useDefaultPassword,
    defaultPasswordRules,
    resetPasswordLoading,
    handleChangeUseDefaultPassword,
    handleOpenResetPasswordModel,
    handleResetPassword
  }
}
const {showResetPassword, targetUserInfo, resetPasswordForm, useDefaultPassword, defaultPasswordRules, resetPasswordLoading, handleChangeUseDefaultPassword, handleOpenResetPasswordModel, handleResetPassword} = initResetPassword()

// 加载默认密码
const initDefaultPassword = async () => {
  defaultPassword.value = await settingStore.fetchDefaultPassword()
}

onMounted(() => {
  initPage()
  initDefaultPassword()
})
</script>
