<template>
  <a-card>
    <a-form layout="vertical">
      <a-form-item label="验证码">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              登陆或注册等场景下是否需要验证码
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {useSettingStore} from "@/stores/setting.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import type {VerificationCode} from "@/api/system/setting/type/VerificationCode.ts";
import {message} from "ant-design-vue";
import {isAdmin} from "@/utils/Auth.ts";
const componentName = getCurrentInstance()?.type.__name
const settingStore = useSettingStore();
// 加载配置，已保存的系统配置中没有当前配置的话会进行创建
const init = async () => {
  const resp = await settingStore.getSetting<VerificationCode>(componentName);
  if (!resp) {
    await settingStore.save(setting.value)
  } else {
    settingForm.value = resp
  }
}


// 默认密码配置表单对象
const settingForm = ref<VerificationCode>({
  enable: true
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '验证码',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

// 处理保存设置
const handleChangeSwitch = async () => {
  if (!isAdmin()) {
    await init()
    message.error("用户权限不足")
    return
  }

  setting.value.settingJson = JSON.stringify(settingForm.value)
  const resp = await settingStore.save(setting.value)
  if (resp.code === 200) {
    message.success(resp.msg)
  } else {
    message.error(resp.msg)
  }
}

onMounted(() => init())
</script>
