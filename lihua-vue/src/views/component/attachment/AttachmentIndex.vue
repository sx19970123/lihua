<template>
  <a-flex vertical :gap="16">
    <a-alert message="最大上传数量为10，一般上传最大允许10M附件，分片上传最大允许上传100M附件，每晚定时任务会将已上传的附件清除，演示环境文件服务为本地文件。"/>
    <a-card>
      <template #title>
        <a-typography-link :href="baseDocApi + '/document/component/ATTACHMENT-UPLOAD'" target="_blank">组件文档</a-typography-link>
      </template>
      <a-flex vertical :gap="16" style="margin-top: 16px">
        <demo1/>
        <demo2/>
        <demo3/>
        <demo4/>
      </a-flex>
    </a-card>
  </a-flex>
</template>

<script setup lang="ts">
import Demo1 from './Demo1.vue'
import Demo2 from './Demo2.vue'
import Demo3 from './Demo3.vue'
import Demo4 from './Demo4.vue'
const baseDocApi = import.meta.env.VITE_APP_DOC_API
</script>