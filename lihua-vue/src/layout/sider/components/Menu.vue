<template>
    <template v-for="item in routers.data">
<!--      页面-->
      <template v-if="item.children === null">
        <a-menu-item :key="item.id" :title="item.title">
          <template #icon>
            <component :is="item.icon"/>
          </template>
          {{item.label}}
        </a-menu-item>
      </template>
<!--    菜单-->
      <template v-else>
        <a-sub-menu :title="item.label" :key="item.id">
          <template #icon>
            <component :is="item.icon"/>
          </template>
          <Menu :data="item.children"/>
        </a-sub-menu>
      </template>
    </template>
</template>
<script lang="ts" setup>
interface RouterType {
  children: Array<RouterType>,
  icon: string | null,
  id: string,
  label: string,
  title: string
}

const routers = defineProps({
  data: Array<RouterType>
})

console.log("routers=",routers)
</script>

