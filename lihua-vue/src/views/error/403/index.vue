<template>
  <div class="not-found-container">
    <h1 class="error-title">403 Forbidden Access</h1>
    <p class="error-message"><component is="PixelCat"/> 您没有权限访问此页面 </p>

    <div class="actions">
      <router-link to="/index" class="action-link">返回首页</router-link>
    </div>
  </div>
</template>

<style scoped>
.not-found-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 96px); /* 考虑到可能存在的顶部导航栏高度 */
  text-align: center;
}

.error-title {
  font-size: 2.5rem;
  font-weight: bold;
  color: #333;
}

.error-message {
  margin-top: 1rem;
  font-size: 1.2rem;
  line-height: 1.5;
  color: #666;
}

.actions {
  margin-top: 3rem;
}

.action-link {
  display: inline-block;
  margin-right: 1rem;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 1rem;
  color: white;
  background-color: var(--colorPrimary);
  text-decoration: none;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: var(--colorPrimary);
  }
}

.action-link {
  /* 如果项目中已有全局链接样式，可以省略这部分 */
  cursor: pointer;
}
</style>
<script setup lang="ts">
</script>