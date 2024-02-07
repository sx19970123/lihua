<template>
    <a-breadcrumb :routes="pageRoute" v-if="pageRoute.length > 1">
        <template #itemRender="{route}">
           <span v-if="pageRoute.indexOf(route) === pageRoute.length - 1">
            {{ route.meta.label }}
            </span>
            <router-link v-else :to="route">
              {{ route.meta.label }}
            </router-link>
        </template>
    </a-breadcrumb>
</template>
<script lang="ts" setup>
import {useRoute} from "vue-router";
import Layout from "@/layout/index.vue"
import MiddleView from "@/components/middle-view/index.vue"
import {ref, watch} from "vue";
const route = useRoute()

// 初始化
const pageRoute = ref(route.matched.filter(match => match.components?.default !== Layout && match.components?.default !== MiddleView));
pageRoute.value.forEach(r => r.children = [])

// 监听路由变化
watch(() => route.matched,(value) => {
  pageRoute.value = value.filter(match => match.components?.default !== Layout && match.components?.default !== MiddleView)
  pageRoute.value.forEach(r => r.children = [])
})
</script>
