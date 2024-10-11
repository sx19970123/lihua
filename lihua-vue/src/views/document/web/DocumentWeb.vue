<template>
  <a-card>
    <a-row>
      <a-col :span="4">
        <a-menu mode="inline">
          <a-menu-item> 入门</a-menu-item>
          <a-sub-menu title="内置组件 components">
            <a-menu-item-group title="展示组件">
              <a-menu-item> 卡片展示 card-show</a-menu-item>
              <a-menu-item> 图标组件 icon</a-menu-item>
              <a-menu-item> 用户头像 user-avatar</a-menu-item>
              <a-menu-item> 用户显示 user-show</a-menu-item>
            </a-menu-item-group>
            <a-menu-item-group title="输入组件">
              <a-menu-item> 卡片选择 card-select</a-menu-item>
              <a-menu-item> 颜色选择 color-select</a-menu-item>
              <a-menu-item> 部门选择 dept-select</a-menu-item>
              <a-menu-item> 图标选择 icon-select</a-menu-item>
              <a-menu-item> 密码输入 password-input</a-menu-item>
              <a-menu-item> 用户选择 user-select</a-menu-item>
              <a-menu-item> 富文本编辑器 editor</a-menu-item>
            </a-menu-item-group>
          </a-sub-menu>
          <a-sub-menu title="状态管理 pinia">
            <a-menu-item> 系统用户 user</a-menu-item>
            <a-menu-item> 多任务栏 viewTabs</a-menu-item>
            <a-menu-item> 主题系统 theme</a-menu-item>
            <a-menu-item> 数据字典 dict</a-menu-item>
            <a-menu-item> 动态路由 permission</a-menu-item>
            <a-menu-item> 系统设置 setting</a-menu-item>
          </a-sub-menu>
          <a-sub-menu title="工具类 utils">
            <a-menu-item> 权限 Auth</a-menu-item>
            <a-menu-item> 浏览器 Browser</a-menu-item>
            <a-menu-item> 浏览器指纹 BrowserId</a-menu-item>
            <a-menu-item> 数据加密 Crypto</a-menu-item>
            <a-menu-item> 数据字典 Dict</a-menu-item>
            <a-menu-item> 文件下载 FileDownload</a-menu-item>
            <a-menu-item> 日期处理 HandleDate</a-menu-item>
            <a-menu-item> 异步请求 Request</a-menu-item>
            <a-menu-item> 实时消息 ServerSentEvents</a-menu-item>
            <a-menu-item> 缓存与令牌 Token</a-menu-item>
            <a-menu-item> 树型结构 Tree</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-col>
    </a-row>
  </a-card>
</template>
<script setup lang="ts">
</script>

<style scoped>
:deep(.ant-menu-inline .ant-menu-sub.ant-menu-inline) {
  border-radius: 8px;
  margin-left: 8px;
  margin-right: 8px;
}
</style>