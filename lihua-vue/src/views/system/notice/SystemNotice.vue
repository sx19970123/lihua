<template>
  <div>
    <div>
      <user-select/>
      <user-select/>
      <user-select/>
      <editor v-model="vModel"/>
    </div>
  </div>
</template>
<script setup lang="ts">
import Editor from "@/components/editor/index.vue"
import UserSelect from "@/components/user-show/index.vue"
import {ref} from "vue";
const vModel = ref<string>()


</script>
<style scoped>

</style>