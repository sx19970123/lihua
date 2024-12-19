<template>
  <a-typography-title :level="4">树形字典回显</a-typography-title>
  <a-typography>某一节点的回显</a-typography>
  <dict-tag dict-data-value="2-2" :dict-data-option="test_tree"/>
  <a-typography>展示全路径</a-typography>
  <dict-tag dict-data-value="2-2-1" :dict-data-option="test_tree" full-tree-node/>
  <a-typography>路径前增加前缀</a-typography>
  <dict-tag dict-data-value="2-2-1" :dict-data-option="test_tree" full-tree-node root-tree-node-prefix="~"/>

  <a-typography-text strong>树形字典结构</a-typography-text>
  <a-row>
    <a-col :span="4">
      <a-tree style="padding: 16px"
              v-if="test_tree.length"
              :tree-data="test_tree"
              :field-names="{children:'children', title:'label'}"
              default-expand-all/>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import DictTag from "@/components/dict-tag/index.vue"
import {initDict} from "@/utils/Dict.ts"
const {test_tree} = initDict("test_tree")
</script>