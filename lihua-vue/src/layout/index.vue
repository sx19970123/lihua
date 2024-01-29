<template>
  <a-layout class="layout-height">
    <!--侧边栏-->
    <a-layout-sider breakpoint="lg"
                    class="sider-height scrollbar"
    >
      <Side/>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="layout-header">
        <!--页头-->
        <Head></Head>
        <!--标签页-->
        <ViewTabs></ViewTabs>
      </a-layout-header>
      <!--内容-->
      <a-layout-content class="layout-content">
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
.layout-height {
  min-height: 100vh
}
.layout-header {
  background: #fff;
  padding-left: 0px;
  padding-right: 0px;
}
.layout-content {
  margin-top: 38px;
}
.sider-height {
  height: 100vh;
}
</style>
