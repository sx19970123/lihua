<template>
    <a-tabs :activeKey="activeKey"
            class="unselectable tab-none-padding enable-glass"
            style="padding: var(--lihua-space-sm) 0 0 var(--lihua-space-sm);"
            type="card"
            size="small"
            hide-add
            ref="viewTabRef"
            @edit="closeTab"
            @change="routeSkip"
    >
      <a-tab-pane v-for="(tab,index) in viewTabs" :key="tab.routerPathKey" class="enable-glass">
        <!--每个tab的下拉菜单-->
        <template #tab>
          <tab-pane-menu :tab="tab"
                         :index="index"
                         @route-skip="routeSkip"
                         @cancel-keep-alive="cancelKeepAliveCache"
                         @close-view-tab="closeTab"
                         @mousedown="(event: MouseEvent) => event.button === 1 && !tab.affix && closeTab(tab.routerPathKey)"
          />
        </template>
      </a-tab-pane>
      <!--view-tabs 右侧下拉菜单-->
      <template #rightExtra>
        <a-space :size="0">
          <tab-right-menu v-rollDisable="true" ref="tabRightMenuRef" @route-skip="routeSkip" @cancel-keep-alive="cancelKeepAliveCache"/>
        </a-space>
      </template>
    </a-tabs>
</template>

<script lang="ts" setup>
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import {type ComponentPublicInstance, computed, onMounted, type Ref, ref, useTemplateRef, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useViewTabsStore} from "@/stores/viewTabs";
import {type DraggableEvent, useDraggable} from 'vue-draggable-plus';
import {isMobile} from 'is-mobile'

const viewTabRef = useTemplateRef<ComponentPublicInstance>('viewTabRef')
const tabRightMenuRef = useTemplateRef<typeof TabRightMenu>('tabRightMenuRef')
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
      routeSkip(tab.routerPathKey, tab.query)
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
 */
const routeSkip = (path: string, query?: string) => {
  if (query) {
    router.push({path: path, query: JSON.parse(query)})
  } else {
    router.push(path)
  }
}

/**
 * 初始化拖拽排序
 */
const initDrag = () => {
  // 开始排序
  const startDrag = () => {
    // 移动端不加载拖拽
    if (isMobile()) {
      return
    }
    const navList = viewTabRef.value?.$el.querySelector('.ant-tabs-nav-list') as HTMLElement | null
    const option = ref({
      animation: 200,
      fallbackClass: 'fallback',
      ghostClass: 'ghost',
      // 排序结束后修改元素位置
      onEnd: (event: DraggableEvent & {oldIndex: number, newIndex: number}) => {
        // 修改store中viewTabs中的位置
        viewTabsStore.move(event.oldIndex, event.newIndex)
        // 子组件刷新缓存
        if (tabRightMenuRef.value) {
          tabRightMenuRef.value.setCache()
        }
      },
    }) as Ref

    if (navList) {
      const { start } = useDraggable(navList, option)
      start()
    }
  }

  return {
    startDrag
  }
}

const {startDrag} = initDrag()

onMounted(() => {
  // 加载拖拽
  startDrag()
  // 调用子组件方法
  if (tabRightMenuRef.value) {
    tabRightMenuRef.value.checkCache()
  }
})

/**
 * 监听路由变化进行切换 tab
 */
watch(() => route.path,() => {
  // 切换tab
  viewTabsStore.init(route)
  // 添加keepalive缓存
  addKeepAliveCache()
  // 子组件刷新缓存
  if (tabRightMenuRef.value) {
    tabRightMenuRef.value.setCache()
  }
})

</script>
<style>
.ant-tabs-nav {
  margin-bottom: var(--lihua-space-sm) !important;
}
.fallback {
  display: none !important;
}
.ghost {
  color: var(--colorPrimary);
}
.ghost:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
  opacity: 0.2;
  border-radius: 5px 5px 0 0;
  background-color: var(--colorPrimary);
}

.tab-none-padding {
  .ant-tabs-tab {
    padding: 0 !important;
  }

}
</style>
