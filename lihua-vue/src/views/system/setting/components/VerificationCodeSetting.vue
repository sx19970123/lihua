<template>
  <div style="margin: 8px">
    <a-form layout="vertical">
      <a-form-item label="登录验证码">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              登陆时是否需要验证码
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {useSettingStore} from "@/stores/modules/setting.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import type {VerificationCode} from "@/api/system/setting/type/VerificationCode.ts";
import {message} from "ant-design-vue";
const componentName = getCurrentInstance()?.type.__name
const settingStore = useSettingStore();
// 加载配置，已保存的系统配置中没有当前配置的话会进行创建
const init = () => {
  const settings = settingStore.settings
  const targetSetting = settings.filter(item => item.settingComponentName === componentName) as SystemSetting[]
  if (componentName && targetSetting.length === 0) {
    settingStore.save(setting.value)
  } else {
    settingForm.value = JSON.parse(targetSetting[0].settingJson)
  }
}


// 默认密码配置表单对象
const settingForm = ref<VerificationCode>({
  enable: true
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '登录验证码',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

// 处理保存设置
const handleChangeSwitch = async () => {
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

<style scoped>

</style>
