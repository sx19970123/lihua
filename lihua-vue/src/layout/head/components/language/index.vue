<template>
  <a-dropdown>
    <div class="header-right-item header-right">
      <a-avatar :size="32" style="background-color: rgba(0,0,0,0)">
        <I18n style="font-size: 20px" class="icon-default-color"/>
      </a-avatar>
    </div>
      <template #overlay>
      <a-menu>
        <a-menu-item v-for="item in sys_language" :key="item.value">{{item.label}}</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import {initDict} from "@/utils/dict"
const { sys_language } = initDict("sys_language")
</script>

