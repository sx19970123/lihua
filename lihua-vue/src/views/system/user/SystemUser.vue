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
                    :show-checked-strategy="SHOW_ALL"
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
                  <a-button type="primary" @click="queryPage" :loading="queryLoading">
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
      >
        <template #title>
          <a-flex :gap="8">
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
          </a-flex>
        </template>
        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'deptLabelList'">
            {{text ? text.join('、') : ''}}
          </template>
          <template v-if="column.key === 'createTime'">
            {{dayjs(text).format('YYYY-MM-DD HH:mm')}}
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

    <a-modal v-model:open="modalActive.open" class="unselectable">
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
              <a-input placeholder="检索部门树" v-model:value="deptKeyword" allowClear style="margin-bottom: 8px; height: 28px"/>
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
                  <div v-if="name.indexOf(deptKeyword) > -1">
                    <span>{{name.substring(0,name.indexOf(deptKeyword))}}</span>
                    <span :style="{'color':  themeStore.colorPrimary}">{{deptKeyword}}</span>
                    <span>{{name.substring(name.indexOf(deptKeyword) + deptKeyword.length)}}</span>
                  </div>
                  <span v-else>{{ name }}</span>
                </template>
              </a-tree>
            </div>
          </a-form-item>
        </div>
        <div v-show="segmented === 'post'">
          <a-form-item>
            <card-select
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
            </card-select>
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
  </div>
</template>

<script setup lang="ts">

// 列表查询
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {
  findPage,
  findById,
  save,
  deleteByIds,
  updateStatus,
  exportExcel,
  importExcel
} from "@/api/system/user/User.ts"
import {initDict} from "@/utils/Dict.ts"
import {createVNode, h, reactive, ref, watch} from "vue";
import CardSelect from "@/components/card-select/index.vue"
import dayjs from "dayjs";
import {getDeptOption} from "@/api/system/dept/Dept.ts";
import {getRoleOption} from "@/api/system/role/Role.ts";
import {getPostOptionByDeptId} from "@/api/system/post/Post.ts";
import {message, TreeSelect, Modal} from "ant-design-vue";
import { cloneDeep } from 'lodash-es';
import {flattenTreeData} from "@/utils/Tree.ts";
import type {Rule} from "ant-design-vue/es/form";
import type {SysUserDTO, SysUserVO} from "@/api/system/user/type/SysUser.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import type {SysRole} from "@/api/system/role/type/SysRole.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";
import {useThemeStore} from "@/stores/modules/theme.ts";
import {downloadByPath, handleFunDownload} from "@/utils/FileDownload.ts";
import type {UploadRequestOption} from "ant-design-vue/lib/vc-upload/interface";
import Spin from "@/components/spin";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import {useSettingStore} from "@/stores/modules/setting.ts";
import type {DefaultPassword} from "@/api/system/setting/type/DefaultPassword.ts";
const settingStore = useSettingStore()
const themeStore = useThemeStore();
const {sys_status,user_gender} = initDict("sys_status", "user_gender")
const SHOW_ALL = TreeSelect.SHOW_ALL;
// 没有进行双向绑定的单位树
let originDeptTree: Array<SysDept> = ([])
// 显示更多按钮
const showMore = ref<boolean>(false)
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
      width: '190px'
    }
  ]

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
    const resp = await findPage(userQuery.value)
    if (resp.code === 200) {
      userList.value = resp.data.records
      userTotal.value = resp.data.total

      // 回显状态
      userList.value.forEach(user => {
        user.statusIsNormal = user.status === '0'
        user.updateStatusLoading = false
      })
    }
    queryLoading.value = false
  }

  // 条件检索初始页码设置为0
  const queryPage = async () => {
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
    queryPage,
    resetPage
  }
}
const {userColumn,userQuery,userList,userTotal,queryLoading,userRowSelectionType,selectedIds,handleRowClick,initPage,queryPage,resetPage } = initSearch()
initPage()

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
      password: settingStore.getSetting<DefaultPassword>("DefaultPasswordSetting")?.defaultPassword
    }
    // 分段器设置为默认
    segmented.value = 'basic'
    // 重制部门岗位
    initPostByDeptIds([])
    initPostTag([])
  }

  // 保存用户信息
  const saveUser = async () => {
    try {
      await formRef.value.validate()
      modalActive.saveLoading = true
      sysUserDTO.value.deptIdList = handleDeptIdList()
      sysUserDTO.value.phoneNumber === "" ? sysUserDTO.value.phoneNumber = undefined : sysUserDTO.value.phoneNumber
      sysUserDTO.value.email === "" ? sysUserDTO.value.email = undefined : sysUserDTO.value.email
      const resp = await save(sysUserDTO.value)
      if (resp.code === 200) {
        message.success(resp.msg)
        modalActive.open = false
        await initPage()
      } else {
        message.error(resp.msg)
      }
    } catch (error) {
      // 出现表单验证信息后跳转到表单首页
      toNextForm('basic')
    } finally {
      modalActive.saveLoading = false
    }
  }

  // 处理改变状态
  const handleUpdateStatus = async (event: MouseEvent, id: string, status: string) => {
    event.stopPropagation()
    const resp = await updateStatus(id, status)
    let newStatus: string = ''
    if (resp.code === 200) {
      newStatus = resp.data
      message.success(resp.msg)
    } else {
      newStatus = status
      message.error(resp.msg)
    }
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

  // 根据id查询用户信息
  const getUserInfo = async (event: MouseEvent, userId: string) => {
    event.stopPropagation()
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
    deptIds,
    handleExpanded,
    handleCheckedAllKeys,
    filterTreeByLabel
  }
}
const {sysDeptList,deptTreeSetting,deptKeyword,flattenDeptList,deptIds,handleExpanded, handleCheckedAllKeys,filterTreeByLabel} = initDeptData()

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
      postLoading.value = true
      await initPostByDeptIds(handleDeptIdList())
    } catch (error) {
      console.error(error)
    } finally {
      postLoading.value = false
    }
  }

  // 将 {checked: string[]} 或 string[] 数据处理为统一 数组
  const handleDeptIdList = (): string[] => {
    const obj = sysUserDTO.value.deptIdList as {checked: string[]} | string[]
    let ids = []
    if ((obj as {checked: string[]}).checked) {
      ids = (obj as {checked: string[]}).checked
    } else {
      ids = (obj as string[])
    }
    return ids
  }

  // 通过部门id获取部门名称用于回显
  const initPostByDeptIds = async (value: Array<string>) => {
    const option:Array<{
      value: string,
      label: string
    }> = []
    // 组合option
    value.forEach(item => {
      flattenDeptList.forEach(dept => {
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
    initPostByDeptIds,
    handleDeptIdList
  }
}
const {sysPostList, postLoading, initPostTag, toPostForm ,handleSelectPostId,initPostByDeptIds,handleDeptIdList} = initPostData()

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
    closePopconfirm()
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
  const handleExportExcel = () => {
    handleFunDownload(exportExcel(userQuery.value))
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
            downloadByPath(data.errorExcelPath)
          }
        })
      }
      // 导入完成后刷新页面
      await initPage()
    } else {
      message.error(resp.msg)
    }
    spinInstance.close()
  }

  return {
    handleExportExcel,
    handleBeforeUpdate,
    handleCustomRequest
  }
}

const { handleExportExcel, handleBeforeUpdate, handleCustomRequest } = initExcel()

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

// 监听反向选中标签
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
})
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
