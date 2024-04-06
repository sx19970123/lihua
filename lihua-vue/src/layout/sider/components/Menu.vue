<template>
    <template v-for="item in props.data">
<!--      页面-->
      <template v-if="item.type !== 'directory' && (item.children === null || typeof item.component === 'function')">
        <a-menu-item :key="item.key"
                     :title="item.meta ? item.meta.title : ''" :danger="item.danger"
                     v-if="item.meta?.visible && item.meta?.visible !== false">
          <!--图标-->
          <template #icon>
            <component :is="item.meta ? item.meta.icon : ''"/>
          </template>
          <!--外部链接-->
          <a-typography-link v-if="item.type === 'link' && item.meta.linkOpenType === 'new-page'"
                             target="_blank"
                             :href="item.meta.link"
          >
            {{item.meta ? item.meta.label : ''}}
          </a-typography-link>
          <!--内部组件-->
          <router-link v-else :to="{
            path: item.key,
            query: item.query? JSON.parse(item.query): {}
          }">
            {{item.meta ? item.meta.label : ''}}
          </router-link>
        </a-menu-item>
      </template>
<!--    菜单-->
      <template v-else>
        <a-sub-menu :title="item.meta? item.meta.label : ''"
                    :key="item.key"
                    v-if="item.meta?.visible && item.meta?.visible !== false"
        >
          <!--图标-->
          <template #icon>
            <component :is="item.meta? item.meta.icon : ''"/>
          </template>
          <Menu :data="item.children"/>
        </a-sub-menu>
      </template>
    </template>
</template>
<script lang="ts" setup>
const props = defineProps(['data'])
console.log(props.data)
</script>

