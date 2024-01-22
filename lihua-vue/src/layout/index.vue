<template>
  <a-layout style="min-height: 100vh">
    <!--侧边栏-->
    <a-layout-sider class="scrollbar">
      <Side/>
    </a-layout-sider>
    <a-layout>
      <a-layout-header :style="{'height': '48px','background': '#fff','padding-left': '0px','padding-right': '0px'}">
        <!--页头-->
        <Head></Head>
        <!--标签页-->
        <ViewTabs></ViewTabs>
      </a-layout-header>
      <!--内容-->
      <a-layout-content :style="{'margin-top': '48px'}">
        <Content/>
      </a-layout-content>
      <a-layout-footer></a-layout-footer>
    </a-layout>
  </a-layout>

</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
</script>

<style scoped>
/* 滚动条 */
.scrollbar {
  height: 100vh;
  overflow: auto;
}

.scrollbar::-webkit-scrollbar {
  width: 6px;
}

.scrollbar::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 10px;
}

.scrollbar::-webkit-scrollbar-track {
  background-color: transparent;
  border-radius: 10px;
}
</style>
