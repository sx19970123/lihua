<template>
  <div class="unselectable">
    <a-checkable-tag v-if="multiple" v-model:checked="treeSetting.checked" @change="handleCheckedAll">全选/全不选</a-checkable-tag>
    <a-checkable-tag v-if="multiple" v-model:checked="treeSetting.checkStrictly" @click="treeSetting.checkStrictly ? checkedKeys = [] : ''">父子关联</a-checkable-tag>
    <a-checkable-tag v-model:checked="treeSetting.expand" @change="handleExpandAll">展开/折叠</a-checkable-tag>
    <a-card :body-style="{padding: '8px'}" style="margin-top: 4px">
      <a-tree :tree-data="treeData"
              :field-names="fieldNames"
              :check-strictly="!treeSetting.checkStrictly"
              v-model:expanded-keys="expandKeys"
              v-model:checked-keys="checkedKeys"
              :selectable="!multiple"
              :checkable="multiple"
              @check="handleSelect"
              @select="handleSelect"
      >
        <template #title="item" v-if="hasTitleSlot">
          <slot name="title" v-bind="{ ...item }"/>
        </template>
      </a-tree>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
import {traverse, flattenTree} from "@/utils/Tree.ts";
import { useSlots } from 'vue';
// 是否使用具名插槽title
const slots = useSlots();
const hasTitleSlot = !!slots.title

// 接收的参数：
// treeData 树形结构数据；
// fieldNames 树形结构字段对应别名
const {treeData, fieldNames = {
  children: 'children',
  label: 'label',
  key: 'id',
}, multiple = true, modelValue} = defineProps<{
  // 树形结构数据
  treeData: Array<any>,
  // 树形结构别名
  fieldNames?: {
    children: string,
    title: string,
    key: string
  },
  // 是否支持多选
  multiple?: boolean,
  // 双向绑定数据
  modelValue: any[] | any,
}>()
const emits = defineEmits(['update:modelValue'])

defineExpose({
  reset: () => handleReset()
})

// 树形结构设置类型
type TreeSettingType = {
  // 是否全部展开
  expand: boolean,
  // 父子联动
  checkStrictly: boolean
  // 是否全部选中
  checked: boolean,
}
// 树形结构控制属性
const treeSetting = ref<TreeSettingType>({expand: false, checkStrictly: false, checked: false});

// 展开的节点
const expandKeys = ref<any[]>([])
// 选中的节点
const checkedKeys = ref<any[]>([])
// 全部节点
const allKeys = ref<any[]>([])
// 显示父子关联打开时的提示
const visibleStrictlyTips = ref<boolean>(false)

// 处理全选
const handleCheckedAll = () => {
  checkedKeys.value = []
  if (treeSetting.value.checked) {
    allKeys.value.forEach((key) => {
      checkedKeys.value.push(key)
    })
  }
  // 处理双线绑定
  handleUpdateModelValue(checkedKeys.value)
}

// 处理全部展开
const handleExpandAll = () => {
  expandKeys.value = []
  if (treeSetting.value.expand) {
    allKeys.value.forEach((key) => {
      expandKeys.value.push(key)
    })
  }
}

// 处理重置组件：全部取消选中、取消展开、关闭父子关联，双向绑定数据清空
const handleReset = () => {
  treeSetting.value.expand = false
  treeSetting.value.checked = false
  treeSetting.value.checkStrictly = false
  handleExpandAll()
  handleCheckedAll()
}

// 处理选中数据
const handleSelect = (selectedKeys: {checked: any[]} | any[]) => {
  let selected = []
  // 多选且关闭父子联动情况下，selectedKeys返回数据为对象，反之返回数据为集合，对数据进行同一处理
  if (multiple && !treeSetting.value.checkStrictly) {
    selectedKeys = selectedKeys as {checked: any[]}
    selected = selectedKeys.checked
  } else {
    selectedKeys = selectedKeys as any[]
    selected = selectedKeys
  }

  // 处理全选回显
  handleAllChecked()
  // 处理双向绑定法
  handleUpdateModelValue(selected)
}

// 处理双线绑定
const handleUpdateModelValue = (selected: any[]) => {
  // 多选情况下，v-model绑定数组
  if (multiple) {
    emits('update:modelValue', selected)
  }
  // 反之绑定单个元素
  else {
    emits('update:modelValue', selected[0])
  }
}

// 处理全选回显
const handleAllChecked = () => {
  // 当allKeys与checkedKeys集合数量一致时，全选按钮回显
  treeSetting.value.checked = checkedKeys.value.length === allKeys.value.length
}

// 将树形结构key收集到 allKeys 中
const handleAllKeys = () => {
  const treeList = flattenTree(treeData)
  if (treeList && treeList.length > 0) {
    allKeys.value = treeList.map(item => item[fieldNames.key])
  }
  // 处理全选回显
  handleAllChecked()
}
handleAllKeys()

// 监听modelValue双向绑定变化
watch(() => modelValue, () => {
  if (modelValue) {
    if (multiple) {
      checkedKeys.value = modelValue
    } else {
      checkedKeys.value = [modelValue]
    }
    // 处理全选回显
    handleAllChecked()
  }
})
</script>

<style scoped>

</style>