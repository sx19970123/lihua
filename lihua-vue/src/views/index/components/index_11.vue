<template>
  <expandable-card
             style="width: 100%; cursor: pointer"
             :is-detail-visible="false"
             :hover-scale="1"
             :expanded-width="600"
             @card-click="handleClick"
             :expanded-height="610"
  >
    <template #overview>
      <a-card :body-style="{height: '124px'}">
        <a-typography-title :level="4" ellipsis>后端文档</a-typography-title>
        <a-typography-text type="secondary" ellipsis>后端项目结构、重要的全局功能、各层开发实例、工具类用法...</a-typography-text>
      </a-card>
    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
const handleClick = (show:boolean) => {
  window.open(import.meta.env.VITE_APP_DOC_API + "/document/SERVER","_blank");
}
</script>
<style scoped>

</style>