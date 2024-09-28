<template>
  <a-card>
    <div class="error-container">
      <component is="XiaoHeiFierce" style="font-size: 96px"/>
      <a-typography-title>Service to the area is temporarily unavailable</a-typography-title>
      <a-typography-text type="secondary">暂时无法为该地区提供服务</a-typography-text>
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
