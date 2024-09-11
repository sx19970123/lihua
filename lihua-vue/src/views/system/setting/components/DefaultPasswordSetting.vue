<template>
  <div style="margin: 8px">
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
        <a-input-password placeholder="请输入默认密码"
                          v-model:value="settingForm.defaultPassword"
                          @change="handleChange"
        ></a-input-password>
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
      <a-form-item>
        <a-button type="primary" html-type="submit">提 交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {useSettingStore} from "@/stores/modules/setting.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import type {Rule} from "ant-design-vue/es/form";
import {message} from "ant-design-vue";
import type {DefaultPassword} from "@/api/system/setting/type/DefaultPassword.ts";
const settingStore = useSettingStore();
const componentName = getCurrentInstance()?.type.__name

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
const settingForm = ref<DefaultPassword>({
  defaultPassword: ''
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '默认密码',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

// 密码强度级别
const passwordLevel = ref<number>(0)

// 处理显示密码强度
const handleChange = () => {
  const weakRegex = /^(?=.*[a-zA-Z])[\w!@#$%^&*]{6,}$/;
  const mediumRegex = /^(?=.*\d)(?=.*[a-zA-Z])[\w!@#$%^&*]{8,}$/;
  const strongRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?=.*[\w!@#$%^&*]).{10,}$/;
  const password = settingForm.value.defaultPassword
  if (password) {
    if (weakRegex.test(password)){
      passwordLevel.value = 30
    }
    if (mediumRegex.test(password)){
      passwordLevel.value = 60
    }
    if (strongRegex.test(password)){
      passwordLevel.value = 90
    }
  } else {
    passwordLevel.value = 0
  }
}

const rules: Record<string, Rule[]> = {
  defaultPassword: [
    { required: true,message: "请输入默认密码",trigger: 'change'},
    { min: 6, max: 30, message: '密码长度6-30位', trigger: 'change' }
  ]
}

const handleFinish = async () => {
  setting.value.settingJson = JSON.stringify(settingForm.value)
  const resp = await settingStore.save(setting.value)

  if (resp.code === 200) {
      message.success(resp.msg)
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
