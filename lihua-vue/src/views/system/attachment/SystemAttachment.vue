<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--      检索条件-->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="附件名称">
                <a-input placeholder="请输入附件名称" v-model:value="attachmentQuery.originalName" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="所属模块">
                <a-input placeholder="请输入所属模块" v-model:value="attachmentQuery.businessName" allowClear/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="附件状态">
                <a-select placeholder="请选择附件状态" v-model:value="attachmentQuery.status" allowClear>
                  <a-select-option v-for="item in sys_attachment_status" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="上传方式">
                <a-select placeholder="请选择上传方式" v-model:value="attachmentQuery.uploadMode" allowClear>
                  <a-select-option v-for="item in sys_attachment_upload_mode" :value="item.value">{{item.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="上传日期">
                <a-range-picker v-model:value="attachmentQuery.createTimeList" allowClear></a-range-picker>
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
      <a-table :pagination="false"
               :data-source="attachmentList"
               :row-selection="attachmentRowSelectionType"
               :custom-row="handleRowClick"
               :columns="attachmentColumn"
               :loading="tableLoad"
               row-class-name="hover-cursor-pointer"
               row-key="id"
               :scroll="{x: 1500}">
        <template #title>
          <a-flex :gap="8">
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
            <!--            表格设置-->
            <table-setting v-model="attachmentColumn"/>
          </a-flex>
        </template>
        <template #bodyCell="{column, record}">
          <template v-if="column.key === 'originalName'">
            <a-typography-link @click="(event: MouseEvent) => handleOpenInfoModal(event, record.id)">{{record[column.key]}}</a-typography-link>
          </template>
          <template v-if="column.key === 'size'">
            {{convertFileSize(record[column.key])}}
          </template>
          <template v-if="column.key === 'createTime'">
            {{dayjs(record[column.key]).format('YYYY-MM-DD HH:mm')}}
          </template>
          <template v-if="column.key === 'status'">
            <dict-tag :dict-data-value="record[column.key]" :dict-data-option="sys_attachment_status"/>
          </template>
          <template v-if="column.key === 'uploadMode'">
            <dict-tag :dict-data-value="record[column.key]" :dict-data-option="sys_attachment_upload_mode"/>
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" @click="(event: MouseEvent) => handleDownload(event, record.id, record.status)">
              <template #icon>
                <CloudDownloadOutlined />
              </template>
              下载
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="link" size="small" @click="(event: MouseEvent) => handleShowShareModal(event, record.id, record.originalName, record.status)">
              <template #icon>
                <ShareAltOutlined />
              </template>
              分享
            </a-button>
            <a-divider type="vertical"/>
            <a-dropdown>
              <a class="ant-dropdown-link" @click="(event: MouseEvent) => event.stopPropagation()">
                <DownOutlined />
                更多
              </a>
              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <a-popconfirm title="删除后不可恢复，是否删除？"
                                  placement="topRight"
                                  ok-text="确 定"
                                  cancel-text="取 消"
                                  @confirm="handleDelete(record.id)"
                    >
                      <a-button type="link" size="small" danger @click="(event:MouseEvent) => event.stopPropagation()">
                        <template #icon>
                          <DeleteOutlined />
                        </template>
                        删除附件
                      </a-button>
                    </a-popconfirm>
                  </a-menu-item>
                  <a-menu-item>
                    <a-popconfirm placement="bottomRight"
                                  :ok-text="countdown === 0 ? '确 认' : '确 认 ' + countdown"
                                  :okButtonProps="{disabled: countdown !== 0}"
                                  cancel-text="取 消"
                                  @confirm="handleForceDelete(record.id)"
                                  @open-change="(open: boolean) => startForceDeleteCountdown(open, 5)"
                    >
                      <template #icon>
                        <InfoCircleFilled :style="{color: themeStore.isDarkTheme ? '#dc4446' : '#ff4d4f'}"/>
                      </template>
                      <template #title>
                        <div>强制删除 {{record.originalName}}</div>
                      </template>
                      <template #description>
                        删除后可能导致业务异常且不可恢复，请确保附件已不再使用。是否删除？
                      </template>
                      <a-button type="link" size="small" danger @click="(event:MouseEvent) => event.stopPropagation()">
                        <template #icon>
                          <DeleteFilled />
                        </template>
                        强制删除
                      </a-button>
                    </a-popconfirm>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>

          </template>
        </template>
        <template #footer>
          <a-flex justify="flex-end">
            <a-pagination v-model:current="attachmentQuery.pageNum"
                          v-model:page-size="attachmentQuery.pageSize"
                          show-size-changer
                          :total="attachmentTotal"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="initPage"/>
          </a-flex>
        </template>
      </a-table>
    </a-flex>
<!--    详情模态框-->
    <a-modal v-model:open="showInfoModal" @cancel="handleCloseInfoModal" width="1000px" :footer="null">
      <a-descriptions title="附件详情" bordered :label-style="{width: '110px'}">
        <!-- 文件信息 -->
        <a-descriptions-item label="附件名称" :span="1">
          <a-typography-link v-if="attachmentInfo.status === '0' && attachmentInfo.type?.startsWith('image')" @click="() => handlePreview(attachmentInfo.id, attachmentInfo.type)">{{attachmentInfo.originalName}}</a-typography-link>
          <span v-else>{{attachmentInfo.originalName}}</span>
        </a-descriptions-item>
        <a-descriptions-item label="存储路径" :span="2">{{attachmentInfo.path}}</a-descriptions-item>
        <a-descriptions-item label="附件大小" :span="1">{{convertFileSize(attachmentInfo.size)}}</a-descriptions-item>

        <a-descriptions-item label="附件类型" :span="1">{{attachmentInfo.type}}</a-descriptions-item>
        <a-descriptions-item label="服务类型" :span="1">{{attachmentInfo.storageLocation}}</a-descriptions-item>

        <!-- 业务信息 -->
        <a-descriptions-item label="业务编码" :span="1">{{attachmentInfo.businessCode}}</a-descriptions-item>
        <a-descriptions-item label="业务名称" :span="1">{{attachmentInfo.businessName}}</a-descriptions-item>

        <!-- 上传信息 -->
        <a-descriptions-item label="上传用户" :span="1">{{attachmentInfo.uploadName}}</a-descriptions-item>
        <a-descriptions-item label="上传时间" :span="1">{{dayjs(attachmentInfo.createTime).format("YYYY-MM-DD HH:mm:ss")}}</a-descriptions-item>
        <a-descriptions-item label="上传方式" :span="1">
          <dict-tag :dict-data-value="attachmentInfo.uploadMode ? attachmentInfo.uploadMode : ''" :dict-data-option="sys_attachment_upload_mode"/>
        </a-descriptions-item>
        <!-- 附件状态 -->
        <a-descriptions-item label="附件状态" :span="1">
          <dict-tag :dict-data-value="attachmentInfo.status ? attachmentInfo.status : ''" :dict-data-option="sys_attachment_status"/>
        </a-descriptions-item>
        <!-- 异常信息 -->
        <a-descriptions-item label="异常信息" :span="3" v-if="attachmentInfo.status === '1'">{{attachmentInfo.errorMsg}}</a-descriptions-item>
        <!-- url上传展示原url -->
        <a-descriptions-item label="原URL" :span="3" v-if="attachmentInfo.url">{{attachmentInfo.url}}</a-descriptions-item>
        <!-- 分片上传Id -->
        <a-descriptions-item label="分片标识" :span="3" v-if="attachmentInfo.uploadId">{{attachmentInfo.uploadId}}</a-descriptions-item>
        <!-- MD5 -->
        <a-descriptions-item label="MD5" :span="3" v-if="attachmentInfo.md5">{{attachmentInfo.md5}}</a-descriptions-item>
      </a-descriptions>
      <!--      图片预览-->
      <a-image :src="attachmentInfo.id ? previewUrlMap.get(attachmentInfo.id) : ''" :style="{ display: 'none' }" :preview="{ visible, onVisibleChange: () => handlePreview(attachmentInfo.id, attachmentInfo.type)}"></a-image>
    </a-modal>
<!--    分享模态框-->
    <a-modal v-model:open="showShareModal" @cancel="handleCloseShareModal">
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{shareName}}</a-typography-title>
        </div>
      </template>
      <a-form :colon="false">
        <a-form-item label="有效时间">
          <a-input-number placeholder="请输入链接有效时间"
                          addon-after="分钟"
                          :min="1"
                          :precision="0"
                          v-model:value="shareValidTime"
                          @blur="handleGetShareUrl"
          />
        </a-form-item>
        <a-form-item label="附件链接">
          <a-textarea :auto-size="{ minRows: 3 }"
                      v-model:value="shareUrl"
                      placeholder="有效时间失去焦点以获取分享链接"
                      readonly/>
        </a-form-item>
      </a-form>
      <template #footer>
        <a-button type="primary" @click="handleCopyToClipboard">
          <template #icon>
            <CopyOutlined />
          </template>
          复制链接到剪切板
        </a-button>
      </template>
    </a-modal>
  </div>
</template>

<script setup lang="ts">

// 查询列表
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {onMounted, onUnmounted, ref, watch} from "vue";
import type {SysAttachment, SysAttachmentDTO, SysAttachmentVO} from "@/api/system/attachment/type/SysAttachment.ts";
import {message} from "ant-design-vue";
import {ResponseError} from "@/api/global/Type.ts";
import {deleteData, forceDeleteData, getDownloadURL, queryById, queryPage} from "@/api/system/attachment/Attachment.ts";
import dayjs from "dayjs";
import {initDict} from "@/utils/Dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
import {download} from "@/utils/AttachmentDownload.ts";
import {useThemeStore} from "@/stores/theme.ts";
import settings from "@/settings.ts";
import TableSetting from "@/components/table-setting/index.vue";

const {sys_attachment_status, sys_attachment_upload_mode} = initDict("sys_attachment_status","sys_attachment_upload_mode")
const baseAPI = import.meta.env.VITE_APP_BASE_API
const themeStore = useThemeStore()

const initSearch = () => {
// 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const attachmentRowSelectionType = {
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
  const handleRowClick = (record: SysAttachment) => {
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

  const attachmentColumn = ref<ColumnsType>([
    {
      title: '附件名称',
      key: 'originalName',
      dataIndex: 'originalName',
      ellipsis: true
    },
    {
      title: '附件大小',
      key: 'size',
      dataIndex: 'size',
      align: "center"
    },
    {
      title: '存储路径',
      key: 'path',
      dataIndex: 'path',
      ellipsis: true
    },
    {
      title: '所属模块',
      key: 'businessName',
      dataIndex: 'businessName',
      align: 'center',
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center',
    },
    {
      title: '上传方式',
      key: 'uploadMode',
      dataIndex: 'uploadMode',
      align: 'center',
    },
    {
      title: '上传时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center',
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '244px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    }
  ])

  const reloadPage = async () => {
    attachmentQuery.value = {
      pageNum: 1,
      pageSize: 10
    }
    await initPage()
  }

  const attachmentQuery = ref<SysAttachmentDTO>({
    pageNum: 1,
    pageSize: 10
  })

  const attachmentTotal = ref<number>()
  const attachmentList = ref<Array<SysAttachment>>([])
  const tableLoad = ref<boolean>(false)

  const handleQueryPage = async () => {
    attachmentQuery.value.pageNum = 1
    await initPage()
  }

  const initPage = async () => {
    tableLoad.value = true
    try {
      const resp = await queryPage(attachmentQuery.value)
      if (resp.code === 200) {
        attachmentTotal.value = resp.data.total
        attachmentList.value = resp.data.records
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
  // 转换文件大小
  const convertFileSize = (size?: string) => {
    if (!size) {
      return;
    }
    let numSize = Number(size)
    const units = ['B', 'KB', 'MB', 'GB', 'TB'];

    let unitIndex = 0;
    while (numSize >= 1024 && unitIndex < units.length - 1) {
      numSize /= 1024;
      unitIndex++;
    }

    return Math.round(numSize * 100) / 100 + " " + units[unitIndex];
  }

  handleQueryPage()
  return {
    selectedIds,
    attachmentRowSelectionType,
    attachmentColumn,
    attachmentTotal,
    attachmentList,
    tableLoad,
    attachmentQuery,
    convertFileSize,
    handleRowClick,
    handleQueryPage,
    initPage,
    reloadPage
  }
}

const {selectedIds, attachmentRowSelectionType ,attachmentColumn, attachmentTotal, attachmentList, tableLoad, attachmentQuery, convertFileSize, handleRowClick, handleQueryPage, reloadPage, initPage} = initSearch()

const initDelete = () => {
  // 显示删除提示
  const openDeletePopconfirm = ref<boolean>(false);

  // 倒计时
  const countdown = ref<number>()
  // 计时器
  const interval = ref()

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
  const handleDelete = async (id?:string) => {
    const deleteIds = id ? [id] : [...selectedIds.value];
    try {
      if (deleteIds.length > 0) {
        const resp = await deleteData(deleteIds)
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

  // 强制删除计时器
  const startForceDeleteCountdown = (open: boolean, second: number) => {
    clearInterval(interval.value)
    if (open) {
      countdown.value = second
    }

    interval.value = setInterval(() => {
      if (second === 0) {
        clearInterval(interval.value)
        countdown.value = 0
      } else {
        second--
        countdown.value = second
      }
    }, 1000)
  }

  // 处理强制删除
  const handleForceDelete = async (id: string) => {
    const resp = await forceDeleteData(id)
    if (resp.code === 200) {
      message.success(resp.msg);
      await initPage()
    } else {
      message.error(resp.msg)
    }
  }
  return {
    openDeletePopconfirm,
    countdown,
    closePopconfirm,
    handleDelete,
    openPopconfirm,
    handleForceDelete,
    startForceDeleteCountdown
  }
}

const {openDeletePopconfirm, countdown, closePopconfirm, handleDelete, openPopconfirm, handleForceDelete, startForceDeleteCountdown} = initDelete()

const initShare = () => {
  const showShareModal = ref<boolean>(false)
  const shareId = ref<string>()
  const shareName = ref<string>()
  const shareValidTime = ref<number>(15)
  const shareUrl = ref<string>()

  // 处理打开分享model
  const handleShowShareModal = (event: MouseEvent, id: string, fileName: string, status: string) => {
    event.stopPropagation()
    if (status !== '0') {
      message.error("仅上传完成附件可分享")
      return;
    }

    showShareModal.value = true
    shareId.value = id
    shareName.value = fileName
    handleGetShareUrl()
  }
  // 处理关闭分享model
  const handleCloseShareModal = () => {
    showShareModal.value = false
    shareId.value = undefined
    shareUrl.value = undefined
  }

  // 处理获取分享链接
  const handleGetShareUrl = async () => {
    if (shareId.value) {
      const resp = await getDownloadURL(shareId.value, shareValidTime.value.toString())
      if (resp.code === 200) {
        const timeoutTime = dayjs(new Date()).add(shareValidTime.value, "minute").format('YYYY-MM-DD HH:mm:ss')
        shareUrl.value = resp.data.startsWith("/") ? window.location.origin + baseAPI + resp.data : resp.data + `### 附件名称 ${shareName.value} 有效期至 ${timeoutTime} --来自狸花猫后台管理系统`
      } else {
        message.error(resp.msg)
      }
    }
  }

  // 处理复制到剪贴板
  const handleCopyToClipboard = () => {
    if (shareUrl.value) {
      navigator.clipboard.writeText(shareUrl.value).then(() => message.success('复制成功')).catch(() => message.success('复制失败'));
      handleCloseShareModal()
    } else {
      message.error("分享链接获取失败")
    }
  }
  return {
    showShareModal,
    shareId,
    shareName,
    shareValidTime,
    shareUrl,
    handleShowShareModal,
    handleCloseShareModal,
    handleGetShareUrl,
    handleCopyToClipboard
  }
}

const {showShareModal, shareName, shareUrl, shareValidTime, handleShowShareModal, handleCloseShareModal,handleGetShareUrl, handleCopyToClipboard} = initShare()

const intiInfo = () => {
  const showInfoModal = ref<boolean>(false)
  const attachmentInfo = ref<SysAttachmentVO>({})
  const previewUrlMap = ref<Map<string, string>>(new Map<string, string>())
  const visible = ref<boolean>(false)

  // 处理打开模态框
  const handleOpenInfoModal = async (event: MouseEvent, id: string) => {
    event.stopPropagation()
    try {
      const resp = await queryById(id)
      if (resp.code === 200) {
        showInfoModal.value = true
        attachmentInfo.value = resp.data
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

  // 处理关闭模态框
  const handleCloseInfoModal = () => {
    showInfoModal.value = false
    attachmentInfo.value = {}
  }

  // 处理详情中的图片预览
  const handlePreview = async (id?: string, type?: string) => {
    if (!id || !type) {
      message.error("附件id或类型不存在")
      return
    }
    const url = previewUrlMap.value.get(id)
    if (!url) {
      const resp = await getDownloadURL(id, '10080')
      if (resp.code === 200) {
        previewUrlMap.value.set(id, resp.data.startsWith("/") ? baseAPI + resp.data : resp.data)
      } else {
        message.error(resp.msg)
        return
      }
    }
    visible.value = !visible.value;
  }
  return {
    showInfoModal,
    attachmentInfo,
    previewUrlMap,
    visible,
    handleOpenInfoModal,
    handleCloseInfoModal,
    handlePreview
  }
}

const {showInfoModal, attachmentInfo, previewUrlMap, visible, handleOpenInfoModal, handleCloseInfoModal, handlePreview} = intiInfo()

// 下载
const handleDownload = async (event: MouseEvent, id: string, status: string) => {
  event.stopPropagation()
  if (status !== '0') {
    message.error("仅上传完成附件可下载")
    return;
  }
  try {
    const resp = await getDownloadURL(id)
    if (resp.code === 200) {
      download(resp.data.startsWith("/") ? baseAPI + resp.data : resp.data)
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
