<template>
  <a-typography-title :level="4">单选树形结构</a-typography-title>
  <a-typography-text>选中的值：{{value}}</a-typography-text>
  <a-row>
    <a-col :span="6">
      <easy-tree-select v-if="test_tree.length > 0" :tree-data="test_tree" v-model="value" :multiple="false"/>
    </a-col>
  </a-row>
</template>
<script setup lang="ts">
import EasyTreeSelect from '@/components/easy-tree-select/index.vue'
import {initDict} from "@/utils/Dict.ts"
import {ref} from "vue";
const {test_tree} = initDict("test_tree")
const value = ref<string>()
</script>