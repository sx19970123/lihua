<template>
  <a-flex vertical :gap="8">
    <a-typography-title :level="4">分片上传</a-typography-title>
    <a-typography-text>绑定数据：{{modelValue}}</a-typography-text>
    <attachment-upload v-model="modelValue" :max-size="100" chunk :chunk-size="8" :chunk-upload-count="2"/>
  </a-flex>
</template>

<script setup lang="ts">
import AttachmentUpload from "@/components/attachment-upload/index.vue";
import {ref} from "vue";
const modelValue = ref<string>()
</script>