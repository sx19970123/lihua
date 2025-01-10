<template>
  <a-card>
    <a-form layout="vertical" :model="settingForm" @finish="handleFinish" :rules="rules">
      <a-form-item label="定期修改密码">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              设置系统用户多长时间需要修改密码
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <div v-if="settingForm.enable">
          <a-form-item label="周期" name="interval">
            <a-input-number style="width: 150px"
                            :precision="0"
                            :min="1"
                            placeholder="请输入"
                            v-model:value="settingForm.interval">
              <template #addonAfter>
                <a-select style="width: 60px" v-model:value="settingForm.unit">
                  <a-select-option value="day">天</a-select-option>
                  <a-select-option value="week">周</a-select-option>
                  <a-select-option value="month">月</a-select-option>
                  <a-select-option value="year">年</a-select-option>
                </a-select>
              </template>
            </a-input-number>
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
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import {useSettingStore} from "@/stores/setting.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {IntervalUpdatePassword} from "@/api/system/setting/type/IntervalUpdatePassword.ts";
import type {Rule} from "ant-design-vue/es/form";
import {message} from "ant-design-vue";
import {useThemeStore} from "@/stores/theme.ts";
import {isAdmin} from "@/utils/Auth.ts";
const themeStore = useThemeStore()
const componentName = getCurrentInstance()?.type.__name
const settingStore = useSettingStore();
const submitLoading = ref<boolean>(false);
const init = async () => {
  const resp = await settingStore.getSetting<IntervalUpdatePassword>(componentName);
  if (!resp) {
    await settingStore.save(setting.value)
  } else {
    settingForm.value = resp
  }
}

// 定期修改密码表单
const settingForm = ref<IntervalUpdatePassword>({
  enable: false,
  unit: 'month'
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '定期修改密码',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

// 表单验证
const rules: Record<string, Rule[]> = {
  interval: [
    { required: true,message: "请输入修改密码周期",trigger: 'change'},
  ]
}

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
    unit: 'month'
  }
  await handleFinish()
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
.form-item-width {
  width: 270px;
}
</style>
