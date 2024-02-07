<template>
  <a-flex align="center" :gap="16">
    <user/>
    <layout-switch/>
  </a-flex>
</template>
<script setup lang="ts">
import LayoutSwitch from "@/layout/head/components/layout-switch/index.vue";
import User from "@/layout/head/components/user/index.vue"
</script>

<style scoped>

</style>
