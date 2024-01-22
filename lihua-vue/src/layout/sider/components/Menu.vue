<template>
    <template v-for="item in props.data">
<!--      页面-->
      <template v-if="item.children === null">
        <a-menu-item :key="item.id">
          <template #icon>
            <component :is="item.icon"></component>
          </template>
          {{item.label}}
        </a-menu-item>
      </template>

<!--    菜单/分割-->
      <template v-else>
        <a-menu-item-group v-if="item.type === 'group'" :key="item.id" :title="item.label">
          <Menu :data="item.children"/>
        </a-menu-item-group>
        <a-sub-menu v-else :key="item.id" :title="item.label">
          <template #icon>
            <component :is="item.icon"></component>
          </template>
          <Menu :data="item.children"/>
        </a-sub-menu>
      </template>
    </template>
</template>
<script lang="ts" setup>
const props = defineProps({
  data: Array<object>
})
</script>

