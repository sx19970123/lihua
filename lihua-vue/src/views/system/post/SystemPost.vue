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
                  <a-button type="primary" @click="handleQueryPage" :loading="tableLoad">
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
      <a-table :columns="postColumn"
               :data-source="postList"
               :pagination="false"
               :loading="tableLoad"
               :row-selection="postRowSelectionType"
               row-class-name="hover-cursor-pointer"
               :custom-row="handleRowClick"
               :scroll="{x: 1200}"
               row-key="id"
      >
        <template #title>
          <a-flex :gap="8" wrap="wrap">
            <a-button type="primary" @click="handleModalStatus('新增岗位')">
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
                      <a-typography-text>2. 以名称和编码为准，保证全局唯一性。遇到重复数据将无法导入</a-typography-text>
                      <a-typography-text>3. 带有*标记的为必填字段</a-typography-text>
                      <a-typography-text>4. 无法导入的数据会被收集并导出，同时标记错误信息，修改后可重新导入</a-typography-text>
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
            <table-setting v-model="postColumn"/>
          </a-flex>
        </template>

        <template #bodyCell="{column,record,text}">
          <template v-if="column.key === 'code'">
            <a-typography-paragraph copyable>{{text}}</a-typography-paragraph>
          </template>
          <template v-if="column.key === 'status'">
            <a-switch v-model:checked="record.statusIsNormal"
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

          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" @click="(event:MouseEvent) => selectById(event, record.id)">
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
              <a-button type="link" danger size="small" @click="(event:MouseEvent) => event.stopPropagation()">
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
                          show-size-changer
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
import {createVNode, reactive, ref, useTemplateRef, watch} from "vue";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {initDict} from "@/utils/Dict.ts";
import {deleteData, exportExcel, queryById, queryPage, importExcel, save, updateStatus} from "@/api/system/post/Post.ts";
import {useRoute} from "vue-router";
import type {Rule} from "ant-design-vue/es/form";
import {flattenTree} from "@/utils/Tree.ts";
import {type FormInstance, message, Modal} from "ant-design-vue";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import type {SysPost, SysPostDTO, SysPostVO} from "@/api/system/post/type/SysPost.ts";
import type {UploadRequestOption} from "ant-design-vue/lib/vc-upload/interface";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import Spin from "@/components/spin";
import {ResponseError} from "@/api/global/Type.ts";
import {download} from "@/utils/AttachmentDownload.ts";
import settings from "@/settings.ts";
import TableSetting from "@/components/table-setting/index.vue";
const {sys_status} = initDict("sys_status")
const route = useRoute();

// 显示更多按钮
const showMore = ref<boolean>(false)

// 监听传入deptId变化进行部门赋值
watch(() => route.query.deptId, (value) => {
  postQuery.value.deptId = value as string | undefined;
  initPage()
})


// 初始化部门树
const initDept = () => {
  const deptTree = ref<Array<SysDept>>([])
  const initDeptTree = async () => {
    try {
      const resp = await getDeptOption()
      if (resp.code === 200 ) {
        deptTree.value = resp.data
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
  initDeptTree()

  return {
    deptTree
  }
}

const { deptTree } = initDept()

// 查询列表
const initSearch = () => {
  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const postRowSelectionType = {
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
  const handleRowClick = (record:SysPostVO) => {
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
  const postColumn = ref<ColumnsType>([
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
      width: '182px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    }
  ])

  const postQuery = ref<SysPostDTO>({
    deptId: route.query.deptId as string | undefined,
    pageNum: 1,
    pageSize: 10,
  })
  const postTotal = ref<number>()
  const postList = ref<Array<SysPostVO>>([])
  const tableLoad = ref<boolean>(false)

  const handleQueryPage = async () => {
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
    try {
      const resp = await queryPage(postQuery.value)
      if (resp.code === 200) {
        postList.value = resp.data.records
        postTotal.value = resp.data.total
        // 回显状态
        postList.value.forEach(post => {
          post.statusIsNormal = post.status === '0'
          post.updateStatusLoading = false
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
      tableLoad.value = false
    }
  }
  handleQueryPage()
  return {
    postColumn,
    postList,
    postQuery,
    selectedIds,
    postRowSelectionType,
    tableLoad,
    postTotal,
    handleRowClick,
    handleQueryPage,
    initPage,
    reloadPage
  }
}
const { postColumn,postList,postQuery,selectedIds,postRowSelectionType,handleRowClick,initPage,handleQueryPage,reloadPage,tableLoad,postTotal } = initSearch()


// 保存岗位
const initSave = () => {

  const formRef = useTemplateRef<FormInstance>("formRef")

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

  const selectById = async (event:MouseEvent, id: string) => {
    event.stopPropagation()
    try {
      const resp = await queryById(id)
      if (resp.code === 200) {
        handleModalStatus('修改岗位')
        sysPost.value = resp.data
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

  // 保存岗位数据
  const savePost = async () => {
    await formRef.value?.validate()

    // 设置岗位编码
    const flattenTreeList = flattenTree(deptTree.value)
    flattenTreeList.forEach(item => {
      if (item.id === sysPost.value.deptId) {
        sysPost.value.deptCode = item.code
      }
    })
    modalActive.saveLoading = true
    try {
      const resp = await save(sysPost.value)
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
    } finally {
      modalActive.saveLoading = false
    }
  }


  // 修改角色状态
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
      postList.value.some(post => {
        if (post.id === id) {
          post.status = newStatus
          post.statusIsNormal = post.status === '0'
          post.updateStatusLoading = false
          return
        }
      })
    }

  }


  return {
    modalActive,
    sysPost,
    postRoles,
    formRef,
    handleUpdateStatus,
    selectById,
    savePost,
    handleModalStatus,
  }
}
const { modalActive, sysPost, postRoles, formRef, handleUpdateStatus,  selectById ,savePost ,handleModalStatus } = initSave()

// 删除岗位
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

  return {
    openDeletePopconfirm,
    closePopconfirm,
    handleDelete,
    openPopconfirm
  }
}

const {openDeletePopconfirm,closePopconfirm,handleDelete,openPopconfirm} = initDelete()

// 初始化excel导入导出相关操作
const initExcel = () => {
  // 导出excel
  const handleExportExcel = async () => {
    const spinInstance = Spin.service({
      tip: '努力加载中...'
    });
    const resp = await exportExcel(postQuery.value)
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
</script>

<style scoped>

</style>
