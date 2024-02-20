<template>
  <div>
    <a-avatar class="modify" :size="props.size" :style="{background: color}" :src="type === 'image' ? path : ''">
      <template v-if="type === 'icon'" #icon>
        <component v-if="icon" :is="avatarIcon"/>
      </template>
      <template v-if="type === 'text'">
        {{text}}
      </template>
    </a-avatar>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

const props = defineProps({
  value: {
    type: String
  },
  size: {
    type: Number,
    default: 32
  }
})
const {type,color,path,text,icon} = ref<{
  type: string,
  color: string,
  path: string,
  text: string,
  icon: string
}>(JSON.parse(props.value))
</script>

<style scoped>

</style>