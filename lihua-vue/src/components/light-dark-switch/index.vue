<template>
  <div style="padding-right: 16px;">
    <a-switch v-model:checked="themeStore.dataTheme"
              @change="handleChangeTheme"
              :style="{'background': themeStore.dataTheme ? '#00008B' : '#2196F3'}">
      <template #checkedChildren>
        <Moon/>
      </template>
      <template #unCheckedChildren>
        <Sun/>
      </template>
    </a-switch>
  </div>
</template>
<script setup lang="ts">
import { useThemeStore } from "@/stores/modules/theme";
import Sun from "@/components/icon/sun/Sun.vue";
import Moon from "@/components/icon/moon/Moon.vue";
import {watch} from "vue";
const themeStore = useThemeStore()

interface Doc extends Document{
  startViewTransition: (arg: unknown) => Transition
}

type Transition = {
  ready: Promise<void>
}


const handleChangeTheme = (checked: boolean, event: PointerEvent) => {
    const transition = (document as Doc).startViewTransition(() => {
      themeStore.changeDataDark()
    })
    const {clientX, clientY} = event
    const endRadius = Math.hypot(
        Math.max(clientX, innerWidth - clientX),
        Math.max(clientY, innerHeight - clientY)
    );

  transition.ready.then(() => {
      const clipPath = [
        `circle(0px at ${clientX}px ${clientY}px)`,
        `circle(${endRadius}px at ${clientX}px ${clientY}px)`,
      ];

      document.documentElement.animate(
          {
            clipPath: checked ? [...clipPath].reverse() : clipPath,
          },
          {
            duration: 400,
            easing: "ease-in",
            pseudoElement: checked
                ? "::view-transition-old(root)"
                : "::view-transition-new(root)",
          }
      );
    });
}
</script>