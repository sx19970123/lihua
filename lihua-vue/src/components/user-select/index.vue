<template>
  <div :style="{width: props.width + 'px'}">
    <a-card>
      <a-flex>
<!--        部门检索组件-->
        <div class="full-width">
          <a-spin :spinning="loadingTree">
            <a-input placeholder="检索部门树"
                     v-model:value="deptKeyword"
                     allowClear
                     @change="handleChangeKeyword"
                     :bordered="false"
                     class="dept-keyword-input">
              <template #suffix>
                <SearchOutlined :style="{color: themeStore.isDarkTheme ? 'rgba(255, 255, 255, 0.45)' : 'rgba(0, 0, 0, 0.45)'}"/>
              </template>
            </a-input>
            <a-divider class="divider"/>
            <div class="scrollbar" :style="{height: props.height + 'px'}">
              <a-tree
                  :tree-data="sysDeptList"
                  :field-names="{children:'children', title:'name', key: 'id' }"
                  default-expand-all
                  v-model:checked-keys="deptIdList"
                  v-model:expanded-keys="expandKeys"
                  @select="handleClickTree"
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
          </a-spin>
        </div>
<!--        用户勾选组件-->
          <a-table :columns="userColumn"
                   :loading="loadingUser"
                   row-key="id"
                   :row-selection="userRowSelectionType"
                   :custom-row="handleRowClick"
                   size="small"
                   row-class-name="hover-cursor-pointer"
                   class="full-width scrollbar"
                   :pagination="false"
                   :scroll="{y: props.height}"
                   :data-source="userList"
          />
<!--        已选用户组件-->
        <div class="full-width">
          <div class="user-show-title">
            <a-flex justify="space-between">
              <div>
                 {{selectedIds.length}} 人
              </div>
              <a-button size="small"
                        danger
                        type="text"
                        :disabled="selectedIds.length === 0"
                        @click="() => selectedIds.length = 0"
              >
                <template #icon>
                  <DeleteOutlined />
                </template>
                 全部清空
              </a-button>
            </a-flex>
          </div>
          <a-divider class="divider"/>
          <div :style="{height: props.height + 'px'}" class="scrollbar user-show-group">
            <a-flex wrap="wrap" gap="small">
              <user-show class="user-show"
                         @click="handleCancelSelect(user)"
                         v-for="user in selectUsers"
                         :avatar-json="user.avatar"
                         :nickname="user.nickname"/>
            </a-flex>
          </div>
        </div>
      </a-flex>
    </a-card>
  </div>
</template>
<script lang="ts" setup>
import {getDeptOption} from "@/api/system/dept/Dept.ts";
import {flattenTreeData} from "@/utils/Tree.ts";
import {ref, watch} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import {cloneDeep} from "lodash-es";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import type {SysUser} from "@/api/system/user/type/SysUser.ts";
import UserShow from "@/components/user-show/index.vue"
import {getUserOption} from "@/api/system/user/User.ts";
import {message} from "ant-design-vue";
const themeStore = useThemeStore();

const props = defineProps({
  height: {
    type: Number,
    default: 150
  },
  width: {
    type: Number,
    default: 750
  },
  user: {
    type: Array<SysUser>
  },
  userId: {
    type: Array<String>
  },
  avatar:{
    type: Array<String>
  },
  nickname:{
    type: Array<String>
  }
})

const emits = defineEmits(['update:user','update:userId','update:avatar','update:nickname'])

// 部门相关
const initDeptTree = () => {
  // 选中的deptIds
  const deptIdList = ref<string[]>([])
  // 部门信息
  const sysDeptList = ref<Array<SysDept>>([])
  // 没有进行双向绑定的单位树
  let originDeptTree: Array<SysDept> = ([])
  // 扁平化部门树
  const flattenDeptList: Array<SysDept> = []
  // 所有树型节点id
  const deptIds: string[] = []
  // 部门树检索关键字
  const deptKeyword = ref<string>('')
  // 部门树配置信息
  const expandKeys = ref<string[]>([])
  // 树加载
  const loadingTree = ref<boolean>(false)
  // 初始化部门数据
  const initDept = async () => {
    loadingTree.value = true
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
    } else {
      message.error(resp.msg)
    }
    loadingTree.value = false
  }

  // 处理展开折叠
  const handleExpanded = () => {
    // 全部展开
    expandKeys.value = []
    expandKeys.value.push(... deptIds)
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
  // 关键词变化时进行过滤
  const handleChangeKeyword = () => {
    const value = deptKeyword.value
    if (value) {
      // 关键词输入时全部展开
      handleExpanded()
      // 对树型结构进行过滤
      sysDeptList.value = filterTreeByLabel(originDeptTree, value)
    } else {
      // value 为空时，还原树
      sysDeptList.value = cloneDeep(originDeptTree)
    }
  }

  initDept()
  return {
    deptIdList,
    deptKeyword,
    sysDeptList,
    expandKeys,
    loadingTree,
    handleChangeKeyword
  }
}
const  { deptIdList, deptKeyword, sysDeptList, expandKeys, loadingTree, handleChangeKeyword } = initDeptTree()

// 用户相关
const initUserTable = () => {
  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 选中的用户集合
  const selectUsers = ref<SysUser[]>([])
  // 用户加载
  const loadingUser = ref<boolean>(false)
  // 用户列表
  const userList = ref<SysUser[]>([])
  // 列表列
  const userColumn: ColumnsType = [
    {
      title: '用户名',
      key: 'username',
      dataIndex: 'username'
    },
    {
      title: '用户昵称',
      key: 'nickname',
      dataIndex: 'nickname'
    }
  ]
  // 列表勾选对象
  const userRowSelectionType = {
    type: 'checkbox',
    // 指定选中id的数据集合，操作完后可手动清空
    selectedRowKeys: selectedIds,
    onChange: (ids: Array<string>) => {
      selectedIds.value = ids
    },
  }
  // 点击数据行选中
  const handleRowClick = (record:SysUser) => {
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

  // 点击部门节点
  const handleClickTree = async (checkedKeys: string[]) => {
    if (checkedKeys.length > 0) {
      loadingUser.value = true
      const resp = await getUserOption(checkedKeys[0])
      if (resp.code === 200) {
        userList.value = resp.data
      } else {
        message.error(resp.msg)
      }
      loadingUser.value = false
    }
  }

  // 取消选中用户
  const handleCancelSelect = (user: SysUser) => {
    selectedIds.value = selectedIds.value.filter(id => id !== user.id)
  }

  return {
    userColumn,
    userRowSelectionType,
    userList,
    selectedIds,
    selectUsers,
    loadingUser,
    handleCancelSelect,
    handleClickTree,
    handleRowClick
  }
}
const { userColumn, userRowSelectionType, userList, selectedIds, selectUsers, loadingUser, handleCancelSelect, handleClickTree, handleRowClick } = initUserTable()

// 监听选中用户id获取selectUsers
watch(() => selectedIds.value, (value) => {
  selectUsers.value = userList.value.filter(user => value.includes(user.id))
  emits('update:nickname', selectUsers.value.map(user => user.nickname))
  emits('update:userId', selectUsers.value.map(user => user.id))
  emits('update:avatar', selectUsers.value.map(user => user.avatar))
  emits('update:user', selectUsers)
}, {deep: true})

</script>

<style scoped>
.user-show-title {
  margin-left: 16px;
  margin-top: 8px
}
.user-show-group {
  padding-left: 12px
}
.user-show {
  position: relative;
}
.user-show:hover:after {
  content: '\274C';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color:rgba(0, 0, 0, 0.06);
  z-index: 1;
  border-radius: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  backdrop-filter: saturate(180%) blur(20px);
}

.dept-keyword-input {
  height: 28px;
  margin-top: 4px;
  margin-right: 10px;
  width: calc(100% - 16px)
}

.divider {
  margin-bottom: 8px;
  margin-top: 6px
}

.full-width {
  width: 100%;
}
/* 表格滚动条缩短 */
:deep(.ant-table-body) {
  scrollbar-width: thin
}
</style>
<style>
[data-theme="dark"] {
  .user-show:hover::after {
    background-color: rgba(255, 255, 255, 0.06);
  }
}
</style>