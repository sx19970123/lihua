<template>
  <card-show
             style="width: 100%; cursor: pointer"
             :is-detail-visible="false"
             :hover-scale="1"
             :expanded-width="600"
             @card-click="handleClick"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card :body-style="{height: '124px'}">
        <a-typography-title :level="4" ellipsis>后端文档</a-typography-title>
        <a-typography-text type="secondary" ellipsis>新建子模块、获取用户上下文、工具类使用...</a-typography-text>
      </a-card>
    </template>
  </card-show>
</template>
<script setup lang="ts">
import CardShow from "@/components/card-show/index.vue";
import router from "@/router";
const handleClick = (show:boolean) => {
  router.push("/document/service")
}
</script>
<style scoped>

</style>