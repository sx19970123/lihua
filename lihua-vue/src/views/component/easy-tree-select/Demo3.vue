<template>
  <a-typography-title :level="4">自定义插槽</a-typography-title>
  <a-typography-text>选中的值：{{value}}</a-typography-text>
  <a-row>
    <a-col :span="6">
      <easy-tree-select v-if="test_tree.length > 0" :tree-data="test_tree" v-model="value">
        <template #title="{keyword,id,label}">
          {{label + ' [' + id + ']'}}
        </template>
      </easy-tree-select>
    </a-col>
  </a-row>
</template>
<script setup lang="ts">
import EasyTreeSelect from '@/components/easy-tree-select/index.vue'
import {initDict} from "@/utils/Dict.ts"
import {ref} from "vue";
const {test_tree} = initDict("test_tree")
const value = ref<string[]>([])
</script>