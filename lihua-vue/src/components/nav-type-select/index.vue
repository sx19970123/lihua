<template>
  <a-flex :gap="16">
    <a-tooltip title="侧边导航-风格1">
      <div class="nav-select-content" @click="handleClockNavType('sider-header')">
        <div class="nav-select-sub-top"/>
        <div class="nav-select-menu-left"/>
        <CheckOutlined class="nav-select-check-icon" :style="{color: themeStore.colorPrimary}" v-if="props.modelValue === 'sider-header'"/>
      </div>
    </a-tooltip>

    <a-tooltip title="侧边导航-风格2">
      <div class="nav-select-content" @click="handleClockNavType('header-sider')">
        <div class="nav-select-top"/>
        <div class="nav-select-menu-sub-left"/>
        <CheckOutlined class="nav-select-check-icon" :style="{color: themeStore.colorPrimary}" v-if="props.modelValue === 'header-sider'"/>
      </div>
    </a-tooltip>

    <a-tooltip title="顶部导航">
      <div class="nav-select-content" @click="handleClockNavType('header-content')">
        <div class="nav-select-menu-top"/>
        <CheckOutlined class="nav-select-check-icon" :style="{color: themeStore.colorPrimary}" v-if="props.modelValue === 'header-content'"/>
      </div>
    </a-tooltip>
  </a-flex>
</template>

<script setup lang="ts">
import {useThemeStore} from "@/stores/theme.ts";

const props = defineProps<{
  modelValue: string
}>()
const emits = defineEmits(['update:modelValue','click','change'])

const themeStore = useThemeStore()

const handleClockNavType = (key: string) => {
  emits('update:modelValue',key)
  emits('click',key)
  emits('change',key)
}
</script>

<style>
.nav-select-check-icon {
  position: absolute;
  bottom: 6px;
  right: 6px;
  font-weight: 700;
  font-size: 14px;
}

[data-theme = light] {
  .nav-select-content {
    position: relative; /* 设置为相对定位 */
    width: 53px;
    height: 43px;
    background: #f3f3f3;
    border-radius: 8px;
    cursor: pointer;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
  }

  .nav-select-sub-top {
    width: 70%;
    height: 30%;
    background: #ffffff;
    float: right;
    border-top-right-radius: 6px;
  }

  .nav-select-menu-left {
    width: 30%;
    height: 100%;
    background: #021524;
    border-top-left-radius: 6px;
    border-bottom-left-radius: 6px;
  }

  .nav-select-menu-sub-left {
    width: 30%;
    height: 70%;
    background: #021524;
    border-bottom-left-radius: 6px;
  }

  .nav-select-top {
    width: 100%;
    height: 30%;
    background: #ffffff;
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
  }

  .nav-select-menu-top {
    width: 100%;
    height: 30%;
    background: #021524;
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
  }
}

[data-theme = dark] {
  .nav-select-content {
    position: relative; /* 设置为相对定位 */
    width: 53px;
    height: 43px;
    background: #000000;
    border-radius: 8px;
    cursor: pointer;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
  }

  .nav-select-sub-top {
    width: 70%;
    height: 30%;
    background: #141414;
    float: right;
    border-top-right-radius: 6px;
  }

  .nav-select-menu-left {
    width: 30%;
    height: 100%;
    background: #212121;
    border-top-left-radius: 6px;
    border-bottom-left-radius: 6px;
  }

  .nav-select-menu-sub-left {
    width: 30%;
    height: 70%;
    background: #212121;
    border-bottom-left-radius: 6px;
  }

  .nav-select-top {
    width: 100%;
    height: 30%;
    background: #141414;
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
  }

  .nav-select-menu-top {
    width: 100%;
    height: 30%;
    background: #212121;
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
  }
}
</style>
