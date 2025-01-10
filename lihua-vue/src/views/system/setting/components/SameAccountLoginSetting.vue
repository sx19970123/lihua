<template>
  <a-card>
    <a-form layout="vertical" :model="settingForm" @finish="handleFinish" :rules="rules">
      <a-form-item label="同账号登录限制">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              同一账号最多允许 X 人同时登录，超出后先登录的用户将被强制下线
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <div v-if="settingForm.enable">
          <a-form-item label="最大同时在线数" name="maximum">
            <a-input-number :precision="0"
                            :min="1"
                            placeholder="请输入"
                            v-model:value="settingForm.maximum"
            />
          </a-form-item>
          <a-form-item>
            <a-button type="primary" html-type="submit" :loading="submitLoading">提 交</a-button>
          </a-form-item>
        </div>
      </transition>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {useSettingStore} from "@/stores/setting.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SameAccountLoginSetting} from "@/api/system/setting/type/SameAccountLoginSetting.ts";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import {useThemeStore} from "@/stores/theme.ts";
import {message} from "ant-design-vue";
import type {Rule} from "ant-design-vue/es/form";
import {isAdmin} from "@/utils/Auth.ts";
const themeStore = useThemeStore()
const settingStore = useSettingStore();
const componentName = getCurrentInstance()?.type.__name
const submitLoading = ref<boolean>(false);

const init = async () => {
  const resp = await settingStore.getSetting<SameAccountLoginSetting>(componentName);
  if (!resp) {
    await settingStore.save(setting.value)
  } else {
    settingForm.value = resp
  }
}

// 定期修改密码表单
const settingForm = ref<SameAccountLoginSetting>({
  enable: false,
  maximum: 1
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '同账号登录限制',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})


// 处理改变switch开关
const handleChangeSwitch = async () => {
  if (!isAdmin()) {
    await init()
    message.error("用户权限不足")
    return
  }

  // 为 ture 则返回，关闭时才发送请求
  if (settingForm.value.enable) {
    return;
  }

  settingForm.value = {
    enable: false,
    maximum: 1
  }
  await handleFinish()
}

// 表单验证
const rules: Record<string, Rule[]> = {
  maximum: [
    { required: true,message: "请输入最小登录数",trigger: 'change'}
  ]
}

// 处理保存设置
const handleFinish = async () => {
  submitLoading.value = true
  setting.value.settingJson = JSON.stringify(settingForm.value)
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

onMounted(() => init())
</script>

<style scoped>

</style>
