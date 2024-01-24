<template>
  <section>
    <router-view v-slot="{ Component, route}">
<!--      <transition>-->
        <keep-alive>
          <component :is="Component" :key="route.path"/>
        </keep-alive>
<!--      </transition>-->
    </router-view>
  </section>
</template>

<script setup lang="ts">
</script>

<style>

</style>

