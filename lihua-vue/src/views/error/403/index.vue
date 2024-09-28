<template>
  <a-card>
    <div class="error-container">
      <component is="XiaoMiaoHappy" style="font-size: 96px"/>
      <a-typography-title>403 Forbidden Access</a-typography-title>
      <a-typography-text type="secondary">您没有权限访问此页面</a-typography-text>
    </div>
  </a-card>
</template>
<style scoped>
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 50px);
  text-align: center;
}
</style>
