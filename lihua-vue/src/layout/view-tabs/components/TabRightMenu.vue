<template>
  <a-dropdown>
    <button class="ant-tabs-nav-more">
      <MoreOutlined />
    </button>
    <template #overlay>
      <a-menu @click="handleClickMenuTab">
        <a-menu-item class="more-tab-item" key="recent">
          <FieldTimeOutlined />
          最近使用
        </a-menu-item>
        <a-sub-menu class="more-tab-item" key="star" style="width: 120px">
          <template #title>
            <span>
              <StarOutlined />
            收藏夹栏
            </span>
          </template>
            <a-menu-item>5d menu item</a-menu-item>
            <a-menu-item>6th menu item</a-menu-item>
        </a-sub-menu>
        <a-menu-item class="more-tab-item" key="close-all">
          <CloseOutlined />
          关闭全部
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>
<script setup lang="ts">
import { useViewTabsStore } from "@/stores/modules/viewTabs";

const viewTabsStore = useViewTabsStore()
const emits = defineEmits(['routeSkip'])
/**
 * 通过父组件调用对应功能
 * @param key
 */
const handleClickMenuTab = ({ key }: { key :string }) => {
  switch (key) {
    case "recent": {
      viewTabsStore.openRecentModal()
      break
    }
    case "star": {
      viewTabsStore.openStarModal()
      break
    }
    case "close-all": {
      viewTabsStore.closeAll()
      if (viewTabsStore.viewTabs.length > 0) {
        emits('routeSkip',viewTabsStore.viewTabs[0])
      }
      break
    }
  }

}
</script>
<style>
.ant-tabs-nav-more {
  margin-left: 2px !important;
  cursor: pointer;
  border-radius: 6px
}
.more-tab-item {
  width: 100px
}
</style>
