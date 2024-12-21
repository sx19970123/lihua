<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <a-typography-text>绑定数据：{{value}}</a-typography-text>
  <editor v-model="value"/>
</template>

<script setup lang="ts">
import Editor from "@/components/editor/index.vue"
import {ref} from "vue";
const value = ref<string>()
</script>
