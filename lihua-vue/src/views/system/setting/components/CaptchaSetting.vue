<template>
  <a-card>
    <a-form layout="vertical">
      <a-form-item label="验证码">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              登录或注册等场景下是否需要验证码
            </template>
            <QuestionCircleOutlined class="question-icon"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SysSetting} from "@/api/system/setting/type/SysSetting.ts";
import type {Captcha} from "@/api/system/setting/type/Captcha.ts";
import {message} from "ant-design-vue";
import {isAdmin} from "@/utils/Auth.ts";
import {save} from "@/api/system/setting/Setting.ts";
import {useSettingStore} from "@/stores/setting.ts";

const settingStore = useSettingStore();

const componentName = getCurrentInstance()?.type.__name
// 加载配置，已保存的系统配置中没有当前配置的话会进行创建
const init = async () => {
  if (!componentName) {
    return;
  }

  const settingData = await settingStore.getSettingInfo<Captcha>(componentName);
  if (settingData) {
    settingForm.value = settingData
  }
}

// 默认密码配置表单对象
const settingForm = ref<Captcha>({
  enable: true
})

// 保存到数据库中的对象
const setting = ref<SysSetting>({
  settingKey: componentName,
  json: JSON.stringify(settingForm.value)
})

// 处理保存设置
const handleChangeSwitch = async () => {
  if (!isAdmin()) {
    await init()
    message.error("用户权限不足")
    return
  }

  setting.value.json = JSON.stringify(settingForm.value)
  const resp = await save(setting.value)
  if (resp.code === 200) {
    message.success(resp.msg)
  } else {
    message.error(resp.msg)
  }
}

onMounted(() => init())
</script>