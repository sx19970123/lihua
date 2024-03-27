<template>
  <a-flex align="center" style="margin-right: 16px">
      <language v-if="false"/>
      <windows-change/>
      <user/>
  </a-flex>
</template>
<script setup lang="ts">
import User from "@/layout/head/components/user/index.vue"
import Language from "@/layout/head/components/language/index.vue"
import WindowsChange from "@/layout/head/components/window-change/index.vue"
</script>
