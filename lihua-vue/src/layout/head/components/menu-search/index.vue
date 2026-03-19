<template>
  <div>
    <div @click="open = true" v-show="!open">
      <a-tooltip title="菜单搜索" placement="bottom" :get-popup-container="(triggerNode: HTMLElement) => triggerNode.parentNode">
        <a-input placeholder="搜索" readonly class="title-search-input" v-if="!themeStore.isSmallWindow">
          <template #prefix>
            <SearchOutlined class="icon-default-color"/>
          </template>
          <template #suffix>
            <a-tag class="title-search-tag" style="margin-right: var(--lihua-space-xs)">{{osType() === 'MacOS' ? '⌘' : 'ctrl'}}</a-tag>
            <a-tag class="title-search-tag" style="margin-right: 0">k</a-tag>
          </template>
        </a-input>
        <a-button type="text" v-else>
          <template #icon>
            <SearchOutlined class="icon-default-color"/>
          </template>
        </a-button>
      </a-tooltip>
    </div>

<!--    菜单搜索dialog-->
    <a-modal v-model:open="open" :closable="false" :z-index="99999">
<!--      我的收藏-->
      <a-typography-text strong v-if="starDataList.length > 0">我的收藏</a-typography-text>
      <a-flex :gap="8" wrap="wrap" class="menu-group">
        <div v-for="(starData, index) in starDataList" v-show="starMenuUnfoldStatus ? index <= starDataList.length : index < 3">
          <a-button size="small" @click="skipMenu(starData.routerPathKey)">
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
          <a-button size="small"  @click="skipMenu(recentData.path)">
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
      <a-typography-text strong>全部菜单</a-typography-text>
      <selectable-card
          v-if="open"
          :card-style="{marginTop: 'var(--lihua-space-xs)', marginBottom: 'var(--lihua-space-xs)'}"
          v-model="pathKey"
          :gap="4"
          :data-source="menuList"
          :scroll-view-index="selectedIndex"
          item-key="key"
          vertical
          ref="allMenuCardRef"
          empty-description="没有匹配菜单"
          @click="handleClickMenu"
      >
        <template #content="{item, isSelected, color}">
<!--          被选中的元素-->
          <a-flex justify="space-between" v-show="isSelected">
            <a-space size="middle">
              <component :is="item.icon" class="menu-icon" :style="{color}"/>
              <a-flex vertical>
                <a-typography-text :style="{color}">{{item.label}}</a-typography-text>
                <a-typography-text :style="{color}">{{item.key}}</a-typography-text>
              </a-flex>
            </a-space>
            <EnterOutlined v-show="isSelected" :style="{ color }"/>
          </a-flex>
<!--          未被选中的元素，关键词检索时需要高亮显示命中的关键词-->
          <a-space size="middle" v-show="!isSelected">
            <component :is="item.icon" class="menu-icon"/>
            <a-flex vertical>
<!--              标签匹配-->
              <div v-if="keyword && item.label.includes(keyword)">
                <a-typography-text>{{item.label.substring(0, item.label.indexOf(keyword))}}</a-typography-text>
                <a-typography-text :style="{color}">{{keyword}}</a-typography-text>
                <a-typography-text>{{item.label.substring(item.label.indexOf(keyword) + keyword?.length)}}</a-typography-text>
              </div>
              <div v-else>
                <a-typography-text>{{item.label}}</a-typography-text>
              </div>
<!--              key匹配-->
              <div v-if="keyword && item.key.includes(keyword)">
                <a-typography-text type="secondary">{{item.key.substring(0, item.key.indexOf(keyword))}}</a-typography-text>
                <a-typography-text :style="{color}">{{keyword}}</a-typography-text>
                <a-typography-text type="secondary">{{item.key.substring(item.key.indexOf(keyword) + keyword?.length)}}</a-typography-text>
              </div>
              <div v-else>
                <a-typography-text type="secondary">{{item.key}}</a-typography-text>
              </div>
            </a-flex>
          </a-space>
        </template>
      </selectable-card>
      <!--      头部搜索栏-->
      <template #title>
        <a-input placeholder="搜索菜单，匹配名称和路径" v-model:value="keyword" @change="debounceFilter" size="large" allow-clear ref="menuSearchInputRef">
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
import {nextTick, onBeforeUnmount, onMounted, ref, useTemplateRef, watch} from "vue";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
import {usePermissionStore} from "@/stores/permission.ts";
import {useThemeStore} from "@/stores/theme.ts";
import {useRouter} from "vue-router";
import {osType} from "@/utils/OS"
import SelectableCard from "@/components/selectable-card/index.vue";
import type {RecentType, StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
import {traverseWithPath} from "@/utils/Tree.ts";
import {cloneDeep, debounce, throttle} from "lodash-es"
import type {ItemType} from "ant-design-vue";

const viewTabsStore = useViewTabsStore();
const permissionStore = usePermissionStore();
const themeStore = useThemeStore();
const router = useRouter();

// 菜单类型
type MenuItem = ItemType & {children: ItemType[], label: string, key: string}
// modal开关
const open = ref<boolean>(false)
// 菜单path
const pathKey = ref<string>()
// 滚动条位置
const selectedIndex = ref<number>(0)
// 搜索框ref
const menuSearchInputRef = useTemplateRef<HTMLInputElement>('menuSearchInputRef')
// 全部按钮ref
const allMenuCardRef = useTemplateRef<InstanceType<typeof SelectableCard>>('allMenuCardRef')

// 监听键盘按下事件
const handleKeydown = (e: KeyboardEvent) => {

  // 快捷方式，开关modal
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
    e.preventDefault()
    open.value = !open.value
  }

  // 下按键，选择菜单
  if (open.value && e.key === 'ArrowDown') {
    e.preventDefault()
    if (pathKey.value) {
      let index = menuList.value.findIndex(item => item && item.key === pathKey.value)
      if (index !== menuList.value.length - 1) {
        index++
      } else {
        index = 0
      }
      pathKey.value = menuList.value[index]?.key as string
      selectedIndex.value = index
    } else {
      pathKey.value = menuList.value[0]?.key
      selectedIndex.value = 0
    }
  }

  // 上按键，选择菜单
  if (open.value && e.key === 'ArrowUp') {
    e.preventDefault()
    if (pathKey.value) {
      let index = menuList.value.findIndex(item => item && item.key === pathKey.value)
      if (index !== 0) {
        index--
      } else {
        index = menuList.value.length - 1
      }
      selectedIndex.value = index
      pathKey.value = menuList.value[index]?.key as string
    } else {
      const index = menuList.value.length - 1
      pathKey.value = menuList.value[index]?.key
      selectedIndex.value = index
    }
  }

  // 回车键，进入菜单
  if (open.value && pathKey.value && e.key === 'Enter') {
    skipMenu(pathKey.value)
  }
}

// 函数节流
const throttleHandleKeydown = throttle(handleKeydown, 70)

/**
 * 点击切换页面
 * @param item 点击元素
 */
const handleClickMenu = ({item}: {item: MenuItem}) => {
  skipMenu(item.key)
}

/**
 * 跳转菜单
 * @param key 菜单路径
 */
const skipMenu = (key?: string | null) => {
  if (key) {
    router.push(key)
    open.value = false
  }
}

/**
 * 重置modal
 */
const reset = () => {
  pathKey.value = undefined
  starMenuUnfoldStatus.value = false
  recentMenuUnfoldStatus.value = false
  keyword.value = undefined
  // 滚动条还原
  selectedIndex.value = -1
  nextTick(() => {
    selectedIndex.value = 0
  })
}

/**
 * 关键词检索
 */
const initKeywordSearch = () => {
  // 关键词
  const keyword = ref<string>()

  // 处理根据关键词过滤
  const handleFilter = () => {
    // 重置选中的选项
    const value = keyword.value
    // 重置菜单
    loadMenu()

    if (value) {
      menuList.value = menuList.value.filter((item: MenuItem) => item && (item.label.includes(value) || item.key.includes(value)))
      // 过滤的内容中没有当前选中的菜单，pathKey设置为空
      const someKey = menuList.value.some((item: MenuItem) => item.key === pathKey.value)
      if (!someKey) {
        pathKey.value = undefined
      }
    }
  }

  return {
    keyword,
    handleFilter
  }
}

const { keyword, handleFilter } = initKeywordSearch()

// 函数防抖
const debounceFilter = debounce(handleFilter, 300)

/**
 * 初始化全部菜单
 */
const initAllMenu = () => {
  // 双向绑定的菜单集合
  const menuList = ref<MenuItem[]>([])
  // 全部菜单
  const allMenuList: MenuItem[] = []
  // 加载全部菜单
  const loadBaseMenu = () => {
    menuList.value = []
    // 菜单中路由
    const menuRouters = cloneDeep(permissionStore.menuRouters)
    // 递归所有菜单
    traverseWithPath(menuRouters, (menuItems) => {
      // 单层
      if (menuItems.length === 1) {
        const firstItem = menuItems[0] as MenuItem
        // children 不存在，认为是页面
        if (firstItem && firstItem.children === undefined) {
          allMenuList.push(firstItem)
        }
      }
      // 多层，合并label
      else {
        const lastItem = menuItems[menuItems.length - 1] as MenuItem
        if (lastItem && lastItem.children === undefined) {
          lastItem.label = menuItems.map((item) => {
            const menuItem = item as MenuItem
            return menuItem.label;
          }).join("/")
          allMenuList.push(lastItem)
        }
      }
    })
  }

  // 加载菜单
  const loadMenu = () => {
    menuList.value = allMenuList
  }

  return {
    menuList,
    loadBaseMenu,
    loadMenu
  }
}

const { menuList, loadBaseMenu, loadMenu } = initAllMenu()

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

watch(() => open.value, () => {
  // 打开modal时进行操作
  if (open.value) {
    // 重置modal
    reset()
    // 加载菜单
    loadMenu()
    // 加载收藏菜单
    loadStarMenu()
    // 加载最近打开菜单
    loadRecentMenu()
    // 输入框聚焦
    nextTick(() => {
      menuSearchInputRef.value?.focus()
    })
  }
})

onMounted(() => {
  // 加载全部菜单基础数据
  loadBaseMenu()
  window.addEventListener('keydown', throttleHandleKeydown)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', throttleHandleKeydown)
})
</script>

<style scoped>
.title-search-input {
  width: 135px;
  margin-right: var(--lihua-space-sm);
  background-color: var(--lihua-alpha-level-0) !important;
}
.title-search-input:hover {
  cursor: pointer !important;
}
.bottom-tag-tips {
  margin-right: 2px;
}
.menu-group {
  margin: var(--lihua-space-sm) 0;
}
.menu-icon {
  font-size: var(--lihua-font-size-lg)
}
:deep(.title-search-input input) {
  cursor: pointer;
  background-color: var(--lihua-alpha-level-0) !important;
}

</style>