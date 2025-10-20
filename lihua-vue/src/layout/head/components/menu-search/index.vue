<template>
  <div>
    <div class="header-right-item" @click="open = true" v-show="!open">
      <a-input placeholder="搜索" readonly class="title-search-input">
        <template #prefix>
          <SearchOutlined />
        </template>
        <template #suffix>
          <a-tag style="margin-right: 4px">{{osType() === 'MacOS' ? '⌘' : 'ctrl'}}</a-tag>
          <a-tag style="margin-right: 0">k</a-tag>
        </template>
      </a-input>
    </div>
<!--    菜单搜索dialog-->
    <a-modal v-model:open="open" :closable="false">
<!--      我的收藏-->
      <a-typography-text strong v-if="starDataList.length > 0">我的收藏</a-typography-text>
      <a-flex :gap="8" wrap="wrap" class="menu-group">
        <div v-for="(starData, index) in starDataList" v-show="starMenuUnfoldStatus ? index <= starDataList.length : index < 3">
          <a-button size="small">
            <template #icon>
              <component :is="starData.icon"/>
            </template>
            {{starData.label}}
          </a-button>
        </div>
        <a-button size="small" type="link" @click="starMenuUnfoldStatus = !starMenuUnfoldStatus" v-if="starDataList.length > 3">
          {{ starMenuUnfoldStatus ? '收起' : '展开'}}
        </a-button>
      </a-flex>
<!--      最近使用-->
      <a-typography-text strong v-if="recentDataList.length > 0">最近使用</a-typography-text>
      <a-flex :gap="8" wrap="wrap" class="menu-group">
        <div v-for="(recentData, index) in recentDataList" v-show="recentMenuUnfoldStatus ? index <= recentDataList.length : index < 3">
          <a-button size="small">
            <template #icon>
              <component :is="recentData.icon"/>
            </template>
            {{recentData.label}}
          </a-button>
        </div>
        <a-button size="small" type="link" @click="recentMenuUnfoldStatus = !recentMenuUnfoldStatus" v-if="recentDataList.length > 3">
          {{ recentMenuUnfoldStatus ? '收起' : '展开'}}
        </a-button>
      </a-flex>
<!--      所有菜单-->
      <a-typography-text strong>所有菜单</a-typography-text>
      <selectable-card
          :card-style="{marginTop: '4px', marginBottom: '4px'}"
          class="menu-content"
          v-model="pathKey"
          :gap="4"
          :data-source="[]"
          item-key="index"
          vertical>
        <template #content="{item}">
          {{item.index}}
        </template>
      </selectable-card>
      <!--      头部搜索栏-->
      <template #title>
        <a-input placeholder="搜索菜单" size="large" allow-clear ref="menuSearchInputRef">
          <template #prefix>
            <SearchOutlined />
          </template>
        </a-input>
      </template>
      <!--      底部操作提示-->
      <template #footer>
        <a-flex gap="16">
          <div>
            <a-tag class="bottom-tag-tips"><EnterOutlined /></a-tag> 进入
          </div>
          <div>
            <a-tag class="bottom-tag-tips"><SwapOutlined style="transform: rotate(90deg)"/></a-tag> 切换
          </div>
          <div>
            <a-tag class="bottom-tag-tips">ESC</a-tag> 关闭
          </div>
        </a-flex>
      </template>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import {nextTick, onBeforeUnmount, onMounted, ref, useTemplateRef, watchEffect} from "vue";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
import {usePermissionStore} from "@/stores/permission.ts";
import {osType} from "@/utils/OS"
import SelectableCard from "@/components/selectable-card/index.vue";
import type {RecentType, StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
import {traverseWithPath} from "@/utils/Tree.ts";
import type {ItemType} from "ant-design-vue";
import type {RouteRecordRaw} from "vue-router";

const viewTabsStore = useViewTabsStore();
const permissionStore = usePermissionStore();
// modal开关
const open = ref<boolean>(false)
// 菜单path
const pathKey = ref<number>(1)
// 搜索框ref
const menuSearchInputRef = useTemplateRef<HTMLInputElement>('menuSearchInputRef')

// 监听键盘按下事件
const handleKeydown = (e: KeyboardEvent) => {

  // 快捷方式，开关modal
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
    e.preventDefault()
    open.value = !open.value
  }

  // 上按键，选择菜单
  if (open.value && e.key === 'ArrowDown') {
    e.preventDefault()
    pathKey.value++
  }

  // 下按键，选择菜单
  if (open.value && e.key === 'ArrowUp') {
    e.preventDefault()
    pathKey.value--
  }

  // 回车键，进入菜单
  if (open.value && e.key === 'Enter') {

  }
}


/**
 * 初始化全部菜单
 */
const initAllMenu = () => {

  // 获取全部菜单
  const getMenu = () => {
    const menuRouters = permissionStore.menuRouters
    // 递归所有菜单
    traverseWithPath(menuRouters, (menuItems) => {
      if (menuItems.length === 1) {
        const firstItem: any = menuItems[0]
        // children 不存在，认为是页面
        if (firstItem.children === undefined || firstItem.children === null) {

        }
      } else {

      }
      console.log("menuItem===", menuItems)
    })

  }

  return {
    getMenu
  }
}

const { getMenu } = initAllMenu()


/**
 * 重置modal
 */
const reset = () => {
  starMenuUnfoldStatus.value = false
  recentMenuUnfoldStatus.value = false
}

/**
 * 初始化收藏菜单
 */
const initStarMenu = () => {
  // 是否展开
  const starMenuUnfoldStatus = ref<boolean>(false)
  // 收藏菜单列表
  const starDataList = ref<StarViewType[]>([])
  // 加载收藏菜单列表
  const loadStarMenu = () => {
    starDataList.value = viewTabsStore.totalViewTabs.filter(item => item.star)
  }

  return {
    starMenuUnfoldStatus,
    starDataList,
    loadStarMenu,
  }
}

const {starMenuUnfoldStatus, starDataList, loadStarMenu} = initStarMenu();

/**
 * 初始化最近使用菜单
 */
const initRecentMenu = () => {
  // 是否展开
  const recentMenuUnfoldStatus = ref<boolean>(false)
  // 最近使用页面列表
  const recentDataList = ref<RecentType[]>([])
  // 加载最近使用列表
  const loadRecentMenu = () => {
    const recentTabsJson = localStorage.getItem(viewTabsStore.$state.tabCacheKey)
    if (recentTabsJson) {
      recentDataList.value = JSON.parse(recentTabsJson)
    }
  }

  return {
    recentMenuUnfoldStatus,
    recentDataList,
    loadRecentMenu,
  }
}

const {recentMenuUnfoldStatus, recentDataList, loadRecentMenu} = initRecentMenu()

watchEffect(() => {
  // 打开modal时进行操作
  if (open.value) {
    // 输入框聚焦
    nextTick(() => {
      menuSearchInputRef.value?.focus()
    })
    // 获取全部菜单
    getMenu()
    // 加载收藏菜单
    loadStarMenu()
    // 加载最近打开菜单
    loadRecentMenu()
    // 重置modal
    reset()
  }
})

onMounted(() => {
  window.addEventListener('keydown', handleKeydown)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleKeydown)
})
</script>

<style scoped>
.title-search-input {
  width: 130px;
  margin-right: 8px;
}
.title-search-input:hover {
  cursor: pointer !important;
}
.bottom-tag-tips {
  margin-right: 2px;
}
.menu-group {
  margin-top: 8px;
  margin-bottom: 8px;
}
:deep(.title-search-input input) {
  cursor: pointer;
}
</style>
