<template>
  <i-frame :src="src" is-inner/>
</template>

<script setup lang="ts">
import IFrame from '@/components/iframe/index.vue'
const src = import.meta.env.VITE_APP_BASE_API + '/druid/login.html'
</script>
