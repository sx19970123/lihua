<template>
  <a-layout class="layout-height">
    <a-layout-sider class="sider-height scrollbar element"
                    theme="light"
                    :trigger="null"
                    v-model:collapsed="permission.collapsed"
                    collapsible
                    breakpoint="lg"

                    style="position: sticky;top: 0px"
    >
      <Logo/>
      <!-- 侧边栏-->
      <Side/>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="layout-header" :style="{'background': bgColor}"  style="position: sticky;top: 0px">
        <!--页头-->
        <Head></Head>
        <ViewTabs :bg-color="bgColor"/>
      </a-layout-header>


      <a-layout-content>
        <!--内容-->
        <Content/>
      </a-layout-content>
      <!--页脚-->
      <a-layout-footer></a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import { usePermissionStore } from "@/stores/modules/permission";
import Logo from "@/layout/sider/components/Logo.vue";
import {ref, watch} from "vue";
import {useTheme} from "@/stores/modules/theme";
const themeStore = useTheme()
const permission = usePermissionStore()
const bgColor = ref<string>('#fff')
watch(() => themeStore.dataDark,(value) => {
  if (value === 'dark') {
    bgColor.value = '#141414'
  } else {
    bgColor.value = '#fff'
  }
})

</script>

<style scoped>
.layout-height {
  min-height: 100vh
}
.layout-header {
  padding-left: 0px;
  padding-right: 0px;
}
.sider-height {
  height: 100vh;
}
</style>

