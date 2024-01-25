<template>
  <a-dropdown :trigger="['contextmenu']">
    <div>
      <component :is="item.data.icon"/>
      {{ item.data.label }}
    </div>

    <template #overlay>
      <a-menu>
        <a-menu-item key="1">
          <RedoOutlined />
          刷新页面
        </a-menu-item>
        <a-menu-item key="2">
          <VerticalLeftOutlined />
          关闭右侧
        </a-menu-item>
        <a-menu-item key="3">
          <VerticalRightOutlined />
          关闭左侧
        </a-menu-item>
        <a-menu-item key="4">
          <CloseCircleOutlined />
          关闭其他
        </a-menu-item>
        <a-menu-item key="5">
          <CloseOutlined />
          全部关闭
        </a-menu-item>
        <a-menu-item key="6">
          <StarOutlined />
          添加收藏
        </a-menu-item>
        <a-menu-item key="7">
          <StarFilled />
          取消收藏
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
const item = defineProps({
  data: null
})
console.log(item.data)
</script>
<style scoped>
  .router-link-active {
    text-decoration: none;
    color: #1677ff
  }
  a {
    text-decoration: none;
    color: rgba(0, 0, 0, 0.88)
  }
</style>
