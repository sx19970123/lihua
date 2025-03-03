<template>
    <a-tabs :activeKey="activeKey"
            style="padding: 8px 0 0 8px;"
            type="editable-card"
            size="small"
            hide-add
            @edit="closeTab"
            @change="handleSwitchTab"
    >
      <a-tab-pane v-for="(tab,index) in viewTabs" :key="tab.routerPathKey" :closable="!tab.affix" style="padding: 0">
        <!--每个tab的下拉菜单-->
        <template #tab>
          <tab-pane-menu :tab="tab"
                         :index="index"
                         @route-skip="routeSkip"
                         @cancel-keep-alive="cancelKeepAliveCache"
                         @close-view-tab="closeTab"
                         @mousedown="(event: MouseEvent) => event.button === 1 && !tab.affix ? closeTab(tab.routerPathKey) : ''"
          />
        </template>
      </a-tab-pane>
      <!--view-tabs 右侧下拉菜单-->
      <template #rightExtra>
        <a-space :size="0">
          <tab-right-menu v-rollDisable="true" @route-skip="routeSkip" @cancel-keep-alive="cancelKeepAliveCache"/>
        </a-space>
      </template>
    </a-tabs>
</template>

<script lang="ts" setup>
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import {computed, watch} from "vue";
import { useRoute,useRouter } from "vue-router";
import {useViewTabsStore} from "@/stores/viewTabs";
import type {StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
const viewTabsStore = useViewTabsStore()
const route = useRoute()
const router = useRouter()
/**
 * 初始化数据及变量
 */
const init = () => {
  viewTabsStore.init(route)
  // 初始化数据
  const viewTabs = computed(() => viewTabsStore.viewTabs)
  // 选中tab页
  const activeKey = computed(() => viewTabsStore.activeKey)
  return {
    viewTabs,
    activeKey
  }
}
const {viewTabs, activeKey} = init()
/**
 * 监听路由变化进行切换 tab
 */
watch(() => route.path,(value) => {
  // 切换tab
  viewTabsStore.selectedViewTab(value,
      route?.meta?.viewTab as boolean,
      Object.keys(route.query).length !== 0 ? JSON.stringify(route.query) : undefined)
  // 添加keepalive缓存
  addKeepAliveCache()
})

/**
 * 通过key获取tab元素进行路由跳转
 * @param key
 */
const handleSwitchTab = (key: string) => {
  routeSkip(viewTabsStore.getViewTabsByKey(key))
}

/**
 * 删除标签，根据情况进行路由切换
 * @param key
 */
const closeTab = (key: string) => {
  if (key === activeKey.value) {
    const index = viewTabsStore.getIndex(key)
    // 删除的第一个元素，跳转到下一个
    let tab
    if (index === 0) {
      tab = viewTabsStore.getTabByIndex(index + 1)
    }
    // 删除的不是第一个元素，跳转到前一个
    else {
      tab = viewTabsStore.getTabByIndex(index - 1)
    }
    // 返回元素不为空则跳转路由
    if (tab) {
      routeSkip(tab)
    }
  }
  // 关闭标签
  viewTabsStore.closeViewTab(key)
  // 卸载组件
  cancelKeepAliveCache([key])
};

/**
 * 添加keep-alive 缓存（当前路由）
 */
const addKeepAliveCache = () => {
  if (route?.meta?.cache && route?.name) {
    viewTabsStore.setComponentsKeepAlive(route?.name as string)
  }
}

/**
 * 取消keep-alive 缓存
 * @param keys
 */
const cancelKeepAliveCache = (keys: Array<string>) => {
  const closeTabRoutes = router.getRoutes().filter(route => keys.includes(route.path))
  closeTabRoutes?.forEach(closeTabRoute => {
    if (closeTabRoute?.meta?.cache && closeTabRoute?.name) {
      viewTabsStore.removeComponentsKeepAlive(closeTabRoute?.name as string)
    }
  })
}

/**
 * 路由跳转
 * @param tab
 */
const routeSkip = (tab: StarViewType) => {
  const { routerPathKey , query } = tab
  if (query) {
    router.push({
      path: routerPathKey as string,
      query: JSON.parse(query)
    })
  } else {
    router.push(routerPathKey as string)
  }
}
</script>
<style>
.ant-tabs-nav {
  margin-bottom: 8px !important;
}
</style>
