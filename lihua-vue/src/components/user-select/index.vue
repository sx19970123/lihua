<template>
  <div style="width: 750px;">
    <a-card>
      <a-flex>
<!--        部门检索组件-->
        <div class="full-width">
          <a-input placeholder="检索部门树"
                   v-model:value="deptKeyword"
                   allowClear
                   @change="handleChangeKeyword"
                   class="dept-keyword-input"/>
          <a-divider class="divider"/>
          <a-tree
              :tree-data="sysDeptList"
              :field-names="{children:'children', title:'name', key: 'id' }"
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
<!--        用户勾选组件-->
          <a-table :columns="userColumn"
                   :row-selection="userRowSelectionType"
                   size="small"
                   class="full-width scrollbar"
                   :pagination="false"
                   :scroll="{y:200}"
                   :data-source="[{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},]"
          >
          </a-table>

<!--        已选用户组件-->
        <div class="full-width">
          <div class="user-show-title">
            <a-flex justify="space-between">
              <div>
                已选10人
              </div>
              <a-button size="small" danger type="text">
                <template #icon>
                  <DeleteOutlined />
                </template>
                清空选择
              </a-button>
            </a-flex>
          </div>
          <a-divider class="divider"/>
          <div style="height: 200px" class="scrollbar user-show-group">
            <a-flex wrap="wrap" gap="small">
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
              <user-show class="user-show" :avatar-json="json" nickname="yukino"/>
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
import {ref} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import {cloneDeep} from "lodash-es";
import type {ColumnsType} from "ant-design-vue/es/table/interface";
import type {SysUserVO} from "@/api/system/user/type/SysUser.ts";
import UserShow from "@/components/user-show/index.vue"
const themeStore = useThemeStore();
const json = "{\"value\":\"NintendoSwitch\",\"type\":\"icon\",\"backgroundColor\":\"rgb(245, 34, 45)\"}"
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
  // 初始化部门数据
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

  // 点击部门节点
  const handleClickTree = (checkedKeys: string[]) => {
    console.log(checkedKeys)
  }

  initDept()
  return {
    deptIdList,
    deptKeyword,
    sysDeptList,
    expandKeys,
    handleChangeKeyword,
    handleClickTree
  }
}
const  {
  deptIdList,
  deptKeyword,
  sysDeptList,
  expandKeys,
  handleChangeKeyword,
  handleClickTree
} = initDeptTree()

const initUserTable = () => {
  // 选中的数据id集合
  const selectedIds = ref<Array<string>>([])
  // 列表勾选对象
  const userRowSelectionType = {

    type: 'checkbox',
    // 支持跨页勾选
    preserveSelectedRowKeys: true,
    // 指定选中id的数据集合，操作完后可手动清空
    selectedRowKeys: selectedIds,
    onChange: (ids: Array<string>) => {
      selectedIds.value = ids
    },
  }
  // 点击数据行选中
  const handleRowClick = (record:SysUserVO) => {
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
    },
    {
      title: '用户昵称',
    }
  ]

  return {
    userColumn,
    userRowSelectionType,
    handleRowClick
  }
}

const { userColumn, userRowSelectionType, handleRowClick } = initUserTable()
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
</style>
<style>
[data-theme="dark"] {
  .user-show:hover::after {
    background-color: rgba(255, 255, 255, 0.06);
  }
}
</style>