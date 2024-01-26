<template>
  <a-dropdown :trigger="['contextmenu']">
    <span>
      <component :is="tabPane.tab.icon"/>
      {{ tabPane.tab.label}}
    </span>
    <template #overlay>
      <a-menu @click="send">
        <a-menu-item key="reload" :disabled="tabPane.tab.routerPathKey !== activeKey">
          <RedoOutlined />
          刷新页面
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="close-left" :disabled="tabPane.index === 0">
          <VerticalRightOutlined />
          关闭左侧
        </a-menu-item>
        <a-menu-item key="close-right" :disabled="tabPane.index === length - 1">
          <VerticalLeftOutlined />
          关闭右侧
        </a-menu-item>
        <a-menu-item key="close-other" :disabled="length === 1">
          <CloseCircleOutlined />
          关闭其他
        </a-menu-item>
        <a-menu-divider v-if="!tabPane.tab.static"/>
        <a-menu-item key="star" v-if="!tabPane.tab.star && !tabPane.tab.static">
          <StarOutlined />
          添加收藏
        </a-menu-item>
        <a-menu-item key="un-star" v-if="tabPane.tab.star && !tabPane.tab.static">
          <StarFilled />
          取消收藏
        </a-menu-item>
        <a-menu-item key="affix" v-if="!tabPane.tab.affix && !tabPane.tab.static">
          <LockOutlined />
          固定页面
        </a-menu-item>
        <a-menu-item key="un-affix" v-if="tabPane.tab.affix && !tabPane.tab.static">
          <UnlockOutlined />
          取消固定
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
const emits = defineEmits(['clickMenuTab'])
const tabPane = defineProps(['tab','index','length','activeKey'])

/**
 * 向父类抛出方法
 * @param key
 */
const send = ({ key }: { key :string }) => {
  emits('clickMenuTab', key, tabPane.tab)
}

</script>
