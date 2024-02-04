<template>
  <a-flex class="head" align="center" :gap="8">
    <layout-switch/>
    <layout-switch/>
    <layout-switch/>
  </a-flex>
</template>
<script setup lang="ts">
import LayoutSwitch from "@/layout/component/layout-switch/index.vue"
</script>

<style scoped>
.head {
  padding-left: 16px;
  padding-right: 16px;
}
</style>
