<template>
  <template v-for="item in props.dictDataOption">
<!--    label标签-->
    <a-tag v-if="item.value === props.dictDataValue"
           :color="item.tagStyle"
           :bordered="props.bordered">
      <template v-if="props.fullTreeNode">
<!--       rootTreeNodePrefix 以分割符开头情况下，去除首位分割符 -->
        <template v-if="props.rootTreeNodePrefix.startsWith(props.fullTreeSeparator)">
          {{ props.rootTreeNodePrefix.substring(props.fullTreeSeparator.length) + props.fullTreeSeparator + item.label }}
        </template>
<!--       自定义 rootTreeNodePrefix 的情况下保留 rootTreeNodePrefix -->
        <template v-else>
          {{ props.rootTreeNodePrefix + props.fullTreeSeparator + item.label }}
        </template>
      </template>
      <template v-else>
        {{ item.label }}
      </template>
    </a-tag>
<!--    递归调用组件-->
    <dict-tag v-else-if="item.children"
              :dict-data-value="props.dictDataValue"
              :dict-data-option="item.children"
              :bordered="props.bordered"
              :root-tree-node-prefix="props.rootTreeNodePrefix === '' ? props.fullTreeSeparator + item.label : props.rootTreeNodePrefix + props.fullTreeSeparator + item.label"
    />
  </template>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import dictTag from "@/components/dict-tag/index.vue"
// 定义字典选项对象的类型
interface DictOption {
  value: string
  label: string
  tagStyle?: string
  bordered?: boolean
  children: Array<DictOption>
}

// 从父组件接收参数
const props = defineProps({
  // 字典data集合
  dictDataOption: {
    type: Array as () => Array<DictOption>,
    required: true
  },
  // 被翻译的字典值
  dictDataValue: {
    type: String,
    required: true
  },
  // 是否有边框
  bordered: {
    type: Boolean,
    default: true
  },
  // 展示树型结构全路径
  fullTreeNode: {
    type: Boolean,
    default: false
  },
  // 树型结构分隔符
  fullTreeSeparator: {
    type: String,
    default: '/'
  },
  // 树型跟节点前缀节点
  rootTreeNodePrefix: {
    type: String,
    default: ''
  }
})
</script>