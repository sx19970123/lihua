<template>
  <div class="unselectable">
    <a-checkable-tag v-if="multiple" v-model:checked="treeSetting.checked" @change="handleCheckedAll">全选/全不选</a-checkable-tag>
    <a-checkable-tag v-if="multiple" v-model:checked="treeSetting.checkStrictly" @click="handleCheckStrictly">父子关联</a-checkable-tag>
    <a-checkable-tag v-model:checked="treeSetting.expand" @change="handleExpandAll">展开/折叠</a-checkable-tag>
    <a-card :body-style="{padding: '8px'}" style="margin-top: 4px">
      <a-input class="keyword-input" placeholder="请输入关键词" v-model:value="keyword" allowClear @change="handleChangeKeyWord()"/>
      <a-tree v-if="deepCloneTreeData && deepCloneTreeData.length > 0"
              :tree-data="deepCloneTreeData"
              :field-names="fieldNames"
              :check-strictly="!treeSetting.checkStrictly"
              v-model:expanded-keys="expandKeys"
              v-model:checked-keys="checkedKeys"
              :selectable="!multiple"
              :checkable="multiple"
              @check="handleSelect"
              @select="handleSelect"
              @expand="handleExpand"
              ref="treeRef"
      >
        <template #title="item" v-if="hasTitleSlot">
          <slot name="title" v-bind="{ ...item }"/>
        </template>
        <template #title="data">
          <div v-if="data[fieldNames.title].indexOf(keyword) > -1">
            <span>{{data[fieldNames.title].substring(0,data[fieldNames.title].indexOf(keyword))}}</span>
            <span :style="{'color':  themeStore.getColorPrimary()}">{{keyword}}</span>
            <span>{{data[fieldNames.title].substring(data[fieldNames.title].indexOf(keyword) + keyword.length)}}</span>
          </div>
          <span v-else>{{ data[fieldNames.title] }}</span>
        </template>
      </a-tree>
      <div v-else>
        <a-empty/>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {ref, useTemplateRef, watch, useSlots, nextTick} from "vue";
import ATree from "ant-design-vue/es/tree/Tree"
import { traverse } from "@/utils/Tree.ts";
import { cloneDeep } from 'lodash-es'
import { useThemeStore } from "@/stores/theme.ts";
const themeStore = useThemeStore();
// 是否使用具名插槽title
const slots = useSlots();
const hasTitleSlot = !!slots.title
// treeRef实例
const treeRef = useTemplateRef<InstanceType<typeof ATree>>("treeRef")
// 接收的参数：
// treeData 树形结构数据；
// fieldNames 树形结构字段对应别名
const {treeData, fieldNames = {
  children: 'children',
  title: 'label',
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
// 全部父级节点（children）
const allParentKeys = ref<any[]>([])
// 模糊搜索
const keyword = ref<string>("")
// 深度克隆的树形数据，值会跟随关键词变化
const deepCloneTreeData = ref<any[]>(treeData)

// 处理全选
const handleCheckedAll = () => {
  // 递归遍历集合
  traverse(deepCloneTreeData.value, (item) => {
    if (item && item[fieldNames.key]) {
      const key = item[fieldNames.key]
      // 全选，将遍历出的树形结构key 添加到集合
      if (treeSetting.value.checked) {
        if (!checkedKeys.value.includes(key)) {
          checkedKeys.value.push(key)
        }
      }
      // 取消全选，将遍历出的树形结构key从集合去掉
      else {
        if (checkedKeys.value.includes(key)) {
          checkedKeys.value.splice(checkedKeys.value.indexOf(key), 1)
        }
      }
    }
  }, fieldNames.children)

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

// 处理父子关联，当父子关联为true时，从aTree组件的ref中获取checkedKeys后进行双向绑定
const handleCheckStrictly = () => {
  if (treeSetting.value.checkStrictly) {
    nextTick(()=> {
      treeSetting.value.checkStrictly = !treeSetting.value.checkStrictly
      checkedKeys.value = cloneDeep(treeRef.value?.checkedKeys)
      handleUpdateModelValue(checkedKeys.value)
      treeSetting.value.checkStrictly = !treeSetting.value.checkStrictly
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

// 处理关键词过滤
const handleChangeKeyWord = () => {
  const key = keyword.value
  if (key) {
    // 节点过滤
    deepCloneTreeData.value = handleFilterTree(treeData, key)
    // 展开全部树形结构
    treeSetting.value.expand = true
    handleExpandAll()
  } else {
    deepCloneTreeData.value = treeData
  }
}

// 处理过滤树
const handleFilterTree = (treeData: any[], keyword: string) => {
  const cloneTree = cloneDeep(treeData);

  const filterNode = (node: any): any => {
    if (node[fieldNames.children]) {
      node[fieldNames.children] = node[fieldNames.children].map(filterNode).filter((child: any) => !!child);
    }
    return node[fieldNames.title]?.includes(keyword) || (node[fieldNames.children] && node[fieldNames.children].length > 0) ? node : null;
  };

  return cloneTree.map(filterNode).filter((node: any) => !!node);
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

// 处理展开折叠回显
const handleExpand = () => {
  treeSetting.value.expand = expandKeys.value.length === allParentKeys.value.length
}

// 将树形结构key收集到 allKeys/allParentKeys 中
const handleAllKeys = () => {
  traverse(treeData, (item) => {
    if (item && item[fieldNames.key]) {
      allKeys.value.push(item[fieldNames.key])
      if (item[fieldNames.children] && item[fieldNames.children].length >= 0) {
        allParentKeys.value.push(item[fieldNames.key])
      }
    }
  }, fieldNames.children)

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
.keyword-input {
  height: 28px;
  margin-bottom: 4px
}
</style>