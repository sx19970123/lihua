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
          <tab-pane-menu :tab="tab" :index="index" @route-skip="routeSkip"/>
        </template>
      </a-tab-pane>
      <!-- view-tabs 左侧空白-->
      <template #leftExtra>
        <div style="width: 8px;"></div>
      </template>
      <!--view-tabs 右侧下拉菜单-->
      <template #rightExtra>
        <tab-right-menu @route-skip="routeSkip"/>
      </template>
    </a-tabs>
</template>

<script lang="ts" setup>
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import {computed, watch} from "vue";
import { useRoute,useRouter } from "vue-router";
import {useViewTabsStore} from "@/stores/modules/viewTabs";
import type {StarViewType} from "@/types/starView";
const viewTabsStore = useViewTabsStore()
const route = useRoute()
const router = useRouter()
/**
 * 初始化数据及变量
 */
const init = () => {
  // 默认选中当前
  viewTabsStore.selectedViewTab(route.path)
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
  viewTabsStore.selectedViewTab(value)
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
};

/**
 * 路由跳转
 * @param tab
 */
const routeSkip = (tab: StarViewType) => {
  const { routerPathKey , query } = tab
  if (query) {
    router.push({
      path: routerPathKey,
      query: {
        ... JSON.parse(query)
      }
    })
  } else {
    router.push(routerPathKey)
  }
}
</script>

<style scoped>
</style>
