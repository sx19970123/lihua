<template>
  <a-flex vertical :gap="16">

    <user-select :bordered="false" :box-shadow="false" />

    <a-flex :gap="16" vertical v-show="false">
      <card-show cardKey="s1"
                 style="width: 100%"
                 :auto-complete="false"
                 :is-complete="middleComplete"
                 @card-click="handleClick"
                 :hover-scale="1.01"
                 :expanded-width="1000"
                 :middle-style="{'background-image':'linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)','border-radius': '8px'}"
      >
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <a-flex vertical justify="center" :gap="8">
            <component is="Sun" style="font-size: 70px;"/>
            <a-typography-text style="margin: auto">加载中</a-typography-text>
          </a-flex>
        </template>
        <template #detail>
          <a-card class="scrollbar" id="test" style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);">
            <a-typography-title>HELLO WORLD</a-typography-title>
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
    <a-row v-show="false" :gutter="16">
      <a-col :span="6"> <card-show cardKey="s2"
                                   style="width: 100%"
                                   :auto-complete="false"
                                   :is-complete="middleComplete"
                                   @card-click="handleClick"
                                   :hover-scale="1.01"
                                   :expanded-width="600"
                                   :middle-style="{'background-image':'linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)','background-size': 'contain','border-radius':' 8px'}"
      >
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <a-flex vertical justify="center" :gap="8">
            <component is="Sun" style="font-size: 70px;"/>
            <a-typography-text style="margin: auto">加载中</a-typography-text>
          </a-flex>

        </template>
        <template #detail>
          <a-card class="scrollbar" id="test" style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);height: 300px">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show></a-col>
      <a-col :span="6"> <card-show cardKey="s3"
                                   style="width: 100%"
                                   :auto-complete="false"
                                   :is-complete="middleComplete"
                                   @card-click="handleClick"
                                   :hover-scale="1.01"
                                   :expanded-width="600"
                                   :middle-style="{'background-image':'linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)','background-size': 'contain','border-radius':' 8px'}"
      >
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <a-flex vertical justify="center" :gap="8">
            <component is="Sun" style="font-size: 70px;"/>
            <a-typography-text style="margin: auto">加载中</a-typography-text>
          </a-flex>

        </template>
        <template #detail>
          <a-card class="scrollbar" id="test" style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);height: 300px;">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show></a-col>
      <a-col :span="6"> <card-show cardKey="s4"
                                   style="width: 100%"
                                   :auto-complete="false"
                                   :is-complete="middleComplete"
                                   @card-click="handleClick"
                                   :hover-scale="1.01"
                                   :expanded-width="600"
                                   :middle-style="{'background-image':'linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)','border-radius':' 8px'}"
      >
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <a-flex vertical justify="center" :gap="8">
            <component is="Sun" style="font-size: 70px;"/>
            <a-typography-text style="margin: auto">加载中</a-typography-text>
          </a-flex>

        </template>
        <template #detail>
          <a-card class="scrollbar" id="test" style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);height: 300px">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show></a-col>
      <a-col :span="6"> <card-show cardKey="s5"
                                   style="width: 100%"
                                   :auto-complete="false"
                                   :is-complete="middleComplete"
                                   @card-click="handleClick"
                                   :hover-scale="1.01"
                                   :expanded-width="600"
                                   :middle-style="{'background-image':'linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%)','border-radius':' 8px'}"
      >
        <template #overview>
          <a-card style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);">
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
        <template #middle>
          <a-flex vertical justify="center" :gap="8">
            <component is="Sun" style="font-size: 70px;"/>
            <a-typography-text style="margin: auto">加载中</a-typography-text>
          </a-flex>

        </template>
        <template #detail>
          <a-card class="scrollbar" id="test" style="border: none;background-image:linear-gradient(135deg,#C2FFD8 10%,#465EFB 100%);height: 300px;">
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
            <a-typography-title>HELLO WORLD</a-typography-title>
          </a-card>
        </template>
      </card-show></a-col>
    </a-row >
  </a-flex>
</template>

<script setup lang="ts">
import CardShow from '@/components/card-show/index.vue'
import {ref} from "vue";
import {handleSseMessage} from "@/utils/ServerSentEvents.ts";
import UserSelect from "@/components/user-select/index.vue"
handleSseMessage((msg: string) => {
  console.log("msg==", msg)
})
const middleComplete = ref<boolean>(false)
const handleClick = (key:string,show:boolean) => {
  setTimeout(() => {
    middleComplete.value = true
  },1000)
}
</script>

<style>

</style>
