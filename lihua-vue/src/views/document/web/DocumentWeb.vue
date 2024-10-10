<template>
  <a-card>
    <a-row>
      <a-col :span="4">
        <a-menu>
          <a-menu-item-group title="展示组件">
            <a-menu-item> 卡片展示</a-menu-item>
            <a-menu-item> 图标组件</a-menu-item>
            <a-menu-item> 用户头像</a-menu-item>
            <a-menu-item> 用户显示</a-menu-item>
          </a-menu-item-group>
          <a-menu-item-group title="输入组件">
            <a-menu-item> 卡片选择</a-menu-item>
            <a-menu-item> 颜色选择</a-menu-item>
            <a-menu-item> 部门选择</a-menu-item>
            <a-menu-item> 富文本编辑器</a-menu-item>
            <a-menu-item> 图标选择</a-menu-item>
            <a-menu-item> 密码输入</a-menu-item>
            <a-menu-item> 用户选择</a-menu-item>
          </a-menu-item-group>
          <a-menu-item-group title="工具类">
            <a-menu-item> 全屏加载</a-menu-item>
          </a-menu-item-group>

        </a-menu>
      </a-col>
    </a-row>
  </a-card>
</template>
<style scoped>
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 200px);
  text-align: center;
}
</style>
<script setup lang="ts">
</script>