<template>
<!--  右侧头部组件-->
  <a-flex align="center">
<!--    全屏-->
    <windows-change/>
<!--    通知公告-->
    <notification/>
<!--    默认部门-->
    <default-dept/>
<!--    用户-->
    <user/>
  </a-flex>
</template>
<script setup lang="ts">
import User from "@/layout/head/components/user/index.vue"
import WindowsChange from "@/layout/head/components/window-change/index.vue"
import Notification from "@/layout/head/components/notice/index.vue"
import DefaultDept from "@/layout/head/components/dept/index.vue"
</script>
