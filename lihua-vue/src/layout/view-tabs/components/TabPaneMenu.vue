<template>
  <a-dropdown :trigger="['contextmenu']">
    <router-link :to="item.data.key"> {{ item.data.label }} </router-link>
    <template #overlay>
      <a-menu>
        <a-menu-item key="1">1st menu item</a-menu-item>
        <a-menu-item key="2">2nd menu item</a-menu-item>
        <a-menu-item key="3">3rd menu item</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
const item = defineProps({
  data: null
})
</script>
<style scoped>
  .router-link-active {
    text-decoration: none;
    color: #1677ff
  }
  a {
    text-decoration: none;
    color: rgba(0, 0, 0, 0.88)
  }
</style>
