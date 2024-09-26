<template>
  <div>
    <a-input-password v-model:value="password"
                      @change="handleChangePassword"
                      :placeholder="placeholder"
                      :maxlength="30"
                      :style="{height: height}"
    >
      <template #prefix v-if="prefixIcon">
        <LockOutlined class="input-prefix-icon-color"/>
      </template>
    </a-input-password>
    <a-progress style="margin: 0;"
                v-show="showProgress"
                :showInfo="false"
                :size="[size,3]"
                :steps="3"
                :percent="passwordLevel"
                :strokeColor="['#ff4d4f','#faad14','#52c41a']"/>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from "vue";

const {value, placeholder, size = 90, height, prefixIcon = false, showProgress = true} = defineProps<{
  value?: string,
  placeholder?: string,
  size?: number,
  height?: string,
  prefixIcon?: boolean,
  showProgress?: boolean
}>()

const password = ref<string | undefined>(value)
const emits = defineEmits(['update:value'])
const passwordLevel = ref<number>(0)

const handleChangePassword = () => {
  const weakRegex = /^(?=.*[a-zA-Z])[\w!@#$%^&*]{6,}$/;
  const mediumRegex = /^(?=.*\d)(?=.*[a-zA-Z])[\w!@#$%^&*]{8,}$/;
  const strongRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?=.*[\w!@#$%^&*]).{10,}$/;
  const passwordValue = password.value
  if (passwordValue) {
    if (strongRegex.test(passwordValue) ){
      passwordLevel.value = 90
    } else if (mediumRegex.test(passwordValue)){
      passwordLevel.value = 60
    } else if (weakRegex.test(passwordValue)){
      passwordLevel.value = 30
    } else {
      passwordLevel.value = 10
    }
  } else {
    passwordLevel.value = 10
  }


  emits('update:value', passwordValue)
}

onMounted(() => {
  handleChangePassword()
})

watch(() => value, () => {
  password.value = value
  handleChangePassword()
})
</script>

<style scoped>

</style>
