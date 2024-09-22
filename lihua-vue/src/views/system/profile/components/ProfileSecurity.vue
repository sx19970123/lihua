<template>
  <div style="margin: 8px">
    <a-form :colon="false" layout="vertical" :model="password" :rules="rules" @finish="handleFinish">
      <a-form-item label="旧密码" name="oldPassword">
        <a-input-password class="form-item-width"
                          placeholder="请输入旧密码"
                          v-model:value="password.oldPassword"/>
      </a-form-item>
      <a-form-item label="新密码" name="newPassword">
        <password-input class="form-item-width" v-model:value="password.newPassword" placeholder="请输入新密码" :size="90"/>
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

import {reactive} from "vue";
import type {Rule} from "ant-design-vue/es/form";
import {updatePassword} from "@/api/system/profile/Profile.ts";
import {message} from "ant-design-vue";
import PasswordInput from "@/components/password-input/index.vue";

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
