<template>
  <div style="padding: 16px;">
    <a-typography-title :level="3">入门</a-typography-title>
    <a-alert>
      <template #message>
        <a-typography-text>开发前应具备 vue3 和 TypeScript 基础。对相应知识掌握不完整的同学推荐观看 </a-typography-text>
        <a-typography-link href="https://www.bilibili.com/video/BV1wR4y1377K/?share_source=copy_web&vd_source=21488b91c9ae03cb3f590ee0d0bdb943" target="_blank">TypeScript 教程</a-typography-link>
        <a-typography-text> 和 </a-typography-text>
        <a-typography-link href="https://www.bilibili.com/video/BV1dS4y1y7vd/?share_source=copy_web&vd_source=21488b91c9ae03cb3f590ee0d0bdb943" target="_blank">vue3 教程</a-typography-link>
        <a-typography-text> 进行相应学习（作者是看他视频学的，看完最差能做出这个系统的程度）</a-typography-text>
      </template>
    </a-alert>
    <br/>
    <a-typography-title :level="5">需了解的库</a-typography-title>
    <a-descriptions bordered>
      <a-descriptions-item label="UI组件库">
        <a-typography-link href="https://antdv.com/components/overview-cn" target="_blank">Ant Design Vue 4</a-typography-link>
      </a-descriptions-item>
      <a-descriptions-item label="前端工具库">
        <a-typography-link href="https://www.lodashjs.com/" target="_blank">Lodash </a-typography-link>
      </a-descriptions-item>
      <a-descriptions-item label="时间日期工具库">
        <a-typography-link href="https://day.js.org/docs/zh-CN/parse/now" target="_blank">Day.js</a-typography-link>
      </a-descriptions-item>
    </a-descriptions>
    <br/>
    <a-typography-title :level="5">前端工程结构</a-typography-title>
    <a-typography-text>重点关注 </a-typography-text>
    <a-typography-text mark> api、components、store、views </a-typography-text>
    <a-typography-text>
      <pre>
├── public                     // 公共文件
│   └──  vditor                // vditor富文本编辑器相关本地库
├── src                        // 源代码
│   ├── api                    // axios请求及TS类型存放目录
│   ├── assets                 // 图片等静态资源
│   ├── components             // 全局公用组件
│   ├── directive              // 全局指令
│   ├── layout                 // 布局
│   ├── router                 // 路由
│   ├── static                 // css等静态资源
│   ├── store                  // 全局 store管理
│   ├── utils                  // 全局公用方法
│   ├── views                  // 所有业务组件都在此目录下
│   ├── App.vue                // 入口页面
│   ├── main.js                // 入口 加载组件 初始化等
│   ├── permission.js          // 路由守卫
│   └── settings.js            // 系统配置
├── .env.development           // 开发环境配置
├── .env.production            // 生产环境配置
├── .gitignore                 // git忽略项
├── .env.d.ts                  // ts环境声明文件
├── index.html                 // html主文件
├── package.json               // 依赖外部库管理文件
├── README.md                  // 安装依赖、启动、打包说明文件
├── tsconfig.app.json          // ts 配置文件
├── tsconfig.json              // ts 全局配置文件
├── tsconfig.node.json         // ts 配置文件
├── tsconfig.vitest.json       // ts 配置文件
├── vite.config.ts             // vite配置文件
├── vitest.config.ts           // vite配置文件
└── web-types.json             // web-types.json
        </pre>
    </a-typography-text>

    <a-typography-title :level="5">开发流程</a-typography-title>
  </div>
</template>

<script setup lang="ts">

</script>

<style scoped>

</style>