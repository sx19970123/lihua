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
[layout-type=header-content][view-tabs=show] .lihua-iframe {
  min-height: calc(100vh - (64px + 60px + 16px + 16px) - 4px);
}
[layout-type=header-content][view-tabs=hide] .lihua-iframe {
  min-height: calc(100vh - (64px + 16px + 16px) - 4px);
}
[layout-type=header-sider][view-tabs=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 60px + 16px + 16px) - 4px);
}
[layout-type=header-sider][view-tabs=hide] .lihua-iframe {
  min-height: calc(100vh - (48px + 16px + 16px) - 4px);
}
[layout-type=sider-header][view-tabs=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 60px + 16px + 16px) - 4px);
}
[layout-type=sider-header][view-tabs=hide] .lihua-iframe {
  min-height: calc(100vh - (48px + 16px + 16px) - 4px);
}
</style>
