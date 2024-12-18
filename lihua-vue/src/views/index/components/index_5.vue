<template>
  <div>
    <expandable-card
               style="width: 100%"
               :is-complete="middleComplete"
               @card-click="handleClick"
               :hover-scale="1.03"
               :expanded-width="600"
               :expanded-height="610"
    >
      <template #overview>
        <a-card class="card-background" :body-style="{height: '100px'}">
          <a-typography-title :level="4" ellipsis>免费开源</a-typography-title>
          <a-typography-text ellipsis type="secondary">
            <a-typography-text type="secondary">基于</a-typography-text>
            <a-typography-text :style="{color:themeStore.getColorPrimary()}">MIT</a-typography-text>
            <a-typography-text type="secondary">开源协议</a-typography-text>
          </a-typography-text>
        </a-card>
      </template>
      <template #middle>
        <a-card class="card-background child" :body-style="{height: '100px'}">
          <a-typography-title :level="4" ellipsis>免费开源</a-typography-title>
          <a-typography-text ellipsis type="secondary">
            <a-typography-text type="secondary">基于</a-typography-text>
            <a-typography-text :style="{color:themeStore.getColorPrimary()}">MIT</a-typography-text>
            <a-typography-text type="secondary">开源协议</a-typography-text>
          </a-typography-text>
        </a-card>
      </template>
      <template #detail>
        <a-card class="scrollbar card-background" id="test">
          <a-typography-title :level="4" ellipsis>免费开源</a-typography-title>
          <a-typography-text ellipsis type="secondary">
            <a-typography-text type="secondary">基于</a-typography-text>
            <a-typography-text :style="{color:themeStore.getColorPrimary()}">MIT</a-typography-text>
            <a-typography-text type="secondary">开源协议</a-typography-text>
          </a-typography-text>
          <br/>
          <div class="scrollbar dark-overlay" style="height: 484px;margin-top: 16px">
            <a-typography-text type="secondary">免费全开源且可商用，无版权问题</a-typography-text>
            <img src="../static/bq.png" style="width: 100%;border-radius: 8px;margin-top: 8px" alt=""/>
          </div>
        </a-card>
      </template>
    </expandable-card>
  </div>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
import {ref} from "vue";
import {useThemeStore} from "@/stores/theme.ts";
const themeStore = useThemeStore();
const middleComplete = ref<boolean>(false)
const handleClick = (show:boolean) => {
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
.child {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("../static/狸花猫-小黑子-期待.svg");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
