<template>
  <div>
    <a-row :gutter="16">
      <a-col :span="6">
        <card-show cardKey="s1">
          <template #overview>
            <a-card :body-style="{borderBottom: 'none'}">
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
          <template #detail>
<!--            <a-card>-->
<!--              <a-typography-title>HELLO WORLD</a-typography-title>-->
<!--              <a-typography-title>HELLO WORLD</a-typography-title>-->
<!--              <a-typography-title>HELLO WORLD</a-typography-title>-->
<!--              <a-typography-title>HELLO WORLD</a-typography-title>-->
<!--              <a-typography-title>HELLO WORLD</a-typography-title>-->
<!--            </a-card>-->
          </template>
        </card-show>
      </a-col>
      <a-col  :span="6">
        <card-show cardKey="s2">
          <template #overview>
            <a-card :body-style="{borderBottom: 'none'}">
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
          <template #detail>
            <a-card :body-style="{borderBottom: 'none'}">
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
        </card-show>
      </a-col>
      <a-col  :span="6">
        <card-show cardKey="s3">
          <template #overview>
            <a-card>
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
          <template #detail>
            <a-card>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
        </card-show>
      </a-col>
      <a-col  :span="6">
        <card-show cardKey="s4">
          <template #overview>
            <a-card>
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
          <template #detail>
            <a-card>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
        </card-show>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import CardShow from '@/components/card-show/index.vue'
</script>

<style>

</style>
