<template>
  <login-setting-base-component title="安全"
                                description="为了确保您的账户安全，请修改密码"
                                icon="LockOutlined"
                                skip-msg="可在个人中心 - 安全设置进行设置"
                                :skip="false"
                                @next="handleNext"
                                @skip="handleSkip"
                                @back="emits('back')"
  >
    <template #content>
      <div style="width: 280px">
        <a-form :colon="false" ref="resetPasswordRef" :model="password" :rules="rules">
          <a-form-item name="oldPassword">
            <a-input-password
                placeholder="请输入旧密码"
                v-model:value="password.oldPassword"
            />
          </a-form-item>
          <a-form-item name="newPassword">
            <password-input class="form-item-width"
                            v-model:value="password.newPassword"
                            placeholder="请输入新密码" :size="92"
                            :show-progress="!!password.newPassword && password.newPassword.length >= 6 && password.newPassword.length <= 30"
            />
          </a-form-item>
          <a-form-item name="confirmPassword" >
            <a-input-password class="form-item-width"
                              placeholder="请再次输入新密码"
                              v-model:value="password.confirmPassword"
            />
          </a-form-item>
        </a-form>
      </div>
    </template>
  </login-setting-base-component>
</template>

<script setup lang="ts">
import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import {type Ref, useTemplateRef} from "vue";

import {reactive} from "vue";
import type {Rule} from "ant-design-vue/es/form";
import {useUserStore} from "@/stores/user.ts";
import {type FormInstance, message} from "ant-design-vue";
import PasswordInput from "@/components/password-input/index.vue";
import {ResponseError} from "@/api/global/Type.ts";
const resetPasswordRef = useTemplateRef<FormInstance>("resetPasswordRef")
const userStore = useUserStore()
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
 */
const equalToPassword = async (rule: any, value: string) => {
  if (password.newPassword !== value) {
    return Promise.reject('两次输入的密码不一致')
  } else {
    return Promise.resolve();
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
    { min: 6, max: 30, message: '密码长度6-30位', trigger: 'change' },
  ],
  confirmPassword: [
    { required: true,message: "请再次输入密码",trigger: 'change'},
    { required: true, validator: equalToPassword, trigger: "change" }
  ]
}


// 修改密码后进入下一步
const handleNext = async (loading:Ref<boolean>) => {
  await resetPasswordRef.value?.validate()
  loading.value = true
  try {
    const resp = await userStore.updatePassword(password.oldPassword,password.newPassword,password.confirmPassword)
    loading.value = false
    if (resp.code === 200) {
      emits('next', loading.value)
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  } finally {
    loading.value = false
  }

}
// 跳过
const handleSkip = (loading:Ref<boolean>) => {
  loading.value = false
  emits('skip', loading.value)
}
</script>
