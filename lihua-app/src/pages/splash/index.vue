<template>
</template>

<script setup lang="ts">
import { onMounted } from "vue"
import router from "@/router/Router"
/**
 * 跳转到首页
 */
const toIndex = () => {
	router.reLaunch({
		url: '/pages/index/index'
	})
}

onMounted(() => {
	toIndex()
})
</script>