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
          <a-typography-title :level="4" ellipsis>移动端</a-typography-title>
          <a-typography-text ellipsis type="secondary">
            <a-typography-text type="secondary">基于</a-typography-text>
            <a-typography-text :style="{color:themeStore.getColorPrimary()}">uni-app</a-typography-text>
            <a-typography-text type="secondary">开发</a-typography-text>
          </a-typography-text>
        </a-card>
      </template>
      <template #middle>
        <a-card class="card-background child" :body-style="{height: '100px'}">
          <a-typography-title :level="4" ellipsis>移动端</a-typography-title>
          <a-typography-text ellipsis type="secondary">
            <a-typography-text type="secondary">基于</a-typography-text>
            <a-typography-text :style="{color:themeStore.getColorPrimary()}">uni-app</a-typography-text>
            <a-typography-text type="secondary">开发</a-typography-text>
          </a-typography-text>
        </a-card>
      </template>
      <template #detail>
        <a-card class="scrollbar card-background" id="test">
          <a-typography-title :level="4" ellipsis>移动端</a-typography-title>
          <a-typography-text ellipsis type="secondary">
            <a-typography-text type="secondary">基于</a-typography-text>
            <a-typography-text :style="{color:themeStore.getColorPrimary()}">uni-app</a-typography-text>
            <a-typography-text type="secondary">开发</a-typography-text>
          </a-typography-text>
          <br/>
          <div class="scrollbar dark-overlay" style="height: 484px;margin-top: var(--lihua-space-base)">
            <a-typography-title :level="5">
              技术概览
            </a-typography-title>
            <a-typography-text>
              <p style="text-indent: 2em">
                基于「狸花猫后台管理系统」的业务扩展方案，uni-app 开发，适配 App 与微信小程序，与 Web 端能力保持一致。
                基础框架为Vue 3 + TypeScript，内置pinia，支持请求路由拦截，与web端开发体验一致。
                <a href="https://gitee.com/yukino_git/lihua-app" target="_blank">项目仓库</a>
              </p>
            </a-typography-text>
            <a-typography-title :level="5">
              项目依赖
            </a-typography-title>
            <a-descriptions bordered :column="1">
              <a-descriptions-item label="前端框架">vue</a-descriptions-item>
              <a-descriptions-item label="路由">sard-uniapp</a-descriptions-item>
              <a-descriptions-item label="状态管理工具">pinia</a-descriptions-item>
              <a-descriptions-item label="全局根节点工具">uni ku root</a-descriptions-item>
              <a-descriptions-item label="ui框架">sard-uniapp</a-descriptions-item>
              <a-descriptions-item label="异步请求">sard-uniapp</a-descriptions-item>
              <a-descriptions-item label="数据加密">crypto-js & wxmp-rsa</a-descriptions-item>
              <a-descriptions-item label="日期工具类">dayjs</a-descriptions-item>
              <a-descriptions-item label="工具库">lodash</a-descriptions-item>
              <a-descriptions-item label="图片裁剪工具">sard-uniapp</a-descriptions-item>
            </a-descriptions>
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
  background-image: url("../static/uni.png");
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
  background-image: url("../static/uni.png");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
