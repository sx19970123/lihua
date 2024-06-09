<template>
  <div>
    <a-flex :gap="16">
      <card-show cardKey="s1"
                 style="width: 100%"
                 :auto-complete="false"
                 :middle-complete="middleComplete"
                 @card-click="handleClick"
                 :hover-scale="1.01"
                 :expanded-height="300"
                 :expanded-width="1000"
      >
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <div style="display: flex; height: 100%;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);border-radius: 8px">
            <component is="Sun" style="font-size: 70px;margin: auto"/>
          </div>
        </template>
        <template #detail>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show>
    </a-flex>
    <a-flex :gap="16" style="margin-top: 16px">
      <card-show cardKey="s2">
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <div style="background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)"></div>
        </template>
        <template #detail>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show>
      <card-show cardKey="s3">
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <div style="background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)"></div>
        </template>
        <template #detail>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show>
      <card-show cardKey="s4">
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <div style="background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)"></div>
        </template>
        <template #detail>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show>
      <card-show cardKey="s5">
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <div style="background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)"></div>
        </template>
        <template #detail>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show>
      <card-show cardKey="s6">
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <div style="background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)"></div>
        </template>
        <template #detail>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import CardShow from '@/components/card-show/index.vue'
import {ref} from "vue";

const middleComplete = ref<boolean>(false)

const handleClick = () => {
  middleComplete.value = false
  setTimeout(() => {
    middleComplete.value = true
  },1000)
}
</script>

<style>

</style>
