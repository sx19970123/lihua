<template>
  <a-card>
    <template #title>
      <a-typography-link :href="baseDocApi + '/document/component/SELECTABLE-CARD'" target="_blank">组件文档</a-typography-link>
    </template>
    <a-flex vertical :gap="16" style="margin-top: 16px">
      <demo1/>
      <demo2/>
      <demo3/>
      <demo4/>
    </a-flex>
  </a-card>
</template>

<script setup lang="ts">
import Demo1 from './Demo1.vue'
import Demo2 from './Demo2.vue'
import Demo3 from './Demo3.vue'
import Demo4 from './Demo4.vue'
const baseDocApi = import.meta.env.VITE_APP_DOC_API
</script>