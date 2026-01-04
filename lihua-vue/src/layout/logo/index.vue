<template>
  <div class="logo unselectable" @click="goHome">
    <a-flex gap="middle" align="center" justify="center" v-if="showTitle && (themeStore.layoutType === 'mix-navigation' || themeStore.layoutType === 'top-navigation' || !permissionStore.collapsed)">
      <a-avatar :style="{backgroundColor: themeStore.getColorPrimary()}">
        <template #icon>
          <XiaoMiaoCool/>
        </template>
      </a-avatar>
      <!--    导航名称-->
      <a-typography-title class="title" :level="4" ellipsis content="Lihua Admin"/>
    </a-flex>
    <a-flex align="center" justify="center" v-else>
      <!--    侧边导航折叠时展示的LOGO-->
      <a-avatar :style="{ backgroundColor: themeStore.getColorPrimary()}">
        <template #icon>
          <XiaoMiaoCool />
        </template>
      </a-avatar>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import {useRouter} from 'vue-router'

const router = useRouter()
const permissionStore = usePermissionStore()
const themeStore = useThemeStore()
const {showTitle = true} = defineProps<{
  showTitle?: boolean;
}>()
// 点击回到首页
const goHome = async () => {
  await router.push("/index");
}
</script>

<style scoped>
.logo {
  cursor: pointer;
  .title {
    margin: 0;
    overflow: hidden;
  }
}
</style>
