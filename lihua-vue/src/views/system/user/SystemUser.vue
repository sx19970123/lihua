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
                <span v-if="selectedIds && selectedIds.length > 0" style="margin-left: 4px"> {{selectedIds.length}} 项</span>
              </a-button>
            </a-popconfirm>
            <div v-show="showMore">
              <a-flex :gap="8">
                <a-button ghost type="primary" @click="handleExportExcel">
                  <template #icon>
                    <ExportOutlined />
                  </template>
                  导出
                </a-button>
                <a-popover title="导入说明">
                  <template #content>
                    <a-space direction="vertical">
                      <a-typography-text>1. 请参考“导出功能”下载的Excel作为导入模板</a-typography-text>
                      <a-typography-text>2. 以用户名为准，保证全局唯一性。遇到重复数据将无法导入</a-typography-text>
                      <a-typography-text>3. 带有*标记的为必填字段</a-typography-text>
                      <a-typography-text>4. 涉及到角色、部门信息、岗位信息请确保系统中录入了对应数据</a-typography-text>
                      <a-typography-text>5. 无法导入的数据会被收集并导出，同时标记错误信息，修改后可重新导入</a-typography-text>
                    </a-space>
                  </template>
                  <a-upload :customRequest="handleCustomRequest"
                            :beforeUpload="handleBeforeUpdate"
                            :showUploadList="false"
                            accept=".xlsx,.xls"
                  >

                      <a-button ghost type="primary">
                        <template #icon>
                          <ImportOutlined />
                        </template>
                        导入
                      </a-button>
                  </a-upload>
                </a-popover>
              </a-flex>
            </div>
            <a-button ghost type="primary" @click="showMore = !showMore">
              <template #icon>
                <DoubleLeftOutlined v-if="showMore" />
                <DoubleRightOutlined v-else />
              </template>
            </a-button>

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
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
      <a-segmented v-model:value="segmented" :options="segmentedOption" style="margin-bottom: 16px" @change="changeSegmented"/>
      <a-form ref="formRef" :rules="userRules" :model="sysUserDTO" :label-col="{span: 4}" :colon="false">
<!--        显示基本信息-->
        <div v-show="segmented === 'basic'">
          <a-form-item label="用户名" name="username" :wrapper-col="{span: 16}">
            <a-input v-model:value="sysUserDTO.username" placeholder="用于用户登陆" :maxlength="30" show-count allowClear/>
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
                <div style="margin-top: 16px;">
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
         <div style="margin-bottom: 24px">
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
         <div style="margin-top: 8px;">
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
  queryPage,
  queryById,
  save,
  deleteByIds,
  updateStatus,
  exportExcel,
  importExcel,
  resetPassword
} from "@/api/system/user/User.ts"
import {initDict} from "@/utils/Dict.ts"
import {createVNode, onMounted, reactive, ref, useTemplateRef, watch} from "vue";
import SelectableCard from "@/components/selectable-card/index.vue"
import PasswordInput from "@/components/password-input/index.vue"
import DictTag from "@/components/dict-tag/index.vue"
import EasyTreeSelect from "@/components/easy-tree-select/index.vue"
import TableSetting from "@/components/table-setting/index.vue";
import dayjs from "dayjs";
import {getDeptOption} from "@/api/system/dept/Dept.ts";
import {getRoleOption} from "@/api/system/role/Role.ts";
import {getPostOptionByDeptId} from "@/api/system/post/Post.ts";
import {message, Modal, type FormInstance} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';
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
import type {DefaultPassword} from "@/api/system/setting/type/DefaultPassword.ts";
import {defaultPasswordDecrypt, rasEncryptPassword} from "@/utils/Crypto.ts";
import {ResponseError} from "@/api/global/Type.ts";
import {download} from "@/utils/AttachmentDownload.ts";
import settings from "@/settings.ts";
const easyTreeSelectRef = useTemplateRef<InstanceType<typeof EasyTreeSelect>>("easyTreeSelectRef")
const settingStore = useSettingStore()
const {sys_status, user_gender, sys_user_register_type} = initDict("sys_status", "user_gender", "sys_user_register_type")
// 显示更多按钮
const showMore = ref<boolean>(false)
// 默认密码
const defaultPassword = ref<string>('')
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
      ellipsis: true,
    },
    {
      title: '操作',
      key: 'action',
      dataIndex: 'action',
      align: 'center',
      width: '292px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
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
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
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
  const segmentedOption = reactive<Array<{
    value: 'basic' | 'dept' | 'post',
    label: string
  }>>([{
    value: 'basic',
    label: '基础信息',
  }, {
    value: 'dept',
    label: '角色&部门'
  }, {
    value: 'post',
    label: '岗位&默认部门'
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
  const toNextForm = (nextSegmentedValue?: 'basic' | 'dept' | 'post') => {
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
    // 重制部门岗位
    initPostByDeptIds([])
    initPostTag([])
    // 重置树形选择组件
    easyTreeSelectRef.value?.reset()
  }

  // 保存用户信息
  const saveUser = async () => {
    try {
      await formRef.value?.validate()
      modalActive.saveLoading = true
      const userDTO = cloneDeep(sysUserDTO.value)
      // 处理用户部门、手机号、邮箱
      userDTO.deptIdList = sysUserDTO.value.deptIdList
      userDTO.phoneNumber === "" ? sysUserDTO.value.phoneNumber = undefined : sysUserDTO.value.phoneNumber
      userDTO.email === "" ? sysUserDTO.value.email = undefined : sysUserDTO.value.email

      const userId = sysUserDTO.value.id
      const password = sysUserDTO.value.password

      // 操作为新增用户时，进行密码加密
      if (!userId && password) {
        try {
          const passwordEncrypt = await rasEncryptPassword(password)
          userDTO.password = passwordEncrypt.ciphertext
          userDTO.passwordRequestKey = passwordEncrypt.requestKey
        } catch (error) {
          message.success(error as string)
        }
      }

      // 调用保存接口
      const resp = await save(userDTO)
      if (resp.code === 200) {
        message.success(resp.msg)
        modalActive.open = false
        await initPage()
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
      // 出现表单验证信息后跳转到表单首页
      toNextForm('basic')
    } finally {
      modalActive.saveLoading = false
    }
  }

  // 处理改变状态
  const handleUpdateStatus = async (event: MouseEvent, id: string, status: string) => {
    event.stopPropagation()
    let newStatus: string = ''
    try {
      const resp = await updateStatus(id, status)
      if (resp.code === 200) {
        newStatus = resp.data
        message.success(resp.msg)
      } else {
        newStatus = status
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      // 重新赋值
      userList.value.some(user => {
        if (user.id === id) {
          user.status = newStatus
          user.statusIsNormal =  user.status === '0'
          user.updateStatusLoading = false
          return
        }
      })
    }

  }

  // 根据id查询用户信息
  const getUserInfo = async (event: MouseEvent, userId: string) => {
    event.stopPropagation()
    try {
      const resp = await queryById(userId)
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
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
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
    try {
      const resp = await getRoleOption()
      if (resp.code === 200) {
        sysRoleList.value = resp.data
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
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
    try {
      const resp = await getDeptOption()
      if (resp.code === 200) {
        // 单位树
        sysDeptList.value = resp.data
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
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
    } catch (error) {
      console.error(error)
    } finally {
      postLoading.value = false
    }
  }

  // 通过部门id获取部门名称用于回显
  const initPostByDeptIds = async (value: Array<string>) => {
    const option:Array<{
      value: string,
      label: string
    }> = []
    // 组合option
    value.forEach(item => {
      traverse(sysDeptList.value, (dept) => {
        if (dept.id === item && dept.name) {
          option.push({
            value: dept.id,
            label: dept.name
          })
        }
      })
    })

    if (value.length > 0) {
      await initPostByDeptIdOption(value, option)
    } else {
      sysPostList.value = []
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
    try {
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
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
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

    try {
      if (deleteIds.length > 0) {
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
      } else {
        message.warning("请勾选数据")
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
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
  // 导出excel
  const handleExportExcel = async () => {
    const spinInstance = Spin.service({
      tip: '努力加载中...'
    });
    const resp = await exportExcel(userQuery.value)
    if (resp.code === 200) {
      download(resp.data)
    } else {
      message.error(resp.msg)
    }
    spinInstance.close()
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
        const data = resp.data
        // 是否完全导入成功
        if (data.allSuccess) {
          message.success(resp.msg);
        } else {
          // 部分成功可下载导入失败的数据集
          Modal.confirm({
            title: '导入完成，部分数据未成功导入',
            icon: createVNode(ExclamationCircleOutlined),
            content: `共解析到 ${data.readCount} 条数据，成功导入 ${data.successCount} 条，失败 ${data.errorCount} 条。点击“确定”下载失败数据集。`,
            onOk: () => {
              // 下载导入失败excel
              download(data.errorExcelPath)
            }
          })
        }
        // 导入完成后刷新页面
        await initPage()
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      spinInstance.close()
    }

  }

  return {
    handleExportExcel,
    handleBeforeUpdate,
    handleCustomRequest
  }
}
const { handleExportExcel, handleBeforeUpdate, handleCustomRequest } = initExcel()

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
  const resetPasswordForm = ref<{
    password?: string
  }>({
    password: ''
  })
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
        // 密码加密处理
        const passwordEncrypt = await rasEncryptPassword(password)
        // 修改密码
        const resp = await resetPassword(id, passwordEncrypt.ciphertext, passwordEncrypt.requestKey)
        if (resp.code === 200) {
          showResetPassword.value = false
          message.success(resp.msg)
        } else {
          message.error(resp.msg)
        }
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
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
  const resp = await settingStore.getSetting<DefaultPassword>("DefaultPasswordSetting")
  if (resp) {
    defaultPassword.value = defaultPasswordDecrypt(resp.defaultPassword)
  }
}

onMounted(() => {
  initPage()
  initDefaultPassword()
})
</script>
