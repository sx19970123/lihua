<template>
 <div>
   <a-flex :gap="16" vertical v-hasRole="['ROLE_admin']">
     <!--检索条件-->
     <a-card :style="{border: 'none'}">
       <a-form :colon="false">

       </a-form>
     </a-card>
     <a-table :pagination="false" :columns="deptColumn" :data-source="deptList">
       <template #title>
         <a-flex :gap="8">
           <a-button type="primary" @click="handleModelStatus('新增部门')">
             <template #icon>
               <PlusOutlined />
             </template>
             新 增
           </a-button>
           <a-button type="primary">
             <template #icon>
               <Unfold v-if="true"/>
               <PickUp v-else/>
             </template>
             {{!true ? '展 开' : '折 叠'}}
           </a-button>
         </a-flex>
       </template>
       <template #bodyCell="{column,record,text}">
         <template v-if="column.key === 'type'">
           <dict-tag :dict-data-value="text" :dict-data-option="sys_dept_type"/>
         </template>
         <template v-if="column.key === 'action'">
           <a-button type="link" size="small">
             <template #icon>
               <EditOutlined />
             </template>
             编辑
           </a-button>
           <a-divider type="vertical"/>
           <a-button type="link"
                     size="small"
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
   <a-modal v-model:open="modalActive.open">
     <template #title>
       <div style="margin-bottom: 24px">
         <a-typography-title :level="4">{{modalActive.title}}</a-typography-title>
       </div>
     </template>
      <a-form :colon="false" :model="sysDept" :label-col="{ span: 4 }">
        <a-form-item label="部门类型">
          <a-radio-group v-model:value="sysDept.type">
            <a-radio-button v-for="item in sys_dept_type" :value="item.value">{{item.label}}</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="上级部门" :wrapper-col="{span: 16}">
          <a-tree-select/>
        </a-form-item>
        <a-form-item label="部门名称" :wrapper-col="{span: 16}">
          <a-input v-model:value="sysDept.name"/>
        </a-form-item>
        <a-form-item label="部门编码" :wrapper-col="{span: 16}">
          <a-input v-model:value="sysDept.code"/>
        </a-form-item>
        <a-form-item label="排序">
          <a-input-number v-model:value="sysDept.sort"/>
        </a-form-item>
        <a-form-item label="状态">
          <a-radio-group v-model:value="sysDept.status">
            <a-radio :value="item.value" v-for="item in sys_status">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-row>
          <a-col :span="12">
            <a-form-item label="负责人" :label-col="{span: 8}">
              <a-input-search v-model:value="sysDept.manager">
                <template #enterButton>
                  <a-button>
                    <template #icon><TeamOutlined /></template>
                  </a-button>
                </template>
              </a-input-search>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系电话" :label-col="{span: 8}">
              <a-input v-model:value="sysDept.phoneNumber"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="传真" :label-col="{span: 8}">
              <a-input v-model:value="sysDept.fax"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="电子邮箱" :label-col="{span: 8}">
              <a-input v-model:value="sysDept.email"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="备注">
          <a-textarea type="textarea" v-model="sysDept.remark"/>
        </a-form-item>
      </a-form>
   </a-modal>
 </div>
</template>

<script setup lang="ts">
// 查询列表
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import {findList} from "@/api/system/dept/dept.ts";
import {reactive, ref} from "vue";
import {message} from "ant-design-vue";
import {initDict} from "@/utils/dict.ts";
import DictTag from "@/components/dict-tag/index.vue"
const {sys_dept_type, sys_status} = initDict("sys_dept_type","sys_status")
const initSearch = () => {
  // 列表信息
  const deptColumn: ColumnsType = [
    {
      title: '名称',
      key: 'name',
      dataIndex: 'name',
    },
    {
      title: '编码',
      key: 'code',
      dataIndex: 'code',
    },
    {
      title: '排序',
      key: 'sort',
      dataIndex: 'sort',
      align: 'right'
    },
    {
      title: '类型',
      key: 'type',
      dataIndex: 'type',
      align: 'center'
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center'
    },
    {
      title: '负责人',
      key: 'manager',
      dataIndex: 'manager',
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      width: '300px'
    }
  ]

  const deptQuery = ref<SysDept>({})
  const deptList = ref<Array<SysDept>>([])

  const initList = async () => {
    const resp = await findList(deptQuery.value);
    if (resp.code === 200) {
      deptList.value = resp.data
    } else {
      message.error(resp.msg);
    }
  }
  initList()

  return {
    deptColumn,
    deptList
  }
}
const {deptColumn,deptList} = initSearch()

// 保存数据
const initSave = () => {

  // 模态框状态
  type modalActiveType = {
    open: boolean, // 模态框开关
    saveLoading: boolean, // 点击保存按钮加载
    title: string // 模态框标题
  }

  const modalActive = reactive<modalActiveType>({
    open: false,
    saveLoading: false,
    title: "保存部门"
  })

  // 部门数据
  const sysDept = ref<SysDept>({})

  // 修改模态框状态，打开关闭模态框
  const handleModelStatus = (title: string) => {
    modalActive.open = !modalActive.open
    if (title) {
      modalActive.title = title
    }

    if (modalActive.open) {
      // resetForm()
    }
  }



  return {
    modalActive,
    sysDept,
    handleModelStatus
  }
}
const {modalActive,sysDept,handleModelStatus} = initSave()

// 删除数据
</script>

<style scoped>

</style>
