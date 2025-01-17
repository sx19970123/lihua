<template>
  <div class="logo unselectable" @click="goHome">
    <a-flex gap="middle" align="center" justify="center" v-if="showTitle && (themeStore.layoutType === 'header-sider' || themeStore.layoutType === 'header-content' || !permissionStore.collapsed)">
      <div>
        <!--    导航LOGO-->
        <a-avatar :style="{ backgroundColor: themeStore.getColorPrimary()}">
          <template #icon>
            <XiaoMiaoCool/>
          </template>
        </a-avatar>
      </div>
      <!--    导航名称-->
      <a-typography-title class="title"
                          :class="themeStore.layoutType !== 'header-sider' && themeStore.siderTheme === 'dark' ? 'main-name': ''"
                          :level="4"
                          ellipsis
                          content="Lihua Admin"
      />
    </a-flex>
    <a-flex align="center" justify="center" v-else>
      <div>
        <!--    侧边导航（风格1）折叠时展示的LOGO-->
        <a-avatar :style="{ backgroundColor: themeStore.getColorPrimary()}">
          <template #icon>
            <XiaoMiaoCool/>
          </template>
        </a-avatar>
      </div>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import { usePermissionStore } from "@/stores/permission";
import { useThemeStore } from "@/stores/theme";
import {useRouter} from 'vue-router'
import XiaoMiaoCool from "@/components/icon/xiaomiaozi/XiaoMiaoCool.vue";
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
.main-name {
  color: rgba(255, 255, 255, 0.85);
}
</style>
