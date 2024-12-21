<template>
  <a-typography-title :level="4">小号组件</a-typography-title>
  <a-typography-text>绑定数据：{{value}}</a-typography-text>
  <icon-select v-model="value" size="small"/>
</template>

<script setup lang="ts">
import IconSelect from "@/components/icon-select/index.vue"
import {ref} from "vue";
const value = ref<string>()
</script>
