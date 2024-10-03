<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%"
             :auto-complete="false"
             :is-complete="middleComplete"
             @card-click="handleClick"
             :hover-scale="1.03"
             :expanded-width="600"
             :middle-style="{'background':themeStore.$state.isDarkTheme ? '#141414': '#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card style="border: none;" :body-style="{height: '100px'}" class="card-background">
        <a-typography-title :level="4">前端框架</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前Vue版本为 </a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">3.5.7</a-typography-text>
        </a-typography-text>

      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test" style="border: none;height: 610px">
        <a-typography-title :level="5">
          前端框架
        </a-typography-title>
        <a-typography-title :level="5">
          技术概览
        </a-typography-title>
        <a-typography-text>
          <p style="text-indent: 2em">
            前端采用 Vue 3.5.x 框架进行开发，使用 TypeScript 语言，集成了 Pinia 进行状态管理、VueRouter 进行路由管理，
            前端组件库为 Ant Design Vue 4.x。项目中提供了多种集成组件：选择卡片、小组件展示卡片、字典标签、图标选择器、颜色选择器、用户选择器等以提高开发效率。
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
          <a-descriptions-item label="ui框架">ant-design-vue</a-descriptions-item>
          <a-descriptions-item label="浏览器指纹">fingerprintjs</a-descriptions-item>
          <a-descriptions-item label="异步请求">axios</a-descriptions-item>
          <a-descriptions-item label="数据加密">crypto-js</a-descriptions-item>
          <a-descriptions-item label="日期工具类">dayjs</a-descriptions-item>
          <a-descriptions-item label="动画库">gsap</a-descriptions-item>
          <a-descriptions-item label="cookie工具">js-cookie</a-descriptions-item>
          <a-descriptions-item label="工具库">lodash</a-descriptions-item>
          <a-descriptions-item label="路由切换进度条">nprogress</a-descriptions-item>
          <a-descriptions-item label="状态管理工具">pinia</a-descriptions-item>
          <a-descriptions-item label="富文本工具">vditor</a-descriptions-item>
          <a-descriptions-item label="图片裁剪工具">vue-cropper</a-descriptions-item>
          <a-descriptions-item label="vue路由">vue-router</a-descriptions-item>
        </a-descriptions>
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
  background-image: url("../static/vue.png");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
