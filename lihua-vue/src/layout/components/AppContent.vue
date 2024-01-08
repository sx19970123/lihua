<!--组件主容器-->
<template>
  <section>
    <router-view>
      <transition>
        <keep-alive>
          <component></component>
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script setup>

</script>

<style scoped>

</style>
