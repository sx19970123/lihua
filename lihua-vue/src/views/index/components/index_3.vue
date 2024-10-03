<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%"
             :auto-complete="false"
             :is-complete="middleComplete"
             @card-click="handleClick"
             :hover-scale="1.03"
             :expanded-width="600"
             :middle-style="{'background':themeStore.$state.isDarkTheme ? '#141414': '#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card style="border: none;" :body-style="{height: '100px'}" class="card-background">
        <a-typography-title :level="4">后端框架</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">当前SpringBoot版本为</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}"> 3.3.2</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test" style="border: none;height: 600px">
        <a-typography-title :level="5">
          后端技术
        </a-typography-title>
        <a-typography-text>
          <p style="text-indent: 2em">
            后端采用了 Spring Boot 3.x 框架进行开发，Java 版本为 21，
            使用 Spring Security 进行权限管理。
            持久层框架采用 MyBatis-Plus，理论上支持 MP 所支持的所有数据库类型，当前使用的是 MySQL 数据库。
            更多依赖信息请查看首页-后端框架。
            <br/>
            <span >
              <p style="text-indent: 2em">
                              下面是系统基础目录结构
              </p>
            </span>
          </p>
        </a-typography-text>


        <a-typography-text>

          <pre>
lihua
├── lihua-admin    // 后台服务启动类&系统模块controller层
├── lihua-captcha  // 验证码
├── lihua-common   // 系统通用模块，包括自定义注解和工具类等
├── lihua-core     // 系统核心模块，包含各种配置及过滤器、拦截器、AOP
├── lihua-service  // 系统服务模块，系统功能业务实现</pre>
        </a-typography-text>
      </a-card>
    </template>
  </card-show>
</template>
<script setup lang="ts">
import CardShow from "@/components/card-show/index.vue";
import {getCurrentInstance, ref} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
const themeStore = useThemeStore();
const componentName = getCurrentInstance()?.type.__name
const middleComplete = ref<boolean>(false)

const handleClick = (key:string,show:boolean) => {
  middleComplete.value = true
}
</script>
<style scoped>
.card-background {
  background-image: url("../static/spring-boot.png");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>