<template>
  <a-card>
    <a-flex vertical :gap="16">
      <demo1/>
      <demo2/>
      <demo3/>
    </a-flex>
  </a-card>
</template>

<script setup lang="ts">
import Demo1 from './Demo1.vue'
import Demo2 from './Demo2.vue'
import Demo3 from './Demo3.vue'
</script>