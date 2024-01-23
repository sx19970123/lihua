<template>
    <template v-for="item in routers.data">
<!--      页面-->
      <template v-if="item.children === null">
        <a-menu-item :key="item.key" :title="item.meta.title">
          <template #icon>
            <component :is="item.meta.icon"/>
          </template>
          {{item.meta.label}}
        </a-menu-item>
      </template>
<!--    菜单-->
      <template v-else>
        <a-sub-menu :title="item.meta.label" :key="item.key">
          <template #icon>
            <component :is="item.meta.icon"/>
          </template>
          <Menu :data="item.children"/>
        </a-sub-menu>
      </template>
    </template>
</template>
<script lang="ts" setup>
import type {Component} from "vue";

interface MetaType {
  noCache?: boolean,
  label?: string,
  title?: string,
  icon?: string | Component,
  affix?: boolean,
  menuShow?: boolean
}
interface MenuType {
  path: string | null,
  meta: MetaType,
  component: Component | null,
  children: Array<MenuType> | null,
  key: string
}

const routers = defineProps({
  data: Array<MenuType>
})

</script>

