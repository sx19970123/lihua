<template>
 <div>
   <a-flex :gap="16" vertical v-hasRole="['ROLE_admin']">
     <!--检索条件-->
     <a-card :style="{border: 'none'}">
       <a-form :colon="false" :model="deptQuery">
         <a-space size="small">
           <a-form-item class="form-item-single-line" label="部门名称" name="name">
             <a-input placeholder="请输入部门名称" v-model:value="deptQuery.name" allow-clear/>
           </a-form-item>
           <a-form-item class="form-item-single-line" label="部门编码" name="code">
             <a-input placeholder="请输入部门编码" v-model:value="deptQuery.code" allow-clear/>
           </a-form-item>
           <a-form-item class="form-item-single-line" label="部门状态" name="status">
             <a-select style="width: 120px;" placeholder="请选择" v-model:value="deptQuery.status" allow-clear>
               <a-select-option v-for="item in sys_status" :value="item.value">{{item.label}}</a-select-option>
             </a-select>
           </a-form-item>
           <a-form-item class="form-item-single-line">
             <a-button type="primary" @click="initList" :loading="tableLoad">
               <template #icon>
                 <SearchOutlined />
               </template>
               查 询
             </a-button>
           </a-form-item>
           <a-form-item class="form-item-single-line">
             <a-button @click="resetList" :loading="tableLoad">
               <template #icon>
                 <RedoOutlined />
               </template>
               重 置
             </a-button>
           </a-form-item>
         </a-space>
       </a-form>
     </a-card>
     <a-table :pagination="false"
              :columns="deptColumn"
              :data-source="deptList"
              row-key="id"
              v-model:expanded-row-keys="expandedRowKeys"
              :loading="tableLoad"
     >
       <template #title>
         <a-flex :gap="8">
           <a-button type="primary" @click="addDept">
             <template #icon>
               <PlusOutlined />
             </template>
             新 增
           </a-button>
           <a-button type="primary" @click="handleExpanded">
             <template #icon>
               <Unfold v-if="expandedRowKeys.length === 0"/>
               <PickUp v-else/>
             </template>
             {{!expandedRowKeys.length ? '展 开' : '折 叠'}}
           </a-button>
         </a-flex>
       </template>
       <template #bodyCell="{column,record,text}">
         <template v-if="column.key === 'status'">
           <dict-tag :dict-data-value="text" :dict-data-option="sys_status"/>
         </template>
         <template v-if="column.key === 'post'">
           <a-typography-link @click="handleSkipRoute(record.id)">
             {{ record.sysPostList?.map((sysPost:SysPost) => sysPost.name).join("、") }}
           </a-typography-link>
         </template>
         <template v-if="column.key === 'action'">
           <a-button type="link" size="small" @click="selectById(record.id)">
             <template #icon>
               <EditOutlined />
             </template>
             编辑
           </a-button>
           <a-divider type="vertical"/>
           <a-button type="link"
                     size="small"
                     @click="addChildren(record)"
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
             <a-button type="link" danger size="small">
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
import {deleteByIds, getDeptOption, findById, findList, save} from "@/api/system/dept/dept.ts";
import {reactive, ref} from "vue";
import {message} from "ant-design-vue";
import {initDict} from "@/utils/dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
import {cloneDeep} from "lodash-es";
import type {Rule} from "ant-design-vue/es/form";
import {useRouter} from "vue-router";
import {flattenTreeData} from "@/utils/tree.ts";
const {sys_status} = initDict("sys_status")
const router = useRouter()
const initSearch = () => {
  // 列表信息
  const deptColumn: ColumnsType = [
    {
      title: '部门名称',
      key: 'name',
      dataIndex: 'name',
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
      width: '300px'
    }
  ]

  // 查询条件
  const deptQuery = ref<SysDept>({})
  // 列表数据
  const deptList = ref<Array<SysDept>>([])
  // 默认展开行
  const expandedRowKeys = ref<Array<string>>([])
  // 列表加载
  const tableLoad = ref<boolean>(false)

  // 查询列表
  const initList = async () => {
    tableLoad.value = true
    const resp = await findList(deptQuery.value);
    if (resp.code === 200) {
      deptList.value = resp.data
      tableLoad.value = false
    } else {
      message.error(resp.msg);
    }
  }

  // 重置列表
  const resetList = async () => {
    deptQuery.value = {}
    await initList()
  }

  // 展开折叠
  const handleExpanded = () => {
    if (expandedRowKeys.value.length == 0) {
      const data:Array<SysMenu> = ([])
      flattenTreeData(deptList.value,data)
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
    initList,
    resetList,
    handleExpanded
  }
}
const {deptColumn,deptQuery,deptList,expandedRowKeys,tableLoad,initList,resetList,handleExpanded} = initSearch()

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
  const formRef = ref()

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
  const addChildren = (dept: SysDept) => {
    handleModelStatus("新增部门")
    sysDept.value.parentId = dept.id
    if (dept.children) {
      sysDept.value.sort = getSort(dept?.children)
    } else {
      sysDept.value.sort = 1
    }
  }

  // 获取排序
  const getSort = (deptList: Array<SysDept>): number => {
    const sorts = deptList.map(item => item.sort)
    if (sorts && sorts.length > 0) {
      const max = Math.max(...sorts as number[])
      return max + 1
    } else {
      return 1
    }
  }

  // 根据id查询数据
  const selectById = async (id: string) => {
    const resp = await findById(id)
    if (resp.code === 200) {
      handleModelStatus("修改部门")
      sysDept.value = resp.data
    } else {
      message.error(resp.msg)
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
      console.error("获取单位树失败",resp.msg)
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
    await formRef.value.validate()
    modalActive.saveLoading = true
    const resp = await save(sysDept.value)
    if (resp.code === 200) {
      await initList()
      await initTreeData()

      handleModelStatus()
      message.success(resp.msg)
    } else {
      message.error(resp.msg)
    }
    modalActive.saveLoading = false
  }
  initTreeData()
  return {
    modalActive,
    sysDept,
    parentDeptList,
    deptRoles,
    formRef,
    selectById,
    addChildren,
    initTreeData,
    handleDeptTree,
    addDept,
    saveDept,
    handleModelStatus
  }
}
const {modalActive,sysDept,parentDeptList,deptRoles,formRef,selectById,addChildren,initTreeData,addDept,saveDept,handleModelStatus} = initSave()

// 删除数据
const handleDelete = async (id: string) => {
  const resp = await deleteByIds([id]);
  if (resp.code === 200) {
    await initList()
    await initTreeData()
    message.success(resp.msg)
  } else {
    message.error(resp.msg)
  }
}

// 跳转至岗位页面
const handleSkipRoute = (id: string) => {
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
