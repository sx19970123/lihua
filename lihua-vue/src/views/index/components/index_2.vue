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
        <a-typography-title :level="4">关于狸花猫</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">狸花猫是一款基于</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}"> SpringBoot </a-typography-text>
          <a-typography-text type="secondary">和</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}"> Vue </a-typography-text>
          <a-typography-text type="secondary">的权限管理系统</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test" style="border: none;height: 610px">
        <a-typography-title :level="4">关于狸花猫</a-typography-title>
        <a-typography-title :level="5">
          为什么开发这个系统
        </a-typography-title>
        <a-typography-text>
          <p style="text-indent: 2em">
            最初开发这个系统是为了学习 Spring Security，
            完成学习后发现已经基本实现了一个基于角色的权限管理后台系统。
            之后受“若依”框架启发，萌生了打造类似框架的想法。
            在研究了多个组件库后，基于个人审美最终选择了 Ant Design Vue 4.x。随后开始学习前端的 TypeScript 和 Vue 3。
          </p>
        </a-typography-text>
        <a-typography-title :level="5">
          为什么叫狸花猫
        </a-typography-title>
        <a-typography-text>
          <p style="text-indent: 2em">
            我家养了两只狸花猫，想用它们作为系统的主题。用它们任何一只的名字命名都不太合适，干脆就按它们的品种来命名了。
          </p>
        </a-typography-text>
        <a-typography-title :level="5">
          系统功能
        </a-typography-title>
        <a-typography-text>
          <p style="text-indent: 2em">
            系统包含完整的基于角色控制的 RBAC 权限管理系统，包括菜单管理、角色管理和用户管理。此外，还提供部门和岗位管理，适用于大多数业务场景，用户支持多部门并可指定默认部门，前后端均提供接口获取用户的默认信息。

            系统字典支持普通字典和树形字典，并提供工具类用于获取和翻译字典信息，前端还提供 dict-tag 组件，可以通过字典 value 直接展示字典 label，并自动匹配 tag 样式。

            通知公告集成了 Vditor 富文本解析器，并使用 SSE 实现了消息的实时发送与接收。

            个人中心支持个性化系统主题配置，管理员角色用户可以对系统进行进一步配置，包括默认密码设置、定期修改密码、同账号登录限制、自助注册配置、登录验证码开关、IP 黑名单和灰色模式。

            此外，系统还提供了日志服务、在线用户监控、缓存监控、服务监控以及定时任务等功能。
          </p>
        </a-typography-text>
      </a-card>
    </template>
  </card-show>
</template>
<script setup lang="ts">
import CardShow from "@/components/card-show/index.vue";
import {getCurrentInstance, ref} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
const componentName = getCurrentInstance()?.type.__name
const middleComplete = ref<boolean>(false)
const themeStore = useThemeStore();
const handleClick = (key:string,show:boolean) => {
  middleComplete.value = true
}
</script>
<style scoped>
.card-background {
  background-image: url("../static/狸花猫-小喵子-酷.svg");
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
