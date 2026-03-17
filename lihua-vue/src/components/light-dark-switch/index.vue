<template>
  <div style="padding-right: var(--lihua-space-base)">
    <a-config-provider :wave="{ disabled: true }">
      <a-switch v-model:checked="themeStore.isDarkTheme" @change="themeStore.changeDataDark()" :style="{'background': themeStore.isDarkTheme ? '#00008B' : '#2196F3'}">
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
import {useThemeStore} from "@/stores/theme";

const themeStore = useThemeStore()
</script>
