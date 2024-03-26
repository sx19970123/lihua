<template>
  <a-dropdown>
    <div class="header-right-item header-right">
      <a-avatar :size="32" style="background-color: rgba(0,0,0,0)">
        <i18n style="font-size: 100px"/>
      </a-avatar>
    </div>
      <template #overlay>
      <a-menu>
        <a-menu-item>中文</a-menu-item>
        <a-menu-item>英文</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import {initDict} from "@/utils/dict"
const {sys_language} = initDict("sys_language")
</script>

<style scoped>

</style>
