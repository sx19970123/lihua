<template>
  <expandable-card
             style="width: 100%"
             :hover-scale="1.03"
             :expanded-width="600"
             :expanded-height="610"
  >
    <template #overview>
      <a-card :body-style="{height: '100px'}" class="card-background">
        <a-typography-title :level="4" ellipsis>前端框架</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前Vue版本为 </a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{versionInfo.vueVersion}}</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #middle>
      <a-card :body-style="{height: '100px'}" class="card-background child">
        <a-typography-title :level="4" ellipsis>前端框架</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前Vue版本为 </a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{versionInfo.vueVersion}}</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar card-background" id="test">
        <a-typography-title :level="4" ellipsis>前端框架</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前Vue版本为 </a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{versionInfo.vueVersion}}</a-typography-text>
        </a-typography-text>
        <div class="scrollbar" style="height: 484px;margin-top: 16px">
          <a-typography-title :level="5">
            技术概览
          </a-typography-title>
          <a-typography-text>
            <p style="text-indent: 2em">
              前端采用 Vue 3.x 框架进行开发，使用 TypeScript 语言，集成了 Pinia 进行状态管理、VueRouter 进行路由管理，
              前端组件库为 Ant Design Vue 4.x。项目中提供了多种集成组件：选择卡片、小组件展示卡片、字典标签、图标选择器、颜色选择器、用户选择器等。
              <span >
              <p style="text-indent: 2em">
                  下面是系统基础目录结构
              </p>
            </span>
            </p>
          </a-typography-text>
          <a-typography-text>

          <pre>
lihua-vue                 // 前端工程
├── public                // 公共文件
├── src                   // 工程主目录
│    ├── api              // axios接口目录
│    ├── assets           // 静态资源
│    ├── components       // 公共组件
│    ├── directive        // vue指令
│    ├── layout           // layout布局
│    ├── router           // 路由
│    ├── static           // 静态资源
│    ├── stores           // pinia
│    ├── utils            // 工具类
│    ├── views            // 页面目录
│    ├── App.vue          // vue主文件
│    ├── main.ts          // vue 主入口配置文件
│    ├── permission.ts    // 路由守卫
│    ├── settings.ts      // 系统配置文件
├── package.json          // 项目包管理文件
├── tsconfig.json         // ts配置文件
├── vite.config.ts        // vite配置文件</pre>
          </a-typography-text>

          <a-typography-title :level="5">
            项目依赖
          </a-typography-title>
          <a-descriptions bordered :column="1">
            <a-descriptions-item label="前端框架">vue</a-descriptions-item>
            <a-descriptions-item label="vue路由">vue-router</a-descriptions-item>
            <a-descriptions-item label="状态管理工具">pinia</a-descriptions-item>
            <a-descriptions-item label="ui框架">ant-design-vue</a-descriptions-item>
            <a-descriptions-item label="浏览器指纹">@fingerprintjs/fingerprintjs</a-descriptions-item>
            <a-descriptions-item label="异步请求">axios</a-descriptions-item>
            <a-descriptions-item label="数据加密">crypto-js</a-descriptions-item>
            <a-descriptions-item label="日期工具类">dayjs</a-descriptions-item>
            <a-descriptions-item label="动画库">gsap</a-descriptions-item>
            <a-descriptions-item label="cookie工具">js-cookie</a-descriptions-item>
            <a-descriptions-item label="工具库">lodash</a-descriptions-item>
            <a-descriptions-item label="路由切换进度条">nprogress</a-descriptions-item>
            <a-descriptions-item label="富文本工具">vditor</a-descriptions-item>
            <a-descriptions-item label="图片裁剪工具">vue-cropper</a-descriptions-item>
          </a-descriptions>
        </div>
      </a-card>

    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
import {ref} from "vue";
import {useThemeStore} from "@/stores/theme.ts";
import {versionInfo} from "@/views/index/setting.ts";
const themeStore = useThemeStore();
</script>
<style scoped>
.card-background {
  background-image: url("../static/vue.png");
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
  background-image: url("../static/vue.png");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
