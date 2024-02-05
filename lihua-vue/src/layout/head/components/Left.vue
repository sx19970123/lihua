<template>
  <a-flex justify="start" align="center" :gap="24">
    <!--    菜单收缩-->
    <HeadCollapsed/>
    <!--    面包屑-->
    <Breadcrumb/>
  </a-flex>
</template>

<script setup lang="ts">
import Breadcrumb from "@/layout/head/components/breadcrumb/index.vue";
import HeadCollapsed from "@/layout/head/components/collapsed/index.vue";
</script>

<style scoped>

</style>
