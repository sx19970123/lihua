<template>
  <a-card>
    <a-form :colon="false" :model="formData" :rules="rules" layout="vertical" @finish="handleSaveLockInfo">
      <a-form-item class="form-item-width" label="自动锁屏" name="defaultPassword">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              一段时间未操作将自动锁屏
            </template>
            <QuestionCircleOutlined style="margin-left: var(--lihua-space-xs)"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="formData.autoLock"></a-switch>
      </a-form-item>
      <a-form-item label="时长（分钟）" v-if="formData.autoLock">
        <a-input-number v-model:value="formData.timeout" :min="1" :precision="0" class="form-item-width" placeholder="请输入自动锁屏时长"/>
      </a-form-item>
      <a-form-item label="锁屏密码" name="password">
        <a-input-password v-model:value="formData.password" class="form-item-width" placeholder="请输入锁屏密码"/>
      </a-form-item>
      <a-form-item label="确认密码" name="confirmPassword" >
        <a-input-password v-model:value="formData.confirmPassword" class="form-item-width" placeholder="请再次输入密码"/>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">提 交</a-button>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
// 锁屏表单类型
import {onMounted, ref} from "vue";
import type {Rule} from "ant-design-vue/es/form";
import {useUserStore} from "@/stores/user.ts";
import {message} from "ant-design-vue";
import {getLockScreenInfo, setLockScreenInfo} from "@/utils/LockScreenUtils.ts";

const userStore = useUserStore();

type LockScreenType = {
  // 自动锁定
  autoLock?: boolean,
  // 自动锁定时间
  timeout?: number,
  // 锁屏密码
  password?: string,
  // 确认密码
  confirmPassword?: string
}

const formData = ref<LockScreenType>({autoLock: false, timeout: 10})


/**
 * 判断第二次密码输入是否正确
 * @param rule
 * @param value
 */
const equalToPassword = async (rule: any, value: string) => {
  if (formData.value.password !== value) {
    return Promise.reject('两次输入的密码不一致')
  } else {
    return Promise.resolve();
  }
}

/**
 * 表单
 */
const rules: Record<string, Rule[]> = {
  timeout: [
    { required: true,message: "请输入超时时间",trigger: 'change'},
  ],
  password: [
    { required: true,message: "请输入锁屏密码",trigger: 'change'}
  ],
  confirmPassword: [
    { required: true,message: "请再次输入密码",trigger: 'change'},
    { required: true, validator: equalToPassword, trigger: "change" }
  ]
}

/**
 * 保存数据
 */
const handleSaveLockInfo = () => {
  const userId = userStore.userId
  if (!userId) {
    message.error("请先登录")
    window.location.reload()
    return
  }

  const {autoLock, password, timeout} = formData.value
  if (autoLock != undefined && password) {
    setLockScreenInfo(autoLock, password, timeout)
    message.success("保存成功")
  }
}


onMounted(() => {
  // 数据回显
  const {autoLock, timeout, password} = getLockScreenInfo() || {autoLock: false, timeout: 0};

  formData.value = {
    autoLock,
    timeout,
    password,
    confirmPassword: password
  }
})

</script>

<style scoped>
.form-item-width {
  width: 270px;
}
</style>
