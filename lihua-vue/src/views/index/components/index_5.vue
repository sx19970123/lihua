<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%"
             :auto-complete="false"
             :is-complete="middleComplete"
             @card-click="handleClick"
             :hover-scale="1.03"
             :expanded-width="600"
             :middle-style="{'background':themeStore.$state.isDarkTheme ? '#141414': '#fff','border-radius':' 8px'}"
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
      <a-card class="scrollbar" id="test" style="border: none;height: 610px">
        <a-typography-title :level="5">免费开源</a-typography-title>
        <a-typography-text>基于MIT开源协议，免费全开源且可商用，无版权问题</a-typography-text>
        <img src="../static/bq.png" style="width: 100%;border-radius: 8px;margin-top: 8px" alt=""/>
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
  background-image: url("../static/狸花猫-小黑子-期待.svg");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
