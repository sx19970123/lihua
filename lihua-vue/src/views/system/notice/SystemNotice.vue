<template>
  <div>
    <a-flex :gap="16" vertical>
<!--      检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="公告标题">
                <a-input placeholder="请输入公告标题" v-model:value="noticeQuery.title" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="公告类型">
                <a-select placeholder="请选择" v-model:value="noticeQuery.type" allow-clear>
                  <a-select-option v-for="item in sys_notice_type" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="公告状态">
                <a-select placeholder="请选择" v-model:value="noticeQuery.status" allow-clear>
                  <a-select-option v-for="item in sys_notice_status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item>
                <a-space size="small">
                  <a-button type="primary" @click="initPage" :loading="tableLoad">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button @click="reloadPage" :loading="tableLoad">
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
<!--      列表-->
      <a-table :row-selection="noticeRowSelectionType"
               :data-source="noticeList"
               :columns="noticeColumn"
               row-class-name="hover-cursor-pointer"
               :custom-row="handleRowClick"
               :pagination="false"
               :loading="tableLoad"
               row-key="id"
               :scroll="{x: 1500}"
      >
        <template #title>
          <a-flex :gap="8">
            <a-button type="primary" @click="handleModalStatus('新增通知公告')">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          ok-text="确 定"
                          cancel-text="取 消"
                          @confirm="handleDelete(undefined)"
                          :open="openDeletePopconfirm"
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
            <!--            表格设置-->
            <table-setting v-model="noticeColumn"/>
          </a-flex>
        </template>

        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'title'">
            <a-tooltip ellipsis>
              <template #title>
                {{text}}
              </template>
              <a-typography-link @click="(event:MouseEvent) => showPreview(event, record.id)">
                {{text}}
              </a-typography-link>
            </a-tooltip>
          </template>
          <template v-if="column.key === 'type'">
            <dict-tag :dict-data-option="sys_notice_type" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'status'">
            <dict-tag :dict-data-option="sys_notice_status" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'priority'">
            <dict-tag :dict-data-option="sys_notice_priority" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'userScope'">
            <dict-tag :dict-data-option="sys_notice_user_scope" :dict-data-value="text"/>
          </template>
          <template v-if="column.key === 'createTime'">
            {{dayjs(text).format('YYYY-MM-DD HH:mm')}}
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" @click="(event:MouseEvent) => selectById(event, record.id, record.status)">
              <template #icon>
                <EditOutlined />
              </template>
              编辑
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small" v-if="record.status === '1'" @click="(event:MouseEvent) => handleRevoke(event, record.id)">
              <template #icon>
                <Cancel />
              </template>
              撤销
            </a-button>
            <a-button type="link" size="small" v-else @click="(event:MouseEvent) => handleRelease(event, record.id)">
              <template #icon>
                <PublishAir />
              </template>
              发布
            </a-button>
            <a-divider type="vertical"/>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          placement="bottomRight"
                          ok-text="确 定"
                          cancel-text="取 消"
                          @confirm="handleDelete(record.id)"
            >
              <a-button type="link" size="small" danger @click="(event:MouseEvent) => event.stopPropagation()">
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
            <a-pagination v-model:current="noticeQuery.pageNum"
                          v-model:page-size="noticeQuery.pageSize"
                          show-size-changer
                          :total="noticeTotal"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="initPage"/>
          </a-flex>
        </template>
      </a-table>
    </a-flex>
<!--    保存修改模态框-->
    <a-modal v-model:open="modalActive.open" :width="960" @ok="saveNotice" destroy-on-close>
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
        </div>
      </template>
      <a-form :colon="false" ref="formRef" :rules="noticeRoles" :model="sysNoticeVO" :label-col="{span: 2}">
        <a-form-item label="公告标题" name="title" :wrapper-col="{span: 8}">
          <a-input v-model:value="sysNoticeVO.title" placeholder="请输入标题" :maxlength="80" show-count/>
        </a-form-item>
        <a-form-item label="优先级别" :wrapper-col="{span: 8}">
          <color-select v-model:value="sysNoticeVO.priority" :data-source="priorityOption"/>
        </a-form-item>
        <a-form-item label="公告类型">
          <a-radio-group v-model:value="sysNoticeVO.type">
            <a-radio v-for="item in sys_notice_type" :value="item.value">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="用户范围" :wrapper-col="{span: 8}">
          <a-radio-group v-model:value="sysNoticeVO.userScope">
            <a-radio v-for="item in sys_notice_user_scope" :value="item.value">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="指定用户" name="userIdList" :wrapper-col="{span: 8}" v-if="sysNoticeVO.userScope === '1'">
          <a-flex :gap="8">
            <a-form-item-rest>
              <a-tooltip>
                <template #title v-if="sysNoticeVO.userIdList && sysNoticeVO.userIdList?.length > 0">
                  {{selectUserInfo}}
                </template>
                <a-input placeholder="请选择用户"
                         readonly
                         v-model:value="selectUserInfo"
                         :addon-after="sysNoticeVO.userIdList?.length + ' 人'" />
              </a-tooltip>
            </a-form-item-rest>
            <a-form-item-rest>
              <a-popover trigger="click"
                         destroyTooltipOnHide
                         :overlayInnerStyle="{maxWidth: 'calc(100vw - 48px)', marginLeft: '24px', marginRight: '24px'}"
                         :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
                <template #content>
                  <user-select :bordered="false"
                               :width="700"
                               :body-style="{padding: '8px'}"
                               v-model:id="sysNoticeVO.userIdList"
                               @change="handleSelectUserInfo"
                  />
                </template>
                <a-button>
                  <template #icon>
                    <SearchOutlined />
                  </template>
                </a-button>
              </a-popover>
            </a-form-item-rest>
          </a-flex>
        </a-form-item>
        <a-form-item label="内容">
          <editor height="300px" v-model="sysNoticeVO.content"/>
        </a-form-item>
      </a-form>
    </a-modal>
<!--    公告预览-->
    <a-modal v-model:open="previewModelOpen" :footer="false" :width="960" destroy-on-close>
      <notice-preview :notice-id="previewNoticeId" :show-read-user="true"/>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import {initDict} from "@/utils/Dict.ts";
import {reactive, ref, useTemplateRef} from "vue";
import type {SysNotice, SysNoticeDTO, SysNoticeVO} from "@/api/system/noice/type/SysNotice.ts";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {deleteByIds, queryById, queryPage, release, revoke, save} from "@/api/system/noice/Notice.ts";
import DictTag from "@/components/dict-tag/index.vue"
import {type FormInstance, message} from "ant-design-vue";
import dayjs from "dayjs";
import Editor from "@/components/editor/index.vue"
import ColorSelect from "@/components/color-select/index.vue"
import UserSelect from "@/components/user-select/index.vue"
import NoticePreview from "@/components/notice-preview/index.vue"
import type {SysUser} from "@/api/system/user/type/SysUser.ts";
import type {Rule} from "ant-design-vue/es/form";
import {getUserOptionByUserIds} from "@/api/system/user/User.ts";
import PublishAir from "@/components/icon/publish-air/PublishAir.vue";
import Cancel from "@/components/icon/cancel/Cancel.vue";
import {ResponseError} from "@/api/global/Type.ts";
import settings from "@/settings.ts";
import TableSetting from "@/components/table-setting/index.vue";
const {sys_notice_type, sys_notice_status, 	sys_notice_user_scope, sys_notice_priority} = initDict("sys_notice_type", "sys_notice_status", "sys_notice_user_scope", "sys_notice_priority")
// 查询列表
const initSearch = () => {
  // 列表多选
  const selectedIds = ref<Array<string>>([])
  const noticeRowSelectionType = {
    columnWidth: '55px',
    type: 'checkbox',
    // 支持跨页勾选
    preserveSelectedRowKeys: true,
    // 指定选中id的数据集合，操作完后可手动清空
    selectedRowKeys: selectedIds,
    onChange: (ids: Array<string>) => {
      selectedIds.value = ids
    }
  }
  // 处理选择
  const handleRowClick = (record: SysNotice) => {
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

  // 列
  const noticeColumn = ref<ColumnsType>([
    {
      title: '公告标题',
      key: 'title',
      dataIndex: 'title',
      ellipsis: true,
    },
    {
      title: '公告类型',
      key: 'type',
      dataIndex: 'type',
      align: 'center'
    },
    {
      title: '公告状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center'
    },
    {
      title: '发送范围',
      key: 'userScope',
      dataIndex: 'userScope',
      align: 'center'
    },
    {
      title: '优先级别',
      key: 'priority',
      dataIndex: 'priority',
      align: 'center'
    },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center',
      width: '160px'
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '264px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    }
  ])

  // 查询条件
  const noticeQuery = ref<SysNoticeDTO>({
    pageNum: 1,
    pageSize: 10,
  })

  const noticeTotal = ref<number>()
  const noticeList = ref<Array<SysNotice>>([])
  const tableLoad = ref<boolean>(false)

  const handleQueryPage = async () => {
    noticeQuery.value.pageNum = 1
    await initPage()
  }

  const reloadPage = async () => {
    noticeQuery.value = {
      pageNum: 1,
      pageSize: 10
    }
    await initPage()
  }

  const initPage = async () => {
    tableLoad.value = true
    try {
      const resp = await queryPage(noticeQuery.value)
      if (resp.code === 200) {
        noticeList.value = resp.data.records
        noticeTotal.value = resp.data.total
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
      tableLoad.value = false
    }
  }
  handleQueryPage()
  return {
    selectedIds,
    noticeRowSelectionType,
    noticeColumn,
    noticeQuery,
    noticeList,
    tableLoad,
    noticeTotal,
    handleRowClick,
    handleQueryPage,
    initPage,
    reloadPage,
  }
}
const {selectedIds, noticeRowSelectionType, noticeColumn, noticeQuery, noticeList, tableLoad, noticeTotal, handleRowClick, handleQueryPage, initPage, reloadPage} = initSearch()

// 表单保存
const initSave = () => {
  const formRef = useTemplateRef<FormInstance>("formRef")

  // 模态框状态
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string // 模态框标题
  }
  const modalActive = reactive<modalActiveType>({
    open: false,
    saveLoading: false,
    title: ""
  })

  // 将优先级选项处理为color-select组件可以处理的数据类型
  const priorityOption = ref<Array<{
    name: string,
    color: string,
    key?: string
  }>>([])
  // 处理优先级字典数据
  const handlePriorityOption = () => {
    const dictPriority = sys_notice_priority.value
    dictPriority.forEach(item => {
      priorityOption.value.push({
        name: item.label ? item.label : '',
        color: item.tagStyle ? item.tagStyle : '',
        key: item.value,
      })
    })
  }

  // 选择用户范围描述
  const selectUserInfo = ref<String>('')

  // 处理显示已选择用户
  const handleSelectUserInfo = (userList: SysUser[]) => {
    const length = userList.length;
    if (length === 0) {
      selectUserInfo.value = ''
      return;
    }
    const nicknameList = userList.map(user => user.nickname);
    selectUserInfo.value = nicknameList.join("、") ;
  };

  const noticeRoles: Record<string, Rule[]> = {
    title: [
      {required: true, message: "请填写标题", trigger: "change"}
    ],
    userIdList: [
      {required: true, message: "请选择用户", trigger: "change"}
    ]
  }

  // notice表单对象
  const sysNoticeVO = ref<SysNoticeVO>({})

  // 处理模态框状态
  const handleModalStatus = (title?: string) => {
    modalActive.open = !modalActive.open
    if(modalActive.open && priorityOption.value.length === 0) {
      handlePriorityOption()
    }
    if (title) {
      modalActive.title = title
    }
    // 重置表单
    sysNoticeVO.value = {
      type: '0',
      status: '0',
      priority: '2',
      userScope: '0',
      userIdList: [],
      content: ''
    }
    // 清空回显用户
    handleSelectUserInfo([])
  }

  // 保存消息通知
  const saveNotice = async () => {
    await formRef.value?.validate()

    try {
      const resp = await save(sysNoticeVO.value);
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
    }
  }

  // 根据id查询通知
  const selectById = async (event:MouseEvent, id: string, status: string) => {
    event.stopPropagation()
    if (status === '1') {
      message.error('已发布消息通知无法编辑')
      return
    }
    try {
      const resp = await queryById(id)
      if (resp.code === 200) {
        handleModalStatus("修改通知公告")
        sysNoticeVO.value = resp.data
        if (resp.data.userScope === '1') {
          const length = resp.data?.userIdList?.length
          if (length && length > 0) {
            try {
              const selectUserList = await getUserOptionByUserIds(resp.data.userIdList as string[])
              if (selectUserList.code === 200) {
                handleSelectUserInfo(selectUserList.data)
              } else {
                message.error(resp.msg)
              }
            } catch (e) {
              if (e instanceof ResponseError) {
                message.error(e.msg)
              } else {
                console.log(e)
              }
            }
          }
        }
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.log(e)
      }
    }
  }
  return {
    modalActive,
    priorityOption,
    sysNoticeVO,
    selectUserInfo,
    formRef,
    noticeRoles,
    handleSelectUserInfo,
    handleModalStatus,
    saveNotice,
    selectById
  }
}
const {modalActive, priorityOption, sysNoticeVO, selectUserInfo, formRef, noticeRoles, handleSelectUserInfo, handleModalStatus, saveNotice, selectById} = initSave()

const initDelete = () => {
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
  // 处理删除数据
  const handleDelete = async (id?: string) => {
    const deleteIds = id ? [id] : [...selectedIds.value];
    if (deleteIds.length > 0) {
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
      } catch (e) {
        if (e instanceof ResponseError) {
          message.error(e.msg)
        } else {
          console.log(e)
        }
      }
    } else {
      message.warning("请勾选数据")
    }

  }

  return {
    openDeletePopconfirm,
    closePopconfirm,
    handleDelete,
    openPopconfirm
  }
}

const { openDeletePopconfirm,closePopconfirm,handleDelete,openPopconfirm } = initDelete()

// 初始化预览
const initPreview = () => {
  const previewModelOpen = ref<boolean>(false)
  const previewNoticeId = ref<string>('')

  const showPreview = async (event:MouseEvent, id: string) => {
    event.stopPropagation()
    previewNoticeId.value = id
    previewModelOpen.value = true
  }
  return {
    previewModelOpen,
    previewNoticeId,
    showPreview
  }
}

const { previewModelOpen, previewNoticeId, showPreview } = initPreview()

// 处理文章发布
const handleRelease = async (event:MouseEvent, id: string) => {
  event.stopPropagation()
  try {
    const resp = await release(id)
    if (resp.code === 200) {
      await initPage()
      message.success(resp.msg)
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

// 处理文章撤销
const handleRevoke = async (event:MouseEvent, id: string) => {
  event.stopPropagation()
  try {
    const resp = await revoke(id)
    if (resp.code === 200) {
      await initPage()
      message.success(resp.msg)
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


</script>
<style scoped>

</style>
