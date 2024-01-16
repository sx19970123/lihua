<template>
  <section>
    <router-view v-slot="{ Component, route}">
      <div>{{console.log(Component)}}</div>
      <transition>
        <keep-alive>

          <component :is="Component" :key="route.path"/>

        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script setup lang="ts">
</script>

<style scoped>

</style>
