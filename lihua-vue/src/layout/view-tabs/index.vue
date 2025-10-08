<template>
    <a-tabs :activeKey="activeKey"
            class="unselectable"
            style="padding: 8px 0 0 8px;"
            type="editable-card"
            size="small"
            hide-add
            ref="viewTabRef"
            @edit="closeTab"
            @change="handleSwitchTab"
            :key="tabsRenderKey"
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
import {
  type ComponentPublicInstance,
  computed, nextTick,
  onMounted,
  type Ref,
  ref,
  useTemplateRef,
  watch
} from "vue";
import { useRoute, useRouter } from "vue-router";
import {useViewTabsStore} from "@/stores/viewTabs";
import type {StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
import {type DraggableEvent, useDraggable} from 'vue-draggable-plus';
import {isEqual} from "lodash-es"
import { notification, Button } from 'ant-design-vue';
import { h } from 'vue';

const viewTabRef = useTemplateRef<ComponentPublicInstance>('viewTabRef')
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

/**
 * 初始化拖拽排序
 */
const initDrag = () => {
  // 排序后触发重新渲染 a-tabs
  const tabsRenderKey = ref(0)
  // 开始排序
  const startDrag = () => {
    const navList = viewTabRef.value?.$el.querySelector('.ant-tabs-nav-list') as HTMLElement | null
    const option = ref({
      animation: 200,
      fallbackClass: 'fallback',
      ghostClass: 'ghost',
      // 排序结束后修改元素位置
      onEnd: (event: DraggableEvent & {oldIndex: number, newIndex: number}) => {
        viewTabsStore.move(event.oldIndex, event.newIndex)
        tabsRenderKey.value++
        // 重新渲染a-tabs后再次加载排序
        nextTick(() => startDrag())
      },
    }) as Ref

    if (navList) {
      const { start } = useDraggable(navList, option)
      start()
    }
  }

  return {
    tabsRenderKey,
    startDrag
  }
}

const {tabsRenderKey, startDrag} = initDrag()


const initViewTabsCache = () => {
  // 缓存key
  const catchKey = "cacheViewTabs"

  // 设置缓存
  const setCache = () => {
    localStorage.setItem(catchKey, JSON.stringify(viewTabsStore.viewTabs.map(tab => tab.routerPathKey)))
  }

  // 执行检查
  const checkCache = () => {
    // 获取缓存中的标签页key集合
    const cacheRouterPathKeyJson = localStorage.getItem(catchKey)
    if (cacheRouterPathKeyJson) {
      const cacheRouterPathKeyList = JSON.parse(cacheRouterPathKeyJson)
      // 获取当前标签页key集合
      const routerPathKeyList = viewTabsStore.viewTabs.map(tab => tab.routerPathKey)
      // 比较不同则提示用户是否恢复
      if (!isEqual(routerPathKeyList, cacheRouterPathKeyList)) {
        notification.open({
          message: `检测到多任务栏发生变化，是否恢复？`,
          placement: 'topRight',
          key: catchKey,
          btn: () => h(Button, { type: 'primary', onClick: () => {
            viewTabsStore.resetViewTabsByPathKeys(cacheRouterPathKeyList)
            notification.close(catchKey)
          }}, { default: () => '恢 复' }),
          onClose: () => setCache()
        });
      }
    }
  }

  return {
    setCache, checkCache
  }

}

const {setCache, checkCache} = initViewTabsCache()

onMounted(() => {
  // 加载拖拽
  startDrag()
  // 检查缓存的标签页
  checkCache()
})

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
 * 监听viewTabs变化
 */
watch(() => viewTabsStore.viewTabs, () => {
  setCache()
}, {deep: true})

</script>
<style>
.ant-tabs-nav {
  margin-bottom: 8px !important;
}
.fallback {
  display: none !important;
}
.ghost {
  transform: scale(0.95);
}
</style>
