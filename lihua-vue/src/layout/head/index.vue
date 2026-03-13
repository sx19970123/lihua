<template>
<!--  右侧头部组件-->
  <a-flex align="center">
<!--    菜单搜索-->
    <menu-search/>
<!--    通知公告-->
    <lock/>
<!--    通知公告-->
    <notification/>
<!--    全屏-->
    <windows-change/>
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
import MenuSearch from "@/layout/head/components/menu-search/index.vue"
import Lock from "@/layout/head/components/lock/index.vue"
</script>
