<template>
  <expandable-card
             style="width: 100%; cursor: pointer"
             @card-click="handleClick"
             :is-detail-visible="false"
             :hover-scale="1"
             :expanded-width="600"
             :expanded-height="610"
  >
    <template #overview>
      <a-card :body-style="{height: '124px'}">
        <a-typography-title :level="4" ellipsis>
          <img src="https://gitee.com/static/images/logo.svg?t=158106664" alt="" style="height: 40px" v-if="themeStore.isDarkTheme">
          <img src="https://gitee.com/static/images/logo-black.svg?t=158106664" alt="" style="height: 40px" v-else>
          码云主页
        </a-typography-title>
        <a-typography-text type="secondary" ellipsis>Yukino/狸花猫</a-typography-text>
      </a-card>
    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
import {useThemeStore} from "@/stores/theme.ts";
const themeStore = useThemeStore();
const handleClick = () => {
  window.open('https://gitee.com/yukino_git/lihua')
}
</script>
<style scoped>

</style>
