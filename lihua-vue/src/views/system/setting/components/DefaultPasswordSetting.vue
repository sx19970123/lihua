<template>
  <a-card>
    <a-form layout="vertical" :model="settingForm" :rules="rules" @finish="handleFinish">
      <a-form-item class="form-item-width" label="默认密码" name="defaultPassword">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              用户管理模块新增用户时的默认密码
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
          </a-tooltip>
        </template>
        <password-input class="form-item-width" v-model:value="settingForm.defaultPassword" placeholder="请输入默认密码" :size="90"/>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" :loading="submitLoading">提 交</a-button>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {useSettingStore} from "@/stores/setting.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import type {Rule} from "ant-design-vue/es/form";
import {message} from "ant-design-vue";
import type {DefaultPassword} from "@/api/system/setting/type/DefaultPassword.ts";
import PasswordInput from "@/components/password-input/index.vue";
const settingStore = useSettingStore();
const componentName = getCurrentInstance()?.type.__name
import {cloneDeep} from 'lodash-es'
import {defaultPasswordDecrypt, defaultPasswordEncrypt} from "@/utils/Crypto.ts";
const submitLoading = ref<boolean>(false);
// 加载配置，已保存的系统配置中没有当前配置的话会进行创建
const init = async () => {
  const resp = await settingStore.getSetting<DefaultPassword>(componentName);
  if (!resp) {
    await settingStore.save(setting.value)
  } else {
    resp.defaultPassword = defaultPasswordDecrypt(resp.defaultPassword)
    settingForm.value = resp
  }
}

// 默认密码配置表单对象
const settingForm = ref<DefaultPassword>({
  defaultPassword: ''
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '默认密码',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

const rules: Record<string, Rule[]> = {
  defaultPassword: [
    { required: true,message: "请输入默认密码",trigger: 'change'},
    { min: 6, max: 30, message: '密码长度6-30位', trigger: 'change' }
  ]
}

const handleFinish = async () => {
  submitLoading.value = true
  // 对默认密码进行加密处理
  const defaultPasswordForm: DefaultPassword = cloneDeep(settingForm.value)
  defaultPasswordForm.defaultPassword = defaultPasswordEncrypt(defaultPasswordForm.defaultPassword)
  setting.value.settingJson = JSON.stringify(defaultPasswordForm)
  try {
    const resp = await settingStore.save(setting.value)

    if (resp.code === 200) {
      message.success(resp.msg)
    } else {
      message.error(resp.msg)
    }
  } finally {
    submitLoading.value = false
  }
}

// 页面加载完成后调用
onMounted(() => init())
</script>

<style scoped>
.form-item-width {
  width: 270px;
}
</style>
