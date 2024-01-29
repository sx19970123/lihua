<template>
  <div :style="{'background': '#fff'}">
    <a-tabs :activeKey="activeKey"
            type="editable-card"
            size="small"
            hide-add
            @edit="handleRemove"
            @change="handleSwitchTab"
    >
      <a-tab-pane v-for="(tab,index) in activeTab" :key="tab.routerPathKey" :closable="!tab.affix">
        <!--每个tab的下拉菜单-->
        <template #tab>
          <tab-pane-menu :tab="tab" :index="index" :length="activeTab.length" :activeKey="activeKey"  @click-menu-tab="handleClickMenuTab"/>
        </template>
      </a-tab-pane>
      <!-- view-tabs 左侧空白-->
      <template #leftExtra>
        <div style="width: 8px;"></div>
      </template>
      <!--view-tabs 右侧下拉菜单-->
      <template #rightExtra>
        <tab-right-menu @click-menu-tab="handleClickMenuTab"/>
      </template>
    </a-tabs>
  </div>
  <tab-recent-visit-modal :show="showRecentModal"
                          :active-tabs="activeTab"
                          @close-recent-modal="handleCloseRecentModal"
                          @switch-tab="handleSwitchRecentTab"
  />
</template>

<script lang="ts" setup>
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import TabRecentVisitModal from "@/layout/view-tabs/components/TabRecentVisitModal.vue";
import { computed, reactive, ref, type UnwrapNestedRefs, watch } from "vue";
import { usePermissionStore } from "@/stores/modules/permission";
import { useRoute,useRouter } from "vue-router";
import { format } from "@/utils/date";
import { starView } from "@/api/system/starView";
const permission = usePermissionStore()
const route = useRoute()
const router = useRouter()
const allViewTags = computed(() => permission.viewTabs)

/**
 * 初始化
 */
const init = () => {
  // 当前活动的数据
  const activeTab: UnwrapNestedRefs<any[]> = reactive([])
  const activeKey = ref()
  const showRecentModal = ref(false)
  // 初始化固定的数据
  allViewTags.value.forEach(tag => {
    if (tag.affix) {
      activeTab.push(tag)
    }
  })
  return {
    activeTab,
    activeKey,
    showRecentModal
  }
}
const { activeTab, activeKey , showRecentModal} = init()
/**
 * 传入key，进行对应标签页反选。新页面新增tab页
 * @param key tab页 key
 */
const handleSelectTab = (key: string) => {
  // 是否在活动tab中存在，不存在调用新增方法
  const includes = activeTab.map(act => act.routerPathKey).includes(key)
  allViewTags.value.forEach(tab => {
    if (tab.routerPathKey === key) {
      // 添加到数组
      if (!includes) {
        activeTab.push(tab)
      }
      // 添加到缓存
      handleAddTabCache(tab)
    }
  })
  activeKey.value = key
}

/**
 * 切换标签，key值存在，则切换到对应的页面。
 * 否则判断是否为固定值，固定值切换到第一个，不是固定值切换到前一个
 * @param key
 */
const handleSwitchTab = (key: string) => {
  routeSkip(activeTab.filter(tab => tab.routerPathKey === key)[0])
}

/**
 * 删除标签，根据情况进行路由切换
 * @param key
 */
const handleRemove = (key: string) => {
  const index = activeTab.findIndex(tab => tab.routerPathKey === key);
  if (index !== -1) {
    activeTab.splice(index, 1);
    if (route.path === key) {
      handleNextRoute(index)
    }
  }
};

/**
 * 处理关闭后需要跳转的下一个路由
 * @param index 关闭标签的索引
 */
const handleNextRoute = (index: number) => {
  // 关闭的第一个 tab,判断当前存在的 activeTab 还有没有，若存在，则跳转到下一个
  if (index === 0 && activeTab.length !== 0) {
    routeSkip(activeTab[0])
    return
  }

  const tab = activeTab[index - 1]
  if (!tab.affix) {
    routeSkip(tab)
    return
  }
  routeSkip(activeTab[0])
}

/**
 * 路由跳转
 * @param tab
 */
const routeSkip = (tab:any) => {
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

/**
 * 向 localStorage 中进行数据缓存
 * @param tab
 */
const handleAddTabCache = (tab: any) => {
  const recentTabs = localStorage.getItem('recent-tabs')
  // 第一次新建缓存集合
  if (recentTabs === null) {
    localStorage.setItem('recent-tabs',JSON.stringify([
      {
        openTime: format(new Date(),'yyyy-MM-dd hh:mm'),
        icon: tab.icon,
        label: tab.label,
        path: tab.routerPathKey
      }
    ]))
  } else {
    const hisArray = JSON.parse(recentTabs)
    const index = hisArray.findIndex((his: any) => his.path === tab.routerPathKey)
    // 删除已存在元素
    if (index !== -1) {
      hisArray.splice(index,1)
    }
    // 首插后存入缓存
    hisArray.unshift({
      openTime: format(new Date(),'yyyy-MM-dd hh:mm'),
      icon: tab.icon,
      label: tab.label,
      path: tab.routerPathKey
    })
    localStorage.setItem('recent-tabs',JSON.stringify(hisArray))
  }
}

/**
 * 监听路由变化进行切换 tab
 */
watch(() => route.path,(value) => {
  handleSelectTab(value)
})

/**
 * 第一次进入组件选中 tab
 */
handleSelectTab(route.path);

/**
 * 处理点击菜单操作
 * @param type
 * @param tab
 */
const handleClickMenuTab = (type: string,tab: any) => {
  switch (type) {
    case "reload": {
      reload()
      break
    }
    case "close-left": {
      closeLeft(tab)
      routeSkip(tab)
      break
    }
    case "close-right": {
      closeRight(tab)
      routeSkip(tab)
      break
    }
    case "close-other": {
      closeOther(tab)
      routeSkip(tab)
      break
    }
    case "history": {
      showRecentModal.value = true
      break
    }
    case "close-all": {
      closeAll()
      if (activeTab.length > 0) {
        routeSkip(activeTab[0])
      }
      break
    }
    case "star": {
      star(tab,true)
      break
    }
    case "un-star": {
      star(tab,false)
      break
    }
    case "affix": {
      affix(tab,true)
      break
    }
    case "un-affix": {
      affix(tab,false)
      break
    }
    default: {
      console.error("错误的菜单类型")
    }
  }
}

/**
 * 刷新当前组件
 */
const reload = () => {
  router.replace({ path: route.path, params: route.params, query: route.query, force: true })
}
/**
 * 关闭左侧标签
 */
const closeLeft = (tab: any) => {
  const index = activeTab.indexOf(tab)
  for (let i = 0; i < index; i++) {
    if (!activeTab[i].affix) {
      activeTab.splice(i, 1);
    }
  }
}
/**
 * 关闭右侧标签
 * @param tab
 */
const closeRight = (tab: any) => {
  const index = activeTab.indexOf(tab)
  for (let i = activeTab.length - 1; i > index; i--) {
    if (!activeTab[i].affix) {
      activeTab.splice(i, 1);
    }
  }
}
/**
 * 关闭其其他标签
 * @param tab
 */
const closeOther = (tab: any) => {
  for (let i = activeTab.length - 1; i >= 0; i--) {
    const t = activeTab[i];
    if (!t.affix && t !== tab) {
      activeTab.splice(i, 1);
    }
  }
}
/**
 * 关闭全部标签
 */
const closeAll = () => {
  for (let i = activeTab.length - 1; i >= 0; i--) {
    const t = activeTab[i];
    if (!t.affix) {
      activeTab.splice(i, 1);
    }
  }
}
/**
 * 关闭最近使用模态框
 */
const handleCloseRecentModal = () => {
  showRecentModal.value = false
}
/**
 * 处理切换选中的最近 tab
 */
const handleSwitchRecentTab = (key: string) => {
 const targetTab = allViewTags.value.filter(tab => tab.routerPathKey === key)
  if (targetTab) {
    routeSkip(targetTab[0])
  }
}
/**
 * 添加/取消收藏
 * @param tab
 */
const star = (tab: any, star: boolean) => {
  starView(tab.routerPathKey,tab.affix ? '1' : '0','1').then(resp => {
    if (resp.code === 200) {
      permission.updateViewTabStar(tab.routerPathKey, star)
    }
  })
}
/**
 * 添加/取消固定
 * @param tab
 * @param affix
 */
const affix = (tab: any, affix: boolean) => {
  starView(tab.routerPathKey,'1',tab.star ? '1' : '0').then(resp => {
    if (resp.code === 200) {
      permission.updateViewTabAffix(tab.routerPathKey, affix)

      const targetTab = activeTab.filter(actTab => actTab.routerPathKey === tab.routerPathKey)[0]

      // 固定页面情况下将该元素移动到前方
      if (affix) {
        const targetIndex = activeTab.filter(actTab => actTab.affix).length - 1
        activeTab.splice(activeTab.indexOf(targetTab),1)
        activeTab.splice(targetIndex,0,targetTab)
      }
      // 取消固定情况下将元素移动到后方
      else {
        activeTab.splice(activeTab.indexOf(targetTab),1)
        activeTab.push(targetTab)
      }

    }
  })
}


</script>

<style scoped lang="less">

</style>
