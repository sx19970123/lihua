<template>
  <a-dropdown :trigger="['contextmenu']">
    <span>
      <component :is="tabPane.tab.icon"/>
      {{ tabPane.tab.label }}
    </span>
    <template #overlay>
      <a-menu @click="handleClickMenuTab">
        <a-menu-item key="reload" :disabled="tabPane.tab.routerPathKey !== viewTabsStore.$state.activeKey">
          <RedoOutlined />
          刷新页面
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
import { useViewTabsStore } from "@/stores/modules/viewTabs";
import {useRoute, useRouter} from "vue-router";
import {starView} from "@/api/system/starView";
import { message } from "ant-design-vue";
import { StarFilled , StarOutlined ,LockOutlined , UnlockOutlined} from '@ant-design/icons-vue';
import { h } from "vue";

const viewTabsStore = useViewTabsStore()
const tabPane = defineProps(['tab','index'])
const emit = defineEmits(['routeSkip'])
const route = useRoute()
const router = useRouter()

const handleClickMenuTab = ({ key }:{ key :string }) => {
  const tab = tabPane.tab
  switch (key) {
    case "reload": {
      router.replace({ path: route.path, params: route.params, query: route.query, force: true })
      break
    }
    case "close-left": {
      const actIndex = viewTabsStore.getIndex(viewTabsStore.activeKey)
      const index = viewTabsStore.getIndex(tab.routerPathKey)
      viewTabsStore.closeLeft(tab.routerPathKey)
      if (actIndex < index) {
        emit('routeSkip',tab)
      }
      break
    }
    case "close-right": {
      const actIndex = viewTabsStore.getIndex(viewTabsStore.activeKey)
      const index = viewTabsStore.getIndex(tab.routerPathKey)
      viewTabsStore.closeRight(tab.routerPathKey)
      if (actIndex > index) {
        emit('routeSkip',tab)
      }
      break
    }
    case "close-other": {
      viewTabsStore.closeOther(tab.routerPathKey)
      emit('routeSkip',tab)
      break
    }
    case "star": {
      starView(tab.routerPathKey,tab.affix ? '1' : '0','1').then(resp => {
        if (resp.code === 200) {
          viewTabsStore.replaceByKey(resp.data)
          message.success({
            content: () => '添加收藏',
            icon: () => h( StarFilled ),
          })
        }
      }).catch(() => {
        message.error({
          content: () => '添加收藏失败',
          icon: () => h( StarFilled ),
        })
      })
      break
    }
    case "un-star": {
      starView(tab.routerPathKey,tab.affix ? '1' : '0','0').then(resp => {
        if (resp.code === 200) {
          viewTabsStore.replaceByKey(resp.data)
          message.success({
            content: () => '取消收藏',
            icon: () => h( StarOutlined ),
          })
        }
      }).catch(() => {
        message.error({
          content: () => '取消收藏失败',
          icon: () => h( StarOutlined ),
        })
      })
      break
    }
    case "affix": {
      starView(tab.routerPathKey,'1',tab.star ? '1' : '0').then(resp => {
        if (resp.code === 200) {
          viewTabsStore.affix(resp.data)
          message.success({
            content: () => '固定页面',
            icon: () => h( LockOutlined ),
          })
        }
      })
      break
    }
    case "un-affix": {
      starView(tab.routerPathKey,'0',tab.star ? '1' : '0').then(resp => {
        if (resp.code === 200) {
          viewTabsStore.unAffix(resp.data)
          message.success({
            content: () => '取消固定',
            icon: () => h( UnlockOutlined ),
          })
        }
      })
      break
    }
    default: {
      console.error("错误的菜单类型")
    }
  }
}
</script>
