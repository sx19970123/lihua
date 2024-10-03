<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%"
             :auto-complete="false"
             :is-complete="middleComplete"
             @card-click="handleClick"
             :hover-scale="1.01"
             :expanded-width="600"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card :body-style="{height: '263px'}" class="card-background">
        <a-typography-title :level="4">更新日志</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">最新版本为：</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}"> 1.0.0 </a-typography-text>
          <a-typography-text>

          </a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #middle>
      <a-card :body-style="{height: '263px'}" class="card-background child">
        <a-typography-title :level="4">更新日志</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">最新版本为：</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}"> 1.0.0 </a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar card-background" id="test" style="height: 600px">
        <a-typography-title :level="4">更新日志</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">最新版本为：</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}"> 1.0.0 </a-typography-text>
        </a-typography-text>
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
  background-image: url("../static/狸花猫-小黑子-超凶.svg");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
.child {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("../static/狸花猫-小黑子-超凶.svg");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>