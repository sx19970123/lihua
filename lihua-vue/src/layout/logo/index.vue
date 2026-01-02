<template>
  <div class="logo unselectable" @click="goHome">
    <a-flex gap="middle" align="center" justify="center" v-if="showTitle && (themeStore.layoutType === 'mix-navigation' || themeStore.layoutType === 'top-navigation' || !permissionStore.collapsed)">
      <div>
        <!--    导航LOGO-->
        <a-avatar :style="{backgroundColor: themeStore.getColorPrimary()}">
          <template #icon>
            <XiaoMiaoCool/>
          </template>
        </a-avatar>
      </div>
      <!--    导航名称-->
      <a-typography-title class="title"
                          :class="(themeStore.siderTheme === 'dark' && themeStore.layoutType !== 'mix-navigation') || (themeStore.siderTheme === 'dark' && themeStore.mixSplitMenu) || (themeStore.siderTheme === 'dark' && isSmallWindow) ? 'main-name': ''"
                          :level="4"
                          ellipsis
                          content="Lihua Admin"
      />
    </a-flex>
    <a-flex align="center" justify="center" v-else>
      <div>
        <!--    侧边导航折叠时展示的LOGO-->
        <a-avatar :style="{ backgroundColor: themeStore.getColorPrimary()}">
          <template #icon>
            <XiaoMiaoCool />
          </template>
        </a-avatar>
      </div>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import {useRouter} from 'vue-router'
import {ref} from "vue";

const router = useRouter()
const permissionStore = usePermissionStore()
const themeStore = useThemeStore()
const {showTitle = true} = defineProps<{
  showTitle?: boolean;
}>()
const isSmallWindow = ref<boolean>(themeStore.isSmallWindow)
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
.main-name {
  color: rgba(255, 255, 255, 0.85);
}
</style>
