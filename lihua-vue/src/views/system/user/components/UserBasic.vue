<template>
  <div style="margin: 8px">
    <a-typography-title :level="4">{{route.meta}}</a-typography-title>
  </div>
</template>

<script setup lang="ts">
import {useRoute} from "vue-router";
const route = useRoute()

</script>

<style scoped>


</style>