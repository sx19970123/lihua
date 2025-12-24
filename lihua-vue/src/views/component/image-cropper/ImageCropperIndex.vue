<template>
  <a-card>
    <template #title>
      <a-typography-link :href="baseDocApi + '/doc-web/components/image-cropper'" target="_blank">组件文档</a-typography-link>
    </template>
    <a-flex vertical :gap="16" style="margin-top: var(--lihua-space-base)">
      <demo1/>
    </a-flex>
  </a-card>
</template>

<script setup lang="ts">
import Demo1 from './Demo1.vue'

const baseDocApi = import.meta.env.VITE_APP_DOC_API
</script>