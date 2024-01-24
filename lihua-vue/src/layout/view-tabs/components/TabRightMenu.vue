<script setup lang="ts">

</script>

<template>
  <a-dropdown>
    <div class="more-menu">
      <span @click.prevent style="padding-left: 8px">
         <svg focusable="false" class="" data-icon="ellipsis" width="1em" height="1em" fill="currentColor" aria-hidden="true" viewBox="64 64 896 896" style="transform: rotate(90deg);"><path d="M176 511a56 56 0 10112 0 56 56 0 10-112 0zm280 0a56 56 0 10112 0 56 56 0 10-112 0zm280 0a56 56 0 10112 0 56 56 0 10-112 0z"></path></svg>
      </span>
    </div>
    <template #overlay>
      <a-menu>
        <a-menu-item>
          最近的使用
        </a-menu-item>
        <a-menu-item>
          关闭所有标签
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<style scoped>
.more-menu {
  width: 40px;
  cursor: pointer;
}
</style>
