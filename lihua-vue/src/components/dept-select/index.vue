<template>
  <div>
    <div class="dept-card scrollbar" :style="'max-height:' + props.maxHeight + 'px'">
      <a-input-search v-model:value="keyword" allowClear placeholder="请输入部门名称"/>
      <div style="margin: 8px 8px 8px 0">
        <a-checkable-tag v-model:checked="checkedAll" @click="handleCheckedAllKeys">全选/全不选</a-checkable-tag>
        <a-checkable-tag v-model:checked="expanded" @click="handleExpanded">展开/折叠</a-checkable-tag>
        <a-checkable-tag v-model:checked="checkStrictly">父子关联</a-checkable-tag>
      </div>
      <a-tree :tree-data="sysDeptList"
              :field-names="{children:'children', title:'name', key: 'id' }"
              :check-strictly="!checkStrictly"
              v-model:checked-keys="selectedIds"
              v-model:expanded-keys="expandedIds"
              :checkable="multiple"
              :selectable="false"
      >
        <template  #title="{ name }">
          <span v-if="name.indexOf(keyword) > -1">
            {{name.substring(0,name.indexOf(keyword))}}
            <span :style="'color:' + themeStore.colorPrimary">{{keyword}}</span>
            {{name.substring(name.indexOf(keyword) + keyword.length)}}
          </span>
          <span v-else>{{ name }}</span>
        </template>
      </a-tree>
    </div>
  </div>
</template>

<script setup lang="ts">
// 部门信息
import {ref, watch} from "vue";
import {getDeptOption} from "@/api/system/dept/dept.ts";
import {flattenTreeData} from "@/utils/tree.ts";
import {useThemeStore} from "@/stores/modules/theme.ts";
import {cloneDeep} from 'lodash-es';

const themeStore = useThemeStore();
const props = defineProps({
  // 双向绑定
  modelValue: {},
  // 是否支持多选
  multiple: {
    type: Boolean,
    default: false
  },
  // 组件最大高度
  maxHeight: {
    type: Number,
    default: 300
  },
})

// 部门树
const sysDeptList = ref<Array<SysDept>>([])
// 扁平化部门树
const flattenDeptList: Array<SysDept> = []
// 不进行双向绑定的树
const originTree: Array<SysDept> = []
// 选中的节点数据
const selectedIds = ref<Array<String>>([])
// 展开的节点
const expandedIds = ref<Array<String>>([])
// 全部选中
const checkedAll = ref<boolean>(false)
// 展开折叠
const expanded = ref<boolean>(false)
// 父子关联
const checkStrictly = ref<boolean>(false)
// 筛选关键词
const keyword = ref<string>('')

const emits = defineEmits(["update:modelValue"])

const showSel = () => {
  console.log(selectedIds.value)
}

// 加载部门信息
const initDept = async () => {
  const resp = await getDeptOption()
  if (resp.code === 200) {
    // 部门树
    sysDeptList.value = resp.data
    // 扁平化部门树
    flattenTreeData(resp.data, flattenDeptList)
    // 不进行双向绑定的树
    originTree.push(...resp.data)
    // 回显双向绑定
    // handleVmodel()
  }
}
initDept()

// 处理数据选中
const handleCheckedAllKeys = () => {
  selectedIds.value = []
  if (checkedAll.value) {
    selectedIds.value.push(...flattenDeptList.map(item => item.id))
  }
}

// 处理全部展开折叠
const handleExpanded = () => {
  expandedIds.value = []
  if (expanded.value) {
    expandedIds.value.push(...flattenDeptList.map(item => item.id))
  }
}

const handleVModel = (checkIds: string[]) => {

}

// 根据关键词过滤树
const filterTreeByLabel = (tree: Array<SysDept>, keyword: string) => {
  const cloneTree = cloneDeep(tree)
  const filterNode = (node) => {
    if (node.children) {
      node.children = node.children.map(filterNode).filter(Boolean);
    }
    return node.name.includes(keyword) || (node.children && node.children.length) ? node : null;
  };

  return cloneTree.map(filterNode).filter(Boolean);
};

// 处理回显双向绑定
// const handleVmodel = () => {
//   const vModel = props.modelValue
//   if (!vModel) {
//     return
//   }
//   if (Array.isArray(vModel)) {
//     vModel.forEach(v => {
//       if (!selectedIds.value.includes(v)) {
//         selectedIds.value.push(v)
//       }
//     })
//   } else {
//     if (!selectedIds.value.includes(vModel)) {
//       selectedIds.value.push(vModel)
//     }
//   }
// }

// 监听关键词筛选
watch(() => keyword.value, (value) => {
  if (value) {
    // 关键词输入时全部展开
    if (!expanded.value) {
      expanded.value = true
      handleExpanded()
    }
    // 对树型结构进行过滤
    sysDeptList.value = filterTreeByLabel(originTree, value)
  } else {
    // value 为空时，还原树
    sysDeptList.value = originTree
  }
})

// 双向绑定
watch(() => selectedIds.value, (value) => {
  let vModelData = []
  // 单选时监听到的数据类型不同
  if (value.checked) {
    vModelData.push(...cloneDeep(value.checked))
  } else {
    vModelData.push(...cloneDeep(value))
  }

  // 多选集合
  if (props.multiple) {
    emits("update:modelValue",vModelData)
  }
  // 单选元素 || null
  else {
    if (vModelData.length > 0) {
      emits("update:modelValue",vModelData[0])
    } else {
      emits("update:modelValue",null)
    }
  }
  // 全选标识
  checkedAll.value = vModelData.length === flattenDeptList.length;
},{deep: true})

</script>
<style>
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
<style scoped>
.dept-card {
  border-radius: 8px;
  padding: 16px;
  margin-right: 3px;
}
</style>