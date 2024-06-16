<template>
  <a-flex align="center">
      <language v-if="false"/>
      <notification/>
      <windows-change/>
      <default-dept/>
      <user/>
  </a-flex>
</template>
<script setup lang="ts">
import User from "@/layout/head/components/user/index.vue"
import Language from "@/layout/head/components/language/index.vue"
import WindowsChange from "@/layout/head/components/window-change/index.vue"
import Notification from "@/layout/head/components/notification/index.vue"
import DefaultDept from "@/layout/head/components/dept/index.vue"
</script>
