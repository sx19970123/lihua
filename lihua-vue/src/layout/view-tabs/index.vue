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
        <tab-right-menu/>
      </template>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabPaneButton from "@/layout/view-tabs/components/TabPaneButton.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import { computed, reactive, ref, type UnwrapNestedRefs, watch } from "vue";
import { usePermissionStore } from "@/stores/modules/permission";
import { useRoute,useRouter } from "vue-router";
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
  // 初始化固定的数据
  allViewTags.value.forEach(tag => {
    if (tag.affix) {
      activeTab.push(tag)
    }
  })
  return {
    activeTab,
    activeKey
  }
}
const { activeTab, activeKey } = init()
/**
 * 传入key，进行对应标签页反选。新页面新增tab页
 * @param key tab页 key
 */
const handleSelectTab = (key: string) => {
  // 是否在活动tab中存在，不存在调用新增方法
  const includes = activeTab.map(act => act.routerPathKey).includes(key)
  if (!includes) {
    allViewTags.value.forEach(tab => {
      if (tab.routerPathKey === key) {
        activeTab.push(tab)
      }
    })
  }
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
 * 处理点击右键菜单
 * @param type
 * @param tab
 */
const handleClickMenuTab = (type: string,tab: any) => {
  console.log(tab)
  switch (type) {
    case "reload": {
      reload()
      break
    }
    case "close-left": {
      closeLeft(tab)
      break
    }
    case "close-right": {
      closeRight(tab)
      break
    }
    case "close-other": {
      closeOther()
      break
    }
    case "star": {
      star()
      break
    }
    case "un-star": {
      unStar()
      break
    }
    case "affix": {
      affix()
      break
    }
    case "un-affix": {
      unAffix()
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

}

const closeAll = () => {

}

const star = () => {

}

const unStar = () => {

}

const affix = () => {

}

const unAffix = () => {

}


</script>

<style scoped lang="less">

</style>
