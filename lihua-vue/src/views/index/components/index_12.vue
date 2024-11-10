<template>
  <expandable-card
             style="width: 100%; cursor: pointer"
             :auto-complete="false"
             :is-detail-visible="false"
             :hover-scale="1"
             :expanded-width="600"
             @card-click="handleClick"
             :expanded-height="610"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card :body-style="{height: '124px'}">
        <a-typography-title :level="4" ellipsis>前端文档</a-typography-title>
        <a-typography-text type="secondary" ellipsis>卡片展示组件、用户选择组件、图标组件...</a-typography-text>
      </a-card>
    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
const handleClick = (show:boolean) => {
  window.open("http://120.26.52.238:81/#/client/","_blank")
}
</script>
<style scoped>

</style>