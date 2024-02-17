<template>
    <a-tabs :activeKey="activeKey"
            type="editable-card"
            size="small"
            hide-add
            @edit="closeTab"
            @change="handleSwitchTab"
    >
      <a-tab-pane v-for="(tab,index) in viewTabs" :key="tab.routerPathKey" :closable="!tab.affix">
        <!--每个tab的下拉菜单-->
        <template #tab>
          <tab-pane-menu :tab="tab" :index="index" @route-skip="routeSkip" @cancel-keep-alive="cancelKeepAliveCache"/>
        </template>
      </a-tab-pane>
      <!-- view-tabs 左侧空白-->
      <template #leftExtra>
        <div style="width: 8px;"></div>
      </template>
      <!--view-tabs 右侧下拉菜单-->
      <template #rightExtra>
        <tab-right-menu @route-skip="routeSkip" @cancel-keep-alive="cancelKeepAliveCache"/>
      </template>
    </a-tabs>
</template>

<script lang="ts" setup>
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import {computed, watch} from "vue";
import { useRoute,useRouter } from "vue-router";
import {useViewTabsStore} from "@/stores/modules/viewTabs";
import type {StarViewType} from "@/api/system/star-view/type/starView";
import {login} from "@/api/system/login/login";
const viewTabsStore = useViewTabsStore()
const route = useRoute()
const router = useRouter()
/**
 * 初始化数据及变量
 */
const init = () => {
  if (route?.meta?.skip) {
    // 当前组件为跳过，默认选中其父组件
    const unSkipList =  route.matched.filter(item => !item?.meta?.skip && item.path !== '/')
    if (unSkipList) {
      // 选中接收view-tabs托管的父组件
      viewTabsStore.selectedViewTab(unSkipList[unSkipList.length - 1].path, true)
    }
  } else {
    // 选中当前
    viewTabsStore.selectedViewTab(route.path,false)
  }

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
  viewTabsStore.selectedViewTab(value,!!route?.meta?.skip)
  // 添加keepalive缓存
  addKeepAliveCache()
})

/**
 * 通过key获取tab元素进行路由跳转
 * @param key
 */
const handleSwitchTab = (key: string) => {
  routeSkip(viewTabsStore.getTotalTabByKey(key))
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
  if (!route?.meta?.noCache && route?.meta?.componentName) {
    viewTabsStore.setComponentsKeepAlive(route?.meta?.componentName as string)
  }
}

/**
 * 取消keep-alive 缓存
 * @param keys
 */
const cancelKeepAliveCache = (keys: Array<string>) => {
  const closeTabRoutes = router.getRoutes().filter(route => keys.includes(route.path))
  closeTabRoutes?.forEach(closeTabRoute => {
    if (!closeTabRoute?.meta?.noCache && closeTabRoute?.meta?.componentName) {
      viewTabsStore.removeComponentsKeepAlive(closeTabRoute?.meta?.componentName as string)
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
      query: {
        ... JSON.parse(query)
      }
    })
  } else {
    router.push(routerPathKey as string)
  }
}
</script>

<style scoped>
</style>
