<template>
  <a-dropdown :trigger="['contextmenu']">
    <span>
      <component :is="tabPane.tab.icon"/>
      {{ tabPane.tab.label }}
    </span>
    <template #overlay>
      <a-menu @click="handleClickMenuTab" v-rollDisable="true">
        <a-menu-item key="reload" v-if="tabPane.tab.routerPathKey === viewTabsStore.$state.activeKey">
          <RedoOutlined />
          刷新页面
        </a-menu-item>
        <a-menu-item key="newPage">
          <ImportOutlined style="transform: rotate(180deg)"/>
          新页打开
        </a-menu-item>
        <a-menu-item key="miniWindow" v-if="usableMiniWindow">
          <PicRightOutlined />
          小窗打开
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="close-left" :disabled="tabPane.index === 0">
          <VerticalRightOutlined />
          关闭左侧
        </a-menu-item>
        <a-menu-item key="close-right" :disabled="tabPane.index === viewTabsStore.viewTabs.length - 1">
          <VerticalLeftOutlined />
          关闭右侧
        </a-menu-item>
        <a-menu-item key="close-other" :disabled="viewTabsStore.viewTabs.length === 1">
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
import { useViewTabsStore } from "@/stores/viewTabs";
import { viewTab } from "@/api/system/view-tab/ViewTab.ts";
import { message } from "ant-design-vue";
import { StarFilled , StarOutlined ,LockOutlined , UnlockOutlined} from '@ant-design/icons-vue';
import {h} from "vue";
import type { ResponseType } from "@/api/global/Type.ts";
import type { StarViewType } from "@/api/system/view-tab/type/SysViewTab.ts";
const viewTabsStore = useViewTabsStore()
const tabPane = defineProps(['tab','index'])
const emits = defineEmits(['routeSkip','cancelKeepAlive','closeViewTab'])
const usableMiniWindow = window.location.origin.startsWith("http://localhost") || window.location.origin.startsWith("https")

// 处理点击菜单
const handleClickMenuTab = ({ key }:{ key :string }) => {
  const tab = tabPane.tab
  switch (key) {
    case "reload": {
      viewTabsStore.regenerateComponentKey()
      break
    }
    case "newPage": {
      window.open(tab.routerPathKey)
      break
    }
    case "miniWindow": {
      createMiniWindow(tab.routerPathKey, tab.affix)
      break
    }
    case "close-left": {
      handleCloseLeft(tab)
      break
    }
    case "close-right": {
      handleCloseRight(tab)
      break
    }
    case "close-other": {
      handleCloseOther(tab)
      break
    }
    case "star": {
      handleStar(tab)
      break
    }
    case "un-star": {
      handleUnStar(tab)
      break
    }
    case "affix": {
      handleAffix(tab)
      break
    }
    case "un-affix": {
      handleUnAffix(tab)
      break
    }
    default: {
      console.error("错误的菜单类型")
    }
  }
}

// 初始化小窗相关，全局搜索：miniWindow=true 可查看ui小窗判定代码
const initMiniWindow = () => {
  let currentAffix: boolean = false
  let currentUrl: string = ''
  // 创建小窗
  const createMiniWindow = async (url: string, affix: boolean) => {
    currentAffix = affix
    currentUrl = url
    try {
      // 创建小窗
      const miniWindow = await window.documentPictureInPicture.requestWindow({width: 439, height: 661})
      miniWindow.document.body.innerHTML = `<iframe src="${url.includes("?") ? url + "&miniWindow=true" : url + "?miniWindow=true"}" style="min-height: calc(100vh); width: 100%; min-width: 439px; border: 0;margin: 0;padding: 0"/>`
      miniWindow.document.body.style.margin = '0'
      miniWindow.document.body.style.overflowY = 'hidden'
      miniWindow.document.body.style.overflowX = 'auto'
      // 关闭当前窗口
      if (!affix) emits("closeViewTab", url)
      // 处理监听器
      handleListener(miniWindow)
    } catch (err) {
      message.error("当前浏览器不支持小窗模式，请使用Chrome、Edge等现代浏览器升级至最新版本")
      console.error("miniWindow创建失败", err)
    }
  }

  // 监听关闭小窗
  const handleListener = (miniWindow: Window) => {
    miniWindow.removeEventListener("pagehide", handleCloseMiniWindow)
    miniWindow.addEventListener("pagehide", handleCloseMiniWindow)
  }

  // 处理关闭小窗，当活动的viewTab不存在时，将路由添加到viewTab
  const handleCloseMiniWindow = () => {
    if (!currentAffix && viewTabsStore.viewTabs.findIndex(tab => tab.routerPathKey === currentUrl) === -1) {
      const viewTab = viewTabsStore.totalViewTabs.filter(tab => tab.routerPathKey === currentUrl)
      if (viewTab && viewTab.length > 0) {
        viewTabsStore.addViewTab(viewTab[0])
      }
    }
  }
  return {
    createMiniWindow
  }
}

const { createMiniWindow } = initMiniWindow()

// 处理关闭左侧
const handleCloseLeft = (tab: StarViewType) => {
  const actIndex = viewTabsStore.getIndex(viewTabsStore.activeKey)
  const index = viewTabsStore.getIndex(tab.routerPathKey)
  const closeKeys = viewTabsStore.closeLeft(tab.routerPathKey)
  emits('cancelKeepAlive',closeKeys)
  if (actIndex < index) {
    emits('routeSkip',tab)
  }
}

// 处理关闭右侧
const handleCloseRight = (tab: StarViewType) => {
  const actIndex = viewTabsStore.getIndex(viewTabsStore.activeKey)
  const index = viewTabsStore.getIndex(tab.routerPathKey)
  const closeKeys = viewTabsStore.closeRight(tab.routerPathKey)
  emits('cancelKeepAlive',closeKeys)
  if (actIndex > index) {
    emits('routeSkip',tab)
  }
}

// 处理关闭其他
const handleCloseOther = (tab: StarViewType) => {
  const closeKeys = viewTabsStore.closeOther(tab.routerPathKey)
  emits('cancelKeepAlive',closeKeys)
  emits('routeSkip',tab)
}

// 处理star
const handleStar = (tab: StarViewType) => {
  if (!tab.menuId) return

  viewTab(tab.menuId,tab.affix ? '1' : '0','1').then((resp: ResponseType<StarViewType>) => {
    if (resp.code === 200) {
      viewTabsStore.replaceByKey(resp.data)
      message.success({
        content: () => '添加收藏',
        icon: () => h( StarFilled ),
      })
    } else {
      message.error(resp.msg)
    }
  }).catch(() => {
    message.error({
      content: () => '添加收藏失败',
      icon: () => h( StarFilled ),
    })
  })
}

// 处理unstar
const handleUnStar = (tab: StarViewType) => {
  if (!tab.menuId) return

  viewTab(tab.menuId,tab.affix ? '1' : '0','0').then((resp: ResponseType<StarViewType>) => {
    if (resp.code === 200) {
      viewTabsStore.replaceByKey(resp.data)
      message.success({
        content: () => '取消收藏',
        icon: () => h( StarOutlined ),
      })
    } else {
      message.error(resp.msg)
    }
  }).catch(() => {
    message.error({
      content: () => '取消收藏失败',
      icon: () => h( StarOutlined ),
    })
  })
}

// 处理固定
const handleAffix = (tab: StarViewType) => {
  if (!tab.menuId) return

  viewTab(tab.menuId,'1',tab.star ? '1' : '0').then((resp: ResponseType<StarViewType>) => {
    if (resp.code === 200) {
      viewTabsStore.affix(resp.data)
      message.success({
        content: () => '固定页面',
        icon: () => h( LockOutlined ),
      })
    } else {
      message.error(resp.msg)
    }
  }).catch(() => {
    message.error({
      content: () => '固定页面失败',
      icon: () => h( LockOutlined ),
    })
  })
}

// 处理取消固定
const handleUnAffix = (tab: StarViewType) => {
  if (!tab.menuId) return

  viewTab(tab.menuId,'0',tab.star ? '1' : '0').then((resp: ResponseType<StarViewType>) => {
    if (resp.code === 200) {
      viewTabsStore.unAffix(resp.data)
      message.success({
        content: () => '取消固定',
        icon: () => h( UnlockOutlined ),
      })
    } else {
      message.error(resp.msg)
    }
  }).catch(() => {
    message.error({
      content: () => '取消固定失败',
      icon: () => h( UnlockOutlined ),
    })
  })
}
</script>
