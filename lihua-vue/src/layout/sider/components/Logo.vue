<template>
  <div class="logo">
    <a-flex gap="middle" align="center" justify="center">
      <div>
        <a-avatar :size="32">
          <template #icon>
            <AntDesignOutlined />
          </template>
        </a-avatar>
      </div>
      <div class="title" v-if="false">
        <span>title</span>
      </div>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import { AntDesignOutlined } from '@ant-design/icons-vue';
// todo 通过 pinia 进行 logo 状态控制

</script>

<style scoped>
.logo {
  height: 48px;
  padding: 16px;
  color: azure;
  .title {
    font-size: 18px;
  }
}
</style>
