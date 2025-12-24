<template>
  <a-card>
    <template #title>
      <a-typography-link :href="baseDocApi + '/doc-web/components/table-setting'" target="_blank">组件文档</a-typography-link>
    </template>
    <a-flex vertical :gap="16" style="margin-top: var(--lihua-space-base)">
      <Demo1/>
      <demo2/>
      <Demo3/>
    </a-flex>
  </a-card>
</template>

<script setup lang="ts">
import Demo3 from './Demo3.vue'
import Demo2 from './Demo2.vue'
import Demo1 from './Demo1.vue'

const baseDocApi = import.meta.env.VITE_APP_DOC_API
</script>