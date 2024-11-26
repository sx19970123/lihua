<template>
  <div>
    <a-checkable-tag v-model:checked="treeSetting.checked" @change="handleCheckedAll">全选/全不选</a-checkable-tag>
    <a-checkable-tag v-model:checked="treeSetting.expand" @change="handleExpandAll">展开/折叠</a-checkable-tag>
    <a-checkable-tag v-model:checked="treeSetting.checkStrictly" @click="treeSetting.checkStrictly ? checkedKeys = [] : ''">父子关联</a-checkable-tag>
    <a-card :body-style="{padding: '8px'}" style="margin-top: 4px">
      <a-tree :tree-data="treeData"
              :field-names="fieldNames"
              :check-strictly="!treeSetting.checkStrictly"
              v-model:expanded-keys="expandKeys"
              v-model:checked-keys="checkedKeys"
              :selectable="false"
              checkable
      >
        <template #title="item" v-if="hasTitleSlot">
          <slot name="title" v-bind="{ ...item }"/>
        </template>
      </a-tree>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {traverse} from "@/utils/Tree.ts";
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
}} = defineProps<{
  // 树形结构数据
  treeData: Array<any>,
  // 树形结构别名
  fieldNames?: {
    children: string,
    title: string,
    key: string
  }
}>()

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
const expandKeys = ref<string[]>([])
// 选中的节点
const checkedKeys = ref<string[]>([])

// 处理全选
const handleCheckedAll = () => {
  checkedKeys.value = []
  if (treeSetting.value.checked) {
    // 遍历树
    traverse(treeData, (item) => {
      if (item[fieldNames.key]) {
        if (Array.isArray(checkedKeys.value)) {
          checkedKeys.value.push(item.id)
        }
      }
    })
  }
}

// 处理全部展开
const handleExpandAll = () => {
  expandKeys.value = []
  if (treeSetting.value.expand) {
    // 遍历树
    traverse(treeData, (item) => {
      if (item[fieldNames.key]) {
        expandKeys.value.push(item[fieldNames.key])
      }
    })
  }
}

</script>

<style scoped>

</style>