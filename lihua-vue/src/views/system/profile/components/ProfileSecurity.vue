<template>
  <div style="margin: 8px">
    <a-form :colon="false" layout="vertical" :model="password" :rules="rules" @finish="handleFinish">
      <a-form-item label="旧密码" name="oldPassword">
        <a-input-password class="form-item-width"
                          placeholder="请输入旧密码"
                          v-model:value="password.oldPassword"/>
      </a-form-item>
      <a-form-item label="新密码" name="newPassword">
        <a-input-password class="form-item-width"
                          v-model:value="password.newPassword"
                          placeholder="请输入新密码"
                          @change="handleChange"/>
        <div>
          <a-progress class="form-item-width"
                      style="margin: 0"
                      :showInfo="false"
                      :size="[90,3]"
                      :steps="3"
                      :percent="passwordLevel"
                      :strokeColor="['#ff4d4f','#faad14','#52c41a']"/>
        </div>
      </a-form-item>

      <a-form-item label="确认密码" name="confirmPassword" >
        <a-input-password class="form-item-width"
                          placeholder="请再次输入新密码"
                          v-model:value="password.confirmPassword"/>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">提 交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">

import {reactive, ref} from "vue";
import type {Rule} from "ant-design-vue/es/form";
import {updatePassword} from "@/api/system/profile/profile";
import {message} from "ant-design-vue";
const passwordLevel = ref<number>(0)

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
    { min: 6, max: 22, message: '密码长度6-22位', trigger: 'change' }
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

const handleFinish = async (data: passwordType) => {
  const resp = await updatePassword(data.oldPassword,data.newPassword)

  if (resp.code === 200) {
    message.success("修改成功")
  } else {
    message.warn(resp.msg)
  }
}

</script>
<style scoped>
.form-item-width {
  width: 270px;
}
</style>
