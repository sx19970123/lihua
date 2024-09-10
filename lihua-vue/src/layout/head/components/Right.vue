<template>
  <a-flex align="center">
      <notification/>
      <windows-change/>
      <default-dept/>
      <user/>
  </a-flex>
</template>
<script setup lang="ts">
import User from "@/layout/head/components/user/index.vue"
import WindowsChange from "@/layout/head/components/window-change/index.vue"
import Notification from "@/layout/head/components/notice/index.vue"
import DefaultDept from "@/layout/head/components/dept/index.vue"
</script>
