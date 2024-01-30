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
        <a-menu-item class="more-tab-item" key="star">
          <StarFilled />
          收藏夹栏
        </a-menu-item>
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
    case "close-all": {
      viewTabsStore.closeAll()
      if (viewTabsStore.viewTabs.length > 0) {
        emits('routeSkip',viewTabsStore.viewTabs[0])
      }
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
.ant-tabs-nav-more:hover {
  margin-left: 2px !important;
  background: #f7f7f7 !important;
  cursor: pointer;
  border-radius: 6px
}
.more-tab-item {
  width: 100px
}
</style>
