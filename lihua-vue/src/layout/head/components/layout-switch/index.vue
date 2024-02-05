<template>
  <a-button @click="handleShowLayoutType">
    <ToolOutlined />
  </a-button>
  <a-drawer v-model:open="showLayoutType">
    <layout-type-switch/>
  </a-drawer>
</template>
<script setup lang="ts">
import {ref} from "vue";
import LayoutTypeSwitch from "@/components/layout-type-switch/index.vue"

const showLayoutType = ref<boolean>(false)
const handleShowLayoutType = () => {
  showLayoutType.value = true
}
</script>

<style scoped>

</style>
