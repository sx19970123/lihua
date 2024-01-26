<template>
  <a-dropdown :trigger="['contextmenu']">
    <div>
      <component :is="tagPane.tab.icon"/>
      {{ tagPane.tab.label }}
    </div>

    <template #overlay>
      <a-menu @click="send">
        <a-menu-item key="reload">
          <RedoOutlined />
          刷新页面
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="close-left" :disabled="closeLeft">
          <VerticalRightOutlined />
          关闭左侧
        </a-menu-item>
        <a-menu-item key="close-right" :disabled="closeRight">
          <VerticalLeftOutlined />
          关闭右侧
        </a-menu-item>
        <a-menu-item key="close-other" :disabled="closeOther">
          <CloseCircleOutlined />
          关闭其他
        </a-menu-item>
        <a-menu-item key="close-all" :disabled="closeAll">
          <CloseOutlined />
          全部关闭
        </a-menu-item>
        <a-menu-divider v-if="!tagPane.tab.static"/>
        <a-menu-item key="star" v-if="!tagPane.tab.star && !tagPane.tab.static">
          <StarOutlined />
          添加收藏
        </a-menu-item>
        <a-menu-item key="un-star" v-if="tagPane.tab.star && !tagPane.tab.static">
          <StarFilled />
          取消收藏
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
const emits = defineEmits(['clickMenuTab'])
const tagPane = defineProps({
  tab: null,
  activeTabs: Array<any>
})

/**
 * 加载菜单元素显示状态
 */
let closeLeft = ref(!tagPane.activeTabs?.indexOf(tagPane.tab))
let closeRight = ref(tagPane.activeTabs?.indexOf(tagPane.tab) === tagPane.activeTabs?.length as number - 1)
let closeOther = ref(tagPane.activeTabs?.length === 1)
let closeAll = ref(tagPane.activeTabs?.length === 1)

/**
 * 监听当前活动集合重新控制按钮
 */
watch(() => tagPane.activeTabs, () => {
   closeLeft = ref(!tagPane.activeTabs?.indexOf(tagPane.tab))
   closeRight = ref(tagPane.activeTabs?.indexOf(tagPane.tab) === tagPane.activeTabs?.length as number - 1)
   closeOther = ref(tagPane.activeTabs?.length === 1)
   closeAll = ref(tagPane.activeTabs?.length === 1)
},{ deep: true })

/**
 * 向父类抛出方法
 * @param key
 */
const send = ({ key }: { key :string }) => {
  emits('clickMenuTab', key, tagPane.tab)
}

</script>
