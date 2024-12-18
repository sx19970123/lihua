<template>
  <a-typography-title :level="4">无middle插槽</a-typography-title>
  <a-row>
    <a-col :span="6">
      <expandable-card
          style="width: 100%"
          :hover-scale="1.03"
          :expanded-width="600"
          :expanded-height="610"
          :middle-style="{'background':themeStore.$state.isDarkTheme ? '#141414': '#fff','background-size': 'contain','border-radius':' 8px'}"
      >
        <template #overview>
          <a-card :body-style="{height: '100px'}" class="card-background">
            <a-typography-title :level="4" ellipsis>关于狸花猫</a-typography-title>
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
          <a-card class="card-background" id="test">
            <a-typography-title :level="4" ellipsis>关于狸花猫</a-typography-title>
            <a-typography-text ellipsis type="secondary">
              <a-typography-text type="secondary">狸花猫是一款基于</a-typography-text>
              <a-typography-text :style="{color:themeStore.getColorPrimary()}"> SpringBoot </a-typography-text>
              <a-typography-text type="secondary">和</a-typography-text>
              <a-typography-text :style="{color:themeStore.getColorPrimary()}"> Vue </a-typography-text>
              <a-typography-text type="secondary">的权限管理系统</a-typography-text>
            </a-typography-text>
            <div class="scrollbar" style="height: 484px;margin-top: 16px;overflow-x: hidden">
              <a-typography-title :level="5">
                为什么叫狸花猫
              </a-typography-title>
              <a-typography-title :level="5">
                系统功能
              </a-typography-title>
              <a-typography-text>
                <p style="text-indent: 2em">
                  系统包含完整的基于角色控制的 RBAC 权限管理系统，包括菜单管理、角色管理和用户管理。此外，还提供部门和岗位管理，适用于大多数业务场景，用户支持多部门并可指定默认部门，前后端均提供接口获取用户的默认信息。
                </p>
              </a-typography-text>
              <a-typography-title :level="5">
                字典管理
              </a-typography-title>
              <a-typography-text>
                <p style="text-indent: 2em">
                  系统字典支持普通字典和树形字典，并提供工具类用于获取和翻译字典信息，前端还提供 dict-tag 组件，可以通过字典 value 直接展示字典 label，并自动匹配 tag 样式。
                </p>
              </a-typography-text>
              <a-typography-title :level="5">
                通知公告
              </a-typography-title>
              <a-typography-text>
                <p style="text-indent: 2em">
                  通知公告集成了 Vditor 富文本解析器，并使用 SSE 实现了消息的实时发送与接收。
                </p>
              </a-typography-text>
              <a-typography-title :level="5">
                个人中心
              </a-typography-title>
              <a-typography-text>
                <p style="text-indent: 2em">
                  个人中心支持个性化系统主题配置，支持主题、布局、导航等页面设置。
                </p>
              </a-typography-text>
              <a-typography-title :level="5">
                系统设置
              </a-typography-title>
              <a-typography-text>
                <p style="text-indent: 2em">
                  管理员角色用户可以对系统进行进一步配置，包括默认密码设置、定期修改密码、同账号登录限制、自助注册配置、验证码开关、IP 黑名单和灰色模式。
                </p>
              </a-typography-text>
              <a-typography-title :level="5">
                其他功能
              </a-typography-title>
              <a-typography-text>
                <p style="text-indent: 2em">
                  系统还提供了日志服务、在线用户监控、缓存监控、服务监控以及定时任务等功能。
                </p>
              </a-typography-text>
            </div>
          </a-card>
        </template>
      </expandable-card>
    </a-col>
  </a-row>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
import {useThemeStore} from "@/stores/theme.ts";
const themeStore = useThemeStore();
</script>
<style scoped>
.card-background {
  background-position-y: 10px;
  background-position-x: calc(100% - 10px);
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>
