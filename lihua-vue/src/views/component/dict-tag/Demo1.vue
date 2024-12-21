<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <dict-tag dict-data-value="0" :dict-data-option="sys_status"/>
  <dict-tag dict-data-value="1" :dict-data-option="sys_status"/>
</template>

<script setup lang="ts">
import DictTag from "@/components/dict-tag/index.vue"
import {initDict} from "@/utils/Dict.ts"
const {sys_status} = initDict("sys_status")
</script>