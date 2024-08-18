<template>
  <div>
    <login-setting/>
<!--<a-button @click="prev">上一步</a-button>-->
<!--<a-button @click="next">下一步</a-button>-->
    <a-flex vertical :gap="16" v-if="false">
      <a-flex :gap="16" vertical v-show="true">
        <card-show cardKey="s1"
                   style="width: 100%"
                   :auto-complete="false"
                   :is-complete="middleComplete"
                   @card-click="handleClick"
                   :hover-scale="1.01"
                   :expanded-width="1000"
                   :middle-style="{'background-color':'rgba(255,255,255,0.3)','backdrop-filter':' blur(20px)','border-radius': '8px'}"
        >
          <template #overview>
            <a-card  style="border: none;background-color: rgba(255,255,255,0.3); backdrop-filter: blur(20px)">
              <a-typography-title>HELLO WORLD</a-typography-title>
            </a-card>
          </template>
          <template #detail>
            <a-card class="scrollbar" id="test" style="border: none;background-color: rgba(255,255,255,0.3); backdrop-filter: blur(20px)">
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
      <a-row v-show="true" :gutter="16">
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
  </div>

</template>

<script setup lang="ts">
import CardShow from '@/components/card-show/index.vue'
import {ref} from "vue";
import LoginSetting from "@/components/login-setting/index.vue";
const middleComplete = ref<boolean>(false)
const handleClick = (key:string,show:boolean) => {
  setTimeout(() => {
    middleComplete.value = true
  },1000)
}

const carouselRef = ref()

const next = () => {
  carouselRef.value.next()
}
const prev = () => {
  carouselRef.value.prev()
}
</script>

<style>

</style>
