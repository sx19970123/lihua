<template>
  <expandable-card
             style="width: 100%"
             :is-complete="middleComplete"
             :hover-scale="1.03"
             :expanded-width="600"
             :expanded-height="610"
  >
    <template #overview>
      <a-card :body-style="{height: '100px'}" class="card-background">
        <a-typography-title :level="4" ellipsis>后端</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前SpringBoot版本为</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{versionInfo.springBootVersion}}</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #middle>
      <a-card :body-style="{height: '100px'}" class="card-background child">
        <a-typography-title :level="4" ellipsis>后端</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前SpringBoot版本为</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{versionInfo.springBootVersion}}</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar card-background" id="test">
        <a-typography-title :level="4" ellipsis>后端</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前SpringBoot版本为</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{versionInfo.springBootVersion}}</a-typography-text>
        </a-typography-text>
        <div class="scrollbar" style="height: 484px;margin-top: var(--lihua-space-base)">
          <a-typography-title :level="5">
            技术概览
          </a-typography-title>
          <a-typography-text>
            <p style="text-indent: 2em">
              后端采用了 Spring Boot 4.x 框架进行开发，Java 版本为 25，
              使用 Spring Security 进行权限管理。
              持久层框架采用 MyBatis-Plus，理论上支持 MP 所支持的所有数据库类型，当前使用的是 MySQL 数据库。
            </p>
          </a-typography-text>
          <a-typography-title :level="5">
            项目依赖
          </a-typography-title>
          <a-descriptions bordered :column="1">
            <a-descriptions-item label="系统框架">spring-boot-starter-parent</a-descriptions-item>
            <a-descriptions-item label="Web">spring-boot-starter-web</a-descriptions-item>
            <a-descriptions-item label="安全框架">spring-boot-starter-security</a-descriptions-item>
            <a-descriptions-item label="切面编程">spring-boot-starter-aop</a-descriptions-item>
            <a-descriptions-item label="数据库">mysql-connector-j</a-descriptions-item>
            <a-descriptions-item label="持久层框架">mybatis-plus-spring-boot3-starter</a-descriptions-item>
            <a-descriptions-item label="lombok">lombok</a-descriptions-item>
            <a-descriptions-item label="系统缓存">spring-boot-starter-data-redis</a-descriptions-item>
            <a-descriptions-item label="系统缓存">spring-integration-redis</a-descriptions-item>
            <a-descriptions-item label="令牌工具">java-jwt</a-descriptions-item>
            <a-descriptions-item label="接口限流">guava</a-descriptions-item>
            <a-descriptions-item label="excel导入导出">fesod</a-descriptions-item>
            <a-descriptions-item label="表单验证">spring-boot-starter-validation</a-descriptions-item>
            <a-descriptions-item label="服务器信息">oshi-core</a-descriptions-item>
            <a-descriptions-item label="定时任务">Snail Job</a-descriptions-item>
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
const middleComplete = ref<boolean>(false)
</script>
<style scoped>
.card-background {
  background-image: url("../static/spring-boot.png");
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
  background-image: url("../static/spring-boot.png");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
