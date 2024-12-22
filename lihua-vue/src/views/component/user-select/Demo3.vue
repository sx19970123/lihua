<template>
  <div>
    <a-typography-title :level="4">自定义宽高</a-typography-title>
    <a-typography-text>绑定id：{{value}}</a-typography-text><br/>
    <a-typography-text>绑定nickname：{{nickname}}</a-typography-text><br/>
    <a-typography-text>绑定username：{{username}}</a-typography-text>
    <user-select v-model:id="value"
                 v-model:nickname="nickname"
                 v-model:username="username"
                 :all-dept-data="false"
                 :width="1000"
                 :height="200"
    />
  </div>
</template>

<script setup lang="ts">
import UserSelect from "@/components/user-select/index.vue"
import {ref} from "vue";
const value = ref<string[]>([])
const nickname = ref<string[]>([])
const username = ref<string[]>([])
</script>
