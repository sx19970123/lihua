<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%"
             :auto-complete="false"
             :is-complete="middleComplete"
             @card-click="handleClick"
             :hover-scale="1.03"
             :expanded-width="600"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card class="card-background" style="border: none;" :body-style="{height: '100px'}">
        <a-typography-title :level="4">免费开源</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">基于</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">MIT</a-typography-text>
          <a-typography-text type="secondary">开源协议</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test" style="border: none;height: 600px">
        <a-typography-title :level="4">基于 SpringBoot 3</a-typography-title>
      </a-card>
    </template>
  </card-show>
</template>
<script setup lang="ts">
import CardShow from "@/components/card-show/index.vue";
import {getCurrentInstance, ref} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
const themeStore = useThemeStore();
const componentName = getCurrentInstance()?.type.__name
const middleComplete = ref<boolean>(false)
const handleClick = (key:string,show:boolean) => {
  middleComplete.value = true
}
</script>
<style scoped>
.card-background {
  background-image: url("../static/mit.png");
  background-position-y: 2px; /* 增加10像素间距 */
  background-position-x: calc(100% - 6px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 42px 42px;
}
</style>