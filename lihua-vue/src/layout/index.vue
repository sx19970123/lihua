<template>
  <a-layout style="min-height: 100vh">
<!--    侧边栏-->
    <a-layout-sider>

    </a-layout-sider>
    <a-layout>
<!--      页头-->
      <a-layout-header :style="{background: '#fff'}">
        <app-header/>
      </a-layout-header>

<!--      内容-->
      <a-layout-content :style="{ margin: '24px 16px', padding: '24px', background: '#fff', minHeight: '280px' }">
        <!--      tabBar-->
        <app-tab-bar/>
      </a-layout-content>
<!--      页脚-->
      <a-layout-footer>
        <p>footer</p>
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>
<script lang="ts" setup>

import AppTabBar from "@/layout/components/AppTagsViews.vue";
import AppHeader from "@/layout/components/AppHeader.vue";
</script>
<style>
</style>
