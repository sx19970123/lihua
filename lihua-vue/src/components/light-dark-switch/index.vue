<template>
  <div style="padding-right: 16px;">
    <a-config-provider :wave="{ disabled: true }">
      <a-switch v-model:checked="themeStore.isDarkTheme"
                @change="handleChangeTheme"
                :style="{'background': themeStore.isDarkTheme ? '#00008B' : '#2196F3'}">
        <template #checkedChildren>
          <XiaoHeiMoon/>
        </template>
        <template #unCheckedChildren>
          <XiaoMiaoSun/>
        </template>
      </a-switch>
    </a-config-provider>
  </div>
</template>
<script setup lang="ts">
import { useThemeStore } from "@/stores/theme";
import XiaoHeiMoon from "@/components/icon/xiaoheizi/XiaoHeiMoon.vue";
import XiaoMiaoSun from "@/components/icon/xiaomiaozi/XiaoMiaoSun.vue";
const themeStore = useThemeStore()
// 定义 startViewTransition 函数返回对象 ready 为 Promise
type TransitionFunction = {
  ready: Promise<any>
}

// 由于 startViewTransition 较新，Document下无法识别到 startViewTransition，所以进行自定义 interface
interface ViewTransitionDocument extends Document {
  startViewTransition: (callback: () => void) => TransitionFunction
}

// 切换主题
const handleChangeTheme = (checked: boolean, event: PointerEvent) => {
  // 调用Document下的startViewTransition API，执行切换主题操作，通过设置返回值进行动画配置
  let transition
  try {
    transition = (document as ViewTransitionDocument).startViewTransition(() => {
      themeStore.changeDataDark()
    })
  } catch (e) {
    console.warn("当前浏览器不兼容 startViewTransition 动画")
  }
  // 判断 transition 是否存在，不存在表示浏览器不兼容该api，直接修改主题
  if (!transition) {
    themeStore.changeDataDark()
  } else {
    // 获取半径
    const {clientX, clientY} = event
    const radius = Math.hypot(
        Math.max(clientX, innerWidth - clientX),
        Math.max(clientY, innerHeight - clientY)
    );
    // 根据半径，圆心播放播放动画
    transition.ready.then(() => {
      const clipPath = [
        `circle(0px at ${clientX}px ${clientY}px)`,
        `circle(${radius}px at ${clientX}px ${clientY}px)`,
      ];
      // 处理动画
      document.documentElement.animate(
          {
            clipPath: checked ? [...clipPath].reverse() : clipPath,
          },
          {
            duration: 380,
            easing: "ease-in",
            pseudoElement: checked
                ? "::view-transition-old(root)"
                : "::view-transition-new(root)",
          }
      );
    });
  }
}
</script>
