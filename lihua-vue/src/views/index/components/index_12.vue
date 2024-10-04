<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%; cursor: pointer"
             :auto-complete="false"
             :is-detail-visible="false"
             :hover-scale="1"
             :expanded-width="600"
             @card-click="handleClick"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card :body-style="{height: '124px'}">
        <a-typography-title :level="4" ellipsis>前端文档</a-typography-title>
        <a-typography-text type="secondary" ellipsis>卡片展示组件、用户选择组件、图标组件...</a-typography-text>
      </a-card>
    </template>
  </card-show>
</template>
<script setup lang="ts">
import CardShow from "@/components/card-show/index.vue";
import {getCurrentInstance} from "vue";
import router from "@/router";
const componentName = getCurrentInstance()?.type.__name
const handleClick = (key:string,show:boolean) => {
  router.push("/document/web")
}
</script>
<style scoped>

</style>