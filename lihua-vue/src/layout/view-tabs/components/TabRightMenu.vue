<script setup lang="ts">

</script>

<template>
  <a-dropdown>
    <button class="ant-tabs-nav-more">
      <MoreOutlined />
    </button>
    <template #overlay>
      <a-menu>
        <a-menu-item>
          最近的使用
        </a-menu-item>
        <a-menu-item>
          关闭所有标签
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<style>
.ant-tabs-nav-more {
  margin-left: 2px !important;
  cursor: pointer;
  border-radius: 6px
}
.ant-tabs-nav-more:hover {
  margin-left: 2px !important;
  background: #f7f7f7 !important;
  cursor: pointer;
  border-radius: 6px
}
</style>
