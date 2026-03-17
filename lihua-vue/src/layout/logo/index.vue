<template>
  <div class="title-content unselectable" @click="goHome" :style="{maxWidth: maxWidth + 'px'}">
    <a-flex gap="middle" align="center" justify="center">
      <!--      系统logo-->
      <a-avatar class="logo" :style="{backgroundColor: themeStore.getColorPrimary()}">
        <template #icon>
          <XiaoMiaoCool/>
        </template>
      </a-avatar>
      <!--    系统名称-->
      <a-typography-title content="Lihua Admin" class="title" :class="{'title-color': darkSiderColor}" :level="4" ellipsis v-if="showTitle"/>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import {useThemeStore} from "@/stores/theme";
import {useRouter} from 'vue-router'
import {computed} from "vue";

const router = useRouter()
const themeStore = useThemeStore()
const {showTitle = true, maxWidth, autoColor = true} = defineProps<{
  // 显示标题
  showTitle?: boolean,
  // 最大宽度
  maxWidth?: number,
  // 自动匹配深浅菜单颜色
  autoColor?: boolean,
}>()
// 点击回到首页
const goHome = async () => {
  await router.push("/index");
}

// 菜单栏为暗色并主题不为暗色时，使用自定义的标题颜色
const darkSiderColor = computed(() => {
  if (autoColor) {
    return themeStore.siderTheme === 'dark' && !themeStore.isDarkTheme
  }
  return autoColor
})
</script>

<style scoped>
.title-content {
  cursor: pointer;

  .logo {
    min-width: 32px;
  }
  .title {
    margin: 0;
    overflow: hidden;
  }
  .title-color {
    color: var(--lihua-alpha-level-5);
  }
}

</style>
