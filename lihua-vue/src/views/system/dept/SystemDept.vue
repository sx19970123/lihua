<template>
 <div>
   <a-flex :gap="16" vertical>
     <!--检索条件-->
     <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
       <a-form :colon="false">
         <a-row :gutter="16">
           <a-col>
             <a-form-item label="部门名称" name="name">
               <a-input placeholder="请输入部门名称" v-model:value="deptQuery.name" allow-clear/>
             </a-form-item>
           </a-col>
           <a-col>
             <a-form-item label="部门编码" name="code">
               <a-input placeholder="请输入部门编码" v-model:value="deptQuery.code" allow-clear/>
             </a-form-item>
           </a-col>
           <a-col>
             <a-form-item label="部门状态" name="status">
               <a-select style="width: 120px;" placeholder="请选择" v-model:value="deptQuery.status" allow-clear>
                 <a-select-option v-for="item in sys_status" :value="item.value">{{item.label}}</a-select-option>
               </a-select>
             </a-form-item>
           </a-col>
           <a-col>
             <a-form-item>
               <a-space size="small">
                 <a-button type="primary" @click="initList" :loading="tableLoad">
                   <template #icon>
                     <SearchOutlined />
                   </template>
                   查 询
                 </a-button>
                 <a-button @click="resetList" :loading="tableLoad">
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
     <a-table :pagination="false"
              :columns="deptColumn"
              :data-source="deptList"
              row-key="id"
              v-model:expanded-row-keys="expandedRowKeys"
              :loading="tableLoad"
              :row-selection="deptRowSelectionType"
              row-class-name="hover-cursor-pointer"
              :custom-row="handleRowClick"
              :scroll="{x: 1500}"
     >
       <template #title>
         <a-flex :gap="8" wrap="wrap">
           <a-button type="primary" @click="addDept">
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

           <a-button type="primary" ghost @click="handleExpanded">
             <template #icon>
               <Unfold v-if="expandedRowKeys.length === 0"/>
               <PickUp v-else/>
             </template>
             {{!expandedRowKeys.length ? '展 开' : '折 叠'}}
           </a-button>
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
                     <a-typography-text>4. 部门名称路径列描述了部门间的层级关系，以“/”分隔，最后一级为当前部门，顶级部门与部门名称相同</a-typography-text>
                     <a-typography-text>5. 无法导入的数据会被收集并导出，同时标记错误信息，修改后可重新导入</a-typography-text>
                     <a-typography-text>6. Excel表内的岗位信息不会被保存，如需导入，请前往“岗位管理”模块进行操作</a-typography-text>
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
           <table-setting v-model="deptColumn"/>
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
         <template v-if="column.key === 'post'">
           <a-tooltip>
             <template #title>
               {{ record.sysPostList?.map((sysPost:SysPost) => sysPost.name).join("、") }}
             </template>
             <a-typography-link @click="(event: MouseEvent) => handleSkipRoute(event, record.id)">
               {{ record.sysPostList?.map((sysPost:SysPost) => sysPost.name).join("、") }}
             </a-typography-link>
           </a-tooltip>
         </template>
         <template v-if="column.key === 'action'">
           <a-button type="link" size="small" @click="(event: MouseEvent) => selectById(event,record.id)">
             <template #icon>
               <EditOutlined />
             </template>
             编辑
           </a-button>
           <a-divider type="vertical"/>
           <a-button type="link"
                     size="small"
                     @click="(event: MouseEvent) => addChildren(event, record)"
           >
             <template #icon>
               <PlusOutlined />
             </template>
             新增下级
           </a-button>
           <a-divider type="vertical"/>
           <a-popconfirm ok-text="确 定"
                         cancel-text="取 消"
                         placement="bottomRight"
                         @confirm="handleDelete(record.id)"
           >
             <template #title>
               数据删除后不可恢复，是否删除？
             </template>
             <a-button type="link" danger size="small" @click="(event: MouseEvent) => event.stopPropagation()">
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
    <!--模态框-->
   <a-modal v-model:open="modalActive.open" @ok="saveDept" :confirm-loading="modalActive.saveLoading">
     <template #title>
       <div style="margin-bottom: 24px">
         <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
       </div>
     </template>
      <a-form :colon="false" :model="sysDept" :label-col="{ span: 4 }" :rules="deptRoles"  ref="formRef">
        <a-form-item label="上级部门" :wrapper-col="{span: 16}">
          <a-tree-select :tree-data="parentDeptList"
                         :fieldNames="{children:'children', label:'name',value: 'id'}"
                         v-model:value="sysDept.parentId"
          />
        </a-form-item>
        <a-form-item label="部门名称" :wrapper-col="{span: 16}" name="name">
          <a-input v-model:value="sysDept.name" placeholder="请输入部门名称" :maxlength="60" show-count allow-clear/>
        </a-form-item>
        <a-form-item label="部门编码" :wrapper-col="{span: 16}" name="code">
          <a-input v-model:value="sysDept.code" placeholder="请输入部门编码" :maxlength="100" show-count allow-clear/>
        </a-form-item>
        <a-form-item label="排序" name="sort">
          <a-input-number v-model:value="sysDept.sort" placeholder="升序排列" allow-clear/>
        </a-form-item>
        <a-form-item label="状态">
          <a-radio-group v-model:value="sysDept.status">
            <a-radio :value="item.value" v-for="item in sys_status">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-row>
          <a-col :span="12">
            <a-form-item label="负责人" :label-col="{span: 8}">
              <a-input v-model:value="sysDept.manager" placeholder="请输入负责人" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系电话" :label-col="{span: 8}" name="phoneNumber">
              <a-input v-model:value="sysDept.phoneNumber" placeholder="请输入联系电话" allow-clear/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="传真" :label-col="{span: 8}" name="fax">
              <a-input v-model:value="sysDept.fax" placeholder="请输入传真号码" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="电子邮箱" :label-col="{span: 8}" name="email">
              <a-input v-model:value="sysDept.email" placeholder="请输入电子邮箱" allow-clear/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="备注">
          <a-textarea type="textarea" v-model:value="sysDept.remark" placeholder="请输入备注" :maxlength="300" show-count allow-clear/>
        </a-form-item>
      </a-form>
   </a-modal>
 </div>
</template>

<script setup lang="ts">
// 查询列表
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {
  deleteData,
  getDeptOption,
  queryById,
  queryList,
  save,
  updateStatus,
  exportExcel, importExcel
} from "@/api/system/dept/Dept.ts";
import {createVNode, reactive, ref, useTemplateRef} from "vue";
import {type FormInstance, message, Modal} from "ant-design-vue";
import {initDict} from "@/utils/Dict.ts";
import {cloneDeep} from "lodash-es";
import type {Rule} from "ant-design-vue/es/form";
import {useRouter} from "vue-router";
import {flattenTree} from "@/utils/Tree.ts";
import type {SysDept, SysDeptVO} from "@/api/system/dept/type/SysDept.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";
import type {UploadRequestOption} from "ant-design-vue/lib/vc-upload/interface";
import Spin from "@/components/spin";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import PickUp from "@/components/icon/pick-up/PickUp.vue";
import Unfold from "@/components/icon/unfold/Unfold.vue";
import {ResponseError} from "@/api/global/Type.ts";
import {download} from "@/utils/AttachmentDownload.ts";
import settings from "@/settings.ts";
import TableSetting from "@/components/table-setting/index.vue";
const {sys_status} = initDict("sys_status")
const router = useRouter()
// 显示更多按钮
const showMore = ref<boolean>(false)

const initSearch = () => {
  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const deptRowSelectionType = {
    checkStrictly: true,
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
  const handleRowClick = (record:SysDeptVO) => {
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
  // 列表信息
  const deptColumn = ref<ColumnsType>([
    {
      title: '部门名称',
      key: 'name',
      dataIndex: 'name',
      ellipsis: true
    },
    {
      title: '部门编码',
      key: 'code',
      dataIndex: 'code',
    },
    {
      title: '排序',
      key: 'sort',
      dataIndex: 'sort',
      align: 'right',
      width: '100px'
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
      title: '岗位',
      key: 'post',
      dataIndex: 'post',
      ellipsis: true
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '292px',
      fixed: document.body.offsetWidth > settings.menuToggleWidth ? 'right' : false
    }
  ])

  // 查询条件
  const deptQuery = ref<SysDept>({})
  // 列表数据
  const deptList = ref<Array<SysDeptVO>>([])
  // 默认展开行
  const expandedRowKeys = ref<Array<string>>([])
  // 列表加载
  const tableLoad = ref<boolean>(false)

  // 查询列表
  const initList = async () => {
    tableLoad.value = true
    try {
      const resp = await queryList(deptQuery.value);
      if (resp.code === 200) {
        deptList.value = resp.data
        handleDeptStatus(deptList.value)
      } else {
        message.error(resp.msg);
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

  // 处理回显菜单状态
  const handleDeptStatus = (deptList: Array<SysDeptVO>) => {
    deptList.forEach(dept => {
      dept.statusIsNormal = dept.status === '0'
      dept.updateStatusLoading = false

      if (dept.children && dept.children.length > 0) {
        handleDeptStatus(dept.children)
      }
    })
  }

  // 重置列表
  const resetList = async () => {
    deptQuery.value = {}
    await initList()
  }

  // 展开折叠
  const handleExpanded = () => {
    if (expandedRowKeys.value.length == 0) {
      const data = flattenTree(deptList.value)
      expandedRowKeys.value = data.filter(item => item.id).map(item => item.id as string)
    } else {
      expandedRowKeys.value = []
    }
  }

  initList()

  return {
    deptColumn,
    deptQuery,
    deptList,
    expandedRowKeys,
    tableLoad,
    selectedIds,
    deptRowSelectionType,
    handleRowClick,
    initList,
    resetList,
    handleExpanded
  }
}
const {deptColumn,deptQuery,deptList,expandedRowKeys,tableLoad,selectedIds,deptRowSelectionType,handleRowClick,initList,resetList,handleExpanded} = initSearch()

// 保存数据
const initSave = () => {

  const deptRoles: Record<string, Rule[]> = {
    name: [
      {required: true, message: "请输入部门名称", trigger: "change"}
    ],
    code: [
      {required: true, message: "请输入部门编码", trigger: "change"}
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
  // 表单
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


  // 部门数据
  const sysDept = ref<SysDept>({})
  // 上级部门
  const parentDeptList = ref<Array<SysDept>>([])

  // 新增部门
  const addDept = () => {
    handleModelStatus("新增部门")
    if (deptList) {
      sysDept.value.sort = getSort(deptList.value)
    } else {
      sysDept.value.sort = 1
    }
  }
  // 新增下级
  const addChildren = (event: MouseEvent, dept: SysDept) => {
    event.stopPropagation()
    handleModelStatus("新增部门")
    sysDept.value.parentId = dept.id
    if (dept.children) {
      sysDept.value.sort = getSort(dept?.children)
    } else {
      sysDept.value.sort = 1
    }
  }

  // 获取排序
  const getSort = (deptList?: Array<SysDept>): number => {
    const sorts = deptList?.map(item => item.sort)
    if (sorts && sorts.length > 0) {
      const max = Math.max(...sorts as number[])
      return max + 1
    } else {
      return 1
    }
  }

  // 根据id查询数据
  const selectById = async (event: MouseEvent,id: string) => {
    event.stopPropagation()
    try {
      const resp = await queryById(id)
      if (resp.code === 200) {
        handleModelStatus("修改部门")
        sysDept.value = resp.data
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

  // 修改模态框状态，打开关闭模态框
  const handleModelStatus = (title?: string) => {
    modalActive.open = !modalActive.open
    if (title) {
      modalActive.title = title
    }

    if (modalActive.open) {
      resetForm()
    }
  }

  const resetForm = () => {
    sysDept.value = {
      status: '0',
      parentId: '0'
    }
  }

  // 初始化树型结构
  const initTreeData = async () => {
    try {
      const resp = await getDeptOption()
      if (resp.code === 200) {
        const deepDeptList = cloneDeep(resp.data)
        handleDeptTree(deepDeptList)
        parentDeptList.value = [{
          id: '0',
          name: '根节点',
          children: deepDeptList
        }]
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

  // 处理树
  const handleDeptTree = (deptList: Array<SysDept>) => {
    deptList.forEach(item => {
      if (item.children && item.children.length > 0) {
        handleDeptTree(item.children)
      }
    })
  }

  const saveDept = async () => {
    await formRef.value?.validate()
    modalActive.saveLoading = true
    try {
      const resp = await save(sysDept.value)
      if (resp.code === 200) {
        await initList()
        await initTreeData()

        handleModelStatus()
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
    } finally {
      modalActive.saveLoading = false
    }
  }

  // 修改菜单状态
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
      handleDeptStatus(deptList.value, id, newStatus)
    }

  }

  // 回显菜单状态
  const handleDeptStatus = (deptList: Array<SysDeptVO>, id: string, status: string): boolean => {
    for (let dept of deptList) {
      if (dept.id === id) {
        dept.status = status;
        dept.statusIsNormal = dept.status === '0';
        dept.updateStatusLoading = false;
        return true; // 终止循环并返回 true
      }

      if (dept.children && dept.children.length > 0) {
        const found = handleDeptStatus(dept.children, id, status);
        if (found) return true; // 终止外部循环
      }
    }

    return false;
  };

  initTreeData()
  return {
    modalActive,
    sysDept,
    parentDeptList,
    deptRoles,
    formRef,
    handleUpdateStatus,
    selectById,
    addChildren,
    initTreeData,
    handleDeptTree,
    addDept,
    saveDept
  }
}
const {modalActive,sysDept,parentDeptList,deptRoles,formRef,handleUpdateStatus,selectById,addChildren,initTreeData,addDept,saveDept} = initSave()

// 删除部门
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
          await initList()
          await initTreeData()
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
        console.log(e)
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
    const resp = await exportExcel(deptQuery.value)
    if (resp.code === 200) {
      download(resp.data)
    } else {
      message.error(resp.msg)
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
      tip: "数据处理中，请稍等..."
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
        await initList()
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

// 跳转至岗位页面
const handleSkipRoute = (event: MouseEvent, id: string) => {
  event.stopPropagation()
  router.push({
    path: "/system/post",
    query: {
      deptId: id
    }
  })
}
</script>

<style scoped>

</style>
