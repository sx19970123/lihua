<template>
    <template v-for="item in routers.data">
<!--      页面-->
      <template v-if="item.type !== 'directory' && (item.children === null || typeof item.component === 'function')">
        <a-menu-item :key="item.key" :title="item.meta ? item.meta.title : ''" :danger="item.danger">
          <template #icon>
            <component :is="item.meta ? item.meta.icon : ''"/>
          </template>
          <router-link :to="item.key">
            {{item.meta ? item.meta.label : ''}}
          </router-link>
        </a-menu-item>
      </template>
<!--    菜单-->
      <template v-else>
        <a-sub-menu :title="item.meta? item.meta.label : ''" :key="item.key">
          <template #icon>
            <component :is="item.meta? item.meta.icon : ''"/>
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
  type?: string,
  menuShow?: boolean
}
interface MenuType {
  path: string | null,
  meta: MetaType,
  component: Component | null,
  children: Array<MenuType> | null,
  type: string | null,
  key: string,
  danger: boolean
}

const routers = defineProps(['data'])
</script>

