<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider></a-layout-sider>
    <a-layout>
      <a-layout-header :style="{'height': '48px','background': '#fff','padding-left': '0px','padding-right': '0px'}">
        <!--页头-->
        <Head></Head>
        <!--标签页-->
        <ViewTabs></ViewTabs>
      </a-layout-header>
      <a-layout-content></a-layout-content>
      <a-layout-footer></a-layout-footer>
    </a-layout>
  </a-layout>

</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";

</script>

<style scoped>

</style>
