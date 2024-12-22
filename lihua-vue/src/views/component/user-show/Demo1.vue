<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <user-show :avatar-json="userStore.$state.userInfo.avatar" :nickname="userStore.$state.nickname" />
</template>

<script setup lang="ts">
import UserShow from "@/components/user-show/index.vue"
import {useUserStore} from "@/stores/user.ts";
const userStore = useUserStore();
</script>
