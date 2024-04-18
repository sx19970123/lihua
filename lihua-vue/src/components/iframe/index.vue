<template>
  <iframe class="lihua-iframe" :src="src"/>
</template>
<script setup lang="ts">
import {useRoute} from "vue-router";
import {ref} from "vue";
const route = useRoute()
const src = ref<string>()
if (route.meta.link) {
  src.value = route.meta.link as string
}
</script>
<style>
.lihua-iframe {
  width: 100%;
  border-radius: 8px;
  border: none;
}
[view-tabs=show][show-hide-layout=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 56px + 16px));
}
[view-tabs=hide][show-hide-layout=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 16px));
}
[view-tabs=show][show-hide-layout=hide] .lihua-iframe {
  min-height: calc(100vh - (56px + 16px));
}
</style>
