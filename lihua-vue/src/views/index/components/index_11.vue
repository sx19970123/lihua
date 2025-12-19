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
        <a-typography-title :level="4" ellipsis>开发文档</a-typography-title>
        <a-typography-text type="secondary" ellipsis>前端 后端 移动端</a-typography-text>
      </a-card>
    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";

const handleClick = (show:boolean) => {
  window.open(import.meta.env.VITE_APP_DOC_API,"_blank");
}
</script>
<style scoped>

</style>