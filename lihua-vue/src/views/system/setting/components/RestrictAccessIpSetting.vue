<template>
  <a-card>
    <a-form
        layout="vertical"
        @finish="handleFinish"
        :model="settingForm"
    >
      <a-form-item label="限制访问IP">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              配置禁止访问ip地址，支持 ? * 通配符
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <div v-if="settingForm.enable">
          <div class="scrollbar" style="max-height: 400px; display: inline-block">
            <a-form-item
                :label="index === 0 ? 'ip地址' : ''"
                :key="index"
                v-for="(ipItem, index) in settingForm.ipList"
                :name="['ipList', index]"
                :rules="[{
                required: true,
                message: '请输入ip地址',
                trigger: ['change', 'blur'],
            },{
              pattern:  /^[0-9.?*]+$/,
              message: '请输入正确的ip地址',
              trigger: ['change', 'blur'],
            }]"
            >
              <a-input class="form-item-width" placeholder="请输入ip地址" v-model:value="settingForm.ipList[index]" allow-clear/>
              <a-button style="margin-left: 8px; margin-right: 8px"
                        danger
                        v-if="settingForm?.ipList.length > 1"
                        @click="handleRemoveIpItem(index)"
              >
                <template #icon>
                  <DeleteOutlined />
                </template>
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="dashed" class="form-item-width" @click="handleAddIpItem">
                <template #icon>
                  <PlusOutlined />
                </template>
                添加IP地址
              </a-button>
            </a-form-item>
          </div>
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
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import type {RestrictAccessIp} from "@/api/system/setting/type/RestrictAccessIp.ts";
import {message} from "ant-design-vue";
import {useThemeStore} from "@/stores/theme.ts";
import {isAdmin} from "@/utils/Auth.ts";
const themeStore = useThemeStore()
const settingStore = useSettingStore();
const componentName = getCurrentInstance()?.type.__name
const submitLoading = ref<boolean>(false);

// 加载配置，已保存的系统配置中没有当前配置的话会进行创建
const init = async () => {
  const resp = await settingStore.getSetting<RestrictAccessIp>(componentName);
  if (!resp) {
    await settingStore.save(setting.value)
  } else {
    settingForm.value = resp
  }
}

// 默认密码配置表单对象
const settingForm = ref<RestrictAccessIp>({
  enable: false,
  ipList: ['']
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '限制访问IP',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

// 添加ip
const handleAddIpItem = () => [
  settingForm.value.ipList.push('')
]

// 删除ip
const handleRemoveIpItem = (index: number) => {
  settingForm.value.ipList.splice(index, 1)
}

// 提交配置信息
const handleFinish = async () => {
  submitLoading.value = true
  // ip 去重
  const ipList = settingForm.value.ipList
  const ipSet = new Set(ipList)
  let flag = false
  if (ipSet.size !== ipList.length) {
    flag = true
  }
  settingForm.value.ipList = [... ipSet]
  setting.value.settingJson = JSON.stringify(settingForm.value)
  try {
    const resp = await settingStore.save(setting.value)
    if (resp.code === 200) {
      if (flag){
        message.warn("已合并重复ip")
      }
      message.success(resp.msg)
      init()
    } else {
      message.error(resp.msg)
    }
  } finally {
    submitLoading.value = false
  }
}

// 关闭ip限制保存配置
const handleChangeSwitch = async () => {
  if (!isAdmin()) {
    await init()
    message.error("用户权限不足")
    return
  }

  if (settingForm.value.enable) {
    return;
  }

  settingForm.value = {
    enable: false,
    ipList: ['']
  }

  await handleFinish()
}

// 页面加载完成后调用
onMounted(() => init())
</script>

<style scoped>
.form-item-width {
  width: 270px;
}
</style>
