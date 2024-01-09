<script setup lang="ts">

</script>

<template>
  <a-dropdown>
          <span @click.prevent style="padding-left: 8px">
           <svg focusable="false" class="" data-icon="ellipsis" width="1em" height="1em" fill="currentColor" aria-hidden="true" viewBox="64 64 896 896" style="transform: rotate(90deg);"><path d="M176 511a56 56 0 10112 0 56 56 0 10-112 0zm280 0a56 56 0 10112 0 56 56 0 10-112 0zm280 0a56 56 0 10112 0 56 56 0 10-112 0z"></path></svg>
          </span>
    <template #overlay>
      <a-menu>
        <a-menu-item key="0">
          <a target="_blank" rel="noopener noreferrer" href="http://www.alipay.com/">
            1st menu item
          </a>
        </a-menu-item>
        <a-menu-item key="1">
          <a target="_blank" rel="noopener noreferrer" href="http://www.taobao.com/">
            2nd menu item
          </a>
        </a-menu-item>
        <a-menu-divider />
        <a-menu-item key="3" disabled>3rd menu item（disabled）</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<style scoped>

</style>
