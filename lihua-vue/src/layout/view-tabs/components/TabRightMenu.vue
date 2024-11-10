<template>
  <div style="padding-right: 8px">
    <button class="ant-tabs-nav-more" @click="showHideLayout">
      <ExpandOutlined v-if="viewTabsStore.showLayout" />
      <CompressOutlined v-else />
    </button>
    <a-dropdown :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
      <button class="ant-tabs-nav-more">
        <MoreOutlined />
      </button>
      <template #overlay>
        <a-menu @click="handleClickMenuTab">
          <a-sub-menu class="menu-item-min-width" key="recent">
            <template #title>
              <FieldTimeOutlined />
              最近使用
            </template>
            <div class="scrollbar" v-if="recentData.length > 0" style="max-height: 400px">
              <a-menu-item v-for="item in recentData" :key="item.path">
                <template #icon>
                  <component :is="item.icon"/>
                </template>
                <a-flex :gap="40" align="space-between" justify="space-between" >
                <span>
                  {{item.label}}
                </span>
                  <span>
                  {{ handleTime(item.openTime) }}
                </span>
                </a-flex>
              </a-menu-item>
            </div>
            <a-divider v-if="recentData.length > 0" style="margin: 0"/>
            <a-menu-item v-if="recentData.length > 0" key="clear-recent" danger>
              <div style="text-align: center">
                <ClearOutlined /> 清空最近使用
              </div>
            </a-menu-item>
            <a-empty v-else>
              <template #description>
                <a-typography-text>暂无数据</a-typography-text>
              </template>
            </a-empty>
          </a-sub-menu>
          <a-sub-menu class="menu-item-min-width" key="star">
            <template #title>
              <StarOutlined />
              收藏夹栏
            </template>
            <div class="scrollbar" style="max-height: 400px" v-if="starData.length > 0">
              <a-menu-item class="menu-item-min-width" v-for="item in starData" :key="item.routerPathKey">
                <template #icon>
                  <component :is="item.icon"/>
                </template>
                {{item.label}}
              </a-menu-item>
            </div>
            <a-empty v-else>
              <template #description>
                <a-typography-text>暂无数据</a-typography-text>
              </template>
            </a-empty>
          </a-sub-menu>
          <a-divider style="margin: 0"/>
          <a-menu-item class="menu-item-min-width" key="close-all" danger>
            <CloseOutlined />
            关闭全部
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </div>


</template>
<script setup lang="ts">
import { useViewTabsStore } from "@/stores/viewTabs";
import {ref, watch} from "vue";
import type {RecentType, StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
import {handleTime} from "@/utils/HandleDate.ts";
const viewTabsStore = useViewTabsStore()
const emits = defineEmits(['routeSkip','cancelKeepAlive'])
/**
 * 处理点击菜单后执行功能
 * @param key
 */
const handleClickMenuTab = ({ key }: { key :string }) => {
  switch (key) {
    // 关闭全部
    case "close-all": {
      const closeKeys = viewTabsStore.closeAll()
      emits('cancelKeepAlive',closeKeys)
      if (viewTabsStore.viewTabs.length > 0) {
        emits('routeSkip',viewTabsStore.viewTabs[0])
      }
      break
    }
    // 清空最近使用
    case "clear-recent": {
      localStorage.removeItem(viewTabsStore.$state.tabCacheKey)
      setTimeout(()=> {
        recentData.value = []
      },200)
      break
    }
    default: {
      emits('routeSkip', viewTabsStore.getTotalTabByKey(key))
    }
  }
}

/**
 * 处理最近访问记录
 */
interface RecentDataType {
  path: string,
  icon?: string,
  label: string,
  openTime: string
}
// 监听访问记录变化
const recentData = ref<[RecentDataType] | []>([])
watch(() => viewTabsStore.viewTabs, (value) => {
  handleRecentList(value)
},{ deep: true })

// 根据监听结果进行数据处理
const handleRecentList = (viewTabs:Array<StarViewType>) => {
  const recentTabsJson = localStorage.getItem(viewTabsStore.$state.tabCacheKey)
  if (recentTabsJson) {
    const recentTabs =  JSON.parse(recentTabsJson)
    // 当前tab页有数据
    if (viewTabs && viewTabs.length > 0) {
      const actPathList = viewTabs.map(tab => tab.routerPathKey)
      recentData.value = recentTabs.filter((tab:RecentType) => {
        if (tab.path) {
          return !actPathList.includes(tab.path)
        } else {
          return false
        }
      })
    } else {
      recentData.value = recentTabs
    }
  }
}
// 第一次加载页面时的处理
handleRecentList(viewTabsStore.viewTabs)

/**
 * 处理收藏夹
 */
// 鉴定收藏夹变化
const starData = ref()
watch(()=> viewTabsStore.totalViewTabs,(value)=> {
  handleStarList(value)
},{deep: true})

// 根据监听结果进行数据处理
const handleStarList = (totalViewTabs: Array<StarViewType>) => {
  starData.value = totalViewTabs.filter((tab: StarViewType) => tab.star)
}
// 第一次加载页面时的处理
handleStarList(viewTabsStore.totalViewTabs)

/**
 * 控制layout显示关闭
 */
const showHideLayout = () => {
  const data =  localStorage.getItem("show-hide-layout")
  if ('hide' === data) {
    localStorage.setItem("show-hide-layout",'show')
  } else {
    localStorage.setItem("show-hide-layout",'hide')
  }
  viewTabsStore.$state.showLayout = localStorage.getItem("show-hide-layout") === 'show'
  viewTabsStore.setShowLayoutAttribute()
}

</script>
<style>
.ant-tabs-nav-more {
  padding: 8px !important;
  cursor: pointer;
  border-radius: 8px
}
.ant-tabs-nav-more:hover {
  color:  var(--colorPrimary) !important;
}
.menu-item-min-width {
  min-width: 120px;
}
</style>
