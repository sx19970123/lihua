<template>
  <login-setting-base-component title="安全"
                                description="为了确保您的账户安全，请修改密码"
                                icon="LockOutlined"
                                skip-msg="可在个人中心 - 安全设置进行设置"
                                @next="handleNext"
                                @skip="handleSkip"
                                @back="emits('back')"
  >
    <template #content>
      <a-form :colon="false" ref="resetPasswordRef" :model="password" :rules="rules">
        <a-form-item name="oldPassword">
          <a-input-password placeholder="请输入旧密码"
                            v-model:value="password.oldPassword"
                            />
        </a-form-item>
        <a-form-item name="newPassword">
          <a-input-password v-model:value="password.newPassword"
                            placeholder="请输入新密码"
                            @change="handleChange"
                            />
          <div>
            <a-progress style="margin: 0"
                        :showInfo="false"
                        :size="[90,3]"
                        :steps="3"
                        :percent="passwordLevel"
                        :strokeColor="['#ff4d4f','#faad14','#52c41a']"/>
          </div>
        </a-form-item>
        <a-form-item name="confirmPassword" >
          <a-input-password class="form-item-width"
                            placeholder="请再次输入新密码"
                            v-model:value="password.confirmPassword"
                         />
        </a-form-item>
      </a-form>
    </template>
  </login-setting-base-component>
</template>

<script setup lang="ts">
import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import {type Ref, useTemplateRef} from "vue";

import {reactive, ref} from "vue";
import type {Rule} from "ant-design-vue/es/form";
import {updatePassword} from "@/api/system/profile/Profile.ts";
import {type FormInstance, message} from "ant-design-vue";
const passwordLevel = ref<number>(0)
const resetPasswordRef = useTemplateRef<FormInstance>("resetPasswordRef")
// 向外抛出函数
const emits = defineEmits(['back', 'skip', 'next'])

type passwordType = {
  oldPassword: string,
  newPassword: string,
  confirmPassword: string
}
const password = reactive<passwordType>({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

/**
 * 判断第二次密码输入是否正确
 * @param rule
 * @param value
 * @param callback
 */
const equalToPassword = (rule: any, value: string, callback:Function) => {
  if (password.newPassword !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
}

/**
 * 密码校验
 */
const rules: Record<string, Rule[]> = {
  oldPassword: [
    { required: true,message: "请输入旧密码",trigger: 'change'},
  ],
  newPassword: [
    { required: true,message: "请输入新密码",trigger: 'change'},
    { min: 6, max: 30, message: '密码长度6-30位', trigger: 'change' }
  ],
  confirmPassword: [
    { required: true,message: "请再次输入密码",trigger: 'change'},
    { required: true, validator: equalToPassword, trigger: "change" }
  ]
}

/**
 * 处理密码强度提示
 */
const handleChange = () => {
  const weakRegex = /^(?=.*[a-zA-Z])[\w!@#$%^&*]{6,}$/;
  const mediumRegex = /^(?=.*\d)(?=.*[a-zA-Z])[\w!@#$%^&*]{8,}$/;
  const strongRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?=.*[\w!@#$%^&*]).{10,}$/;
  const newPassword = password.newPassword
  if (newPassword) {
    if (weakRegex.test(newPassword)){
      passwordLevel.value = 30
    }
    if (mediumRegex.test(newPassword)){
      passwordLevel.value = 60
    }
    if (strongRegex.test(newPassword)){
      passwordLevel.value = 90
    }
  } else {
    passwordLevel.value = 0
  }
}

// 修改密码后进入下一步
const handleNext = async (loading:Ref<boolean>) => {
  await resetPasswordRef.value?.validate()
  loading.value = true
  const resp = await updatePassword(password.oldPassword,password.newPassword)
  if (resp.code === 200) {
    loading.value = false
    emits('next', loading.value)
  } else {
    message.warn(resp.msg)
    loading.value = false
  }
}
// 跳过
const handleSkip = (loading:Ref<boolean>) => {
  loading.value = false
  emits('skip', loading.value)
}
</script>

<style scoped>

</style>