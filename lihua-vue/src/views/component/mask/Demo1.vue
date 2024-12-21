<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <a-button @click="showMask = true" style="width: 120px">点击打开遮罩</a-button>
  <a-typography-text>点击遮罩可关闭</a-typography-text>
  <Mask :show-mask="showMask" @click="showMask = false"/>
</template>

<script setup lang="ts">
import Mask from "@/components/mask/index.vue"
import {ref} from "vue";
const showMask = ref<boolean>(false)
</script>
