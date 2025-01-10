<template>
  <a-card>
    <a-form layout="vertical" :model="settingForm">
      <a-form-item label="灰色模式">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              所有用户页面设置为灰白配色
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <div v-if="settingForm.enable">
          <a-form-item label="定时关闭">
            <a-date-picker
                :disabledDate="(current: Dayjs) => current && current < dayjs().add(-1, 'day').endOf('day')"
                show-time
                :presets="presets"
                placeholder="请选择关闭时间"
                format="YYYY-MM-DD HH:mm"
                @change="handleChangeDate"
                v-model:value="closeTime"
            ></a-date-picker>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleSubmit" :loading="submitLoading">提 交</a-button>
          </a-form-item>
        </div>
      </transition>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {useSettingStore} from "@/stores/setting.ts";
import {useThemeStore} from "@/stores/theme.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import type {GrayModel} from "@/api/system/setting/type/GrayModel.ts";
import {message} from "ant-design-vue";
import dayjs, {type Dayjs} from "dayjs";
import {isAdmin} from "@/utils/Auth.ts";
const themeStore = useThemeStore();
const settingStore = useSettingStore();
const componentName = getCurrentInstance()?.type.__name
const submitLoading = ref<boolean>(false)
const init = async () => {
  const grayModel = await settingStore.getSetting<GrayModel>(componentName);
  if (!grayModel) {
    await settingStore.save(setting.value)
  } else {
    if (grayModel.closeTime) {
      // 当前时间小于指定关闭时间进行回显
      if (dayjs() < dayjs(grayModel.closeTime)) {
        settingForm.value = grayModel
        closeTime.value = dayjs(settingForm.value.closeTime)
      } else {
        closeTime.value = undefined
      }
    } else {
      settingForm.value = grayModel
    }
  }
}

// 日期选项快捷预设
const presets = ref([
  { label: '三天后', value: dayjs().add(+3, 'd') },
  { label: '下周', value: dayjs().add(+7, 'd') },
  { label: '下个月', value: dayjs().add(+1, 'month') },
]);

// 默认密码配置表单对象
const settingForm = ref<GrayModel>({
  enable: false,
  closeTime: undefined
})

// 双向绑定关闭时间
const closeTime = ref<Dayjs>()

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '灰色模式',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

// 处理开关switch
const handleChangeSwitch = async () => {
  if (!isAdmin()) {
    await init()
    message.error("用户权限不足")
    return
  }
  if (!settingForm.value.enable) {
    settingForm.value.closeTime = undefined
    closeTime.value = undefined
  }
  setting.value.settingJson = JSON.stringify(settingForm.value)
  const resp = await settingStore.save(setting.value)
  if (resp.code === 200) {
    themeStore.enableGrayModel(settingForm.value.enable)
    message.success(resp.msg)
  } else {
    message.error(resp.msg)
  }
}

// 为日期赋值
const handleChangeDate = (data?: Dayjs) => {
  settingForm.value.closeTime = data?.toDate()
}

// 处理选择日期时间
const handleSubmit = async () => {
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
