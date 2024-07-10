<template>
  <div id="vditor"/>
</template>

<script setup lang="ts">
import Vditor from "vditor";
import 'vditor/dist/index.css';
import {onMounted, ref} from "vue";

const vditor = ref()

onMounted(() => {
  vditor.value =  new Vditor('vditor', {
    height: '50vh',
    width: '100%',
    theme: 'classic',
    value: 'hello world',
    debugger: true,
    counter: {
      enable: true,
      type: 'text'
    },
    comment: {
      enable: true,
    }
  })
})

</script>

<style scoped>

</style>