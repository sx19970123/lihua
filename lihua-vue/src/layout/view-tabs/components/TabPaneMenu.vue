<script setup lang="ts">

</script>

<template>
  <a-dropdown :trigger="['contextmenu']">
    <span style="font-size: 12px">首页</span>
    <template #overlay>
      <a-menu>
        <a-menu-item key="1">1st menu item</a-menu-item>
        <a-menu-item key="2">2nd menu item</a-menu-item>
        <a-menu-item key="3">3rd menu item</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<style scoped>

</style>
