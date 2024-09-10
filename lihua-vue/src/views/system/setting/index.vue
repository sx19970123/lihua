<template>
  <div class="header-content-center">
    <a-card>
      <a-row>
        <a-col :span="4">
          <a-menu  style="height: 100%">
            <a-menu-item key="Individuation"> 自助注册</a-menu-item>
            <a-menu-item key="Security"> 登录验证码</a-menu-item>
            <a-menu-item key="Security"> 默认密码</a-menu-item>
            <a-menu-item key="Security"> 定期修改密码</a-menu-item>
            <a-menu-item key="Basic"> 灰色模式</a-menu-item>
          </a-menu>
        </a-col>
        <a-col :span="20">
          <transition :name="themeStore.routeTransition" mode="out-in">
            <component style="margin: 20px"/>
          </transition>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {useThemeStore} from "@/stores/modules/theme.ts";

const themeStore = useThemeStore()
</script>


<style scoped>

</style>
