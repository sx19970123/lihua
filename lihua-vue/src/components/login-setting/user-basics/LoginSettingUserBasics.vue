<template>
  <login-setting-base-component
      title="基础信息"
      icon="UserOutlined"
      description="新用户首次登录需要录入基础信息"
      :skip="false"
      @next="handleNext"
      @back="emits('back')"
  >
    <template #content>
      <a-form ref="userBasicRef" :model="profileInfo" :rules="userRoles">
        <a-form-item name="nickname">
          <a-input class="form-item-width" placeholder="请输入用户昵称" v-model:value="profileInfo.nickname" allow-clear></a-input>
        </a-form-item>
        <a-form-item name="phoneNumber">
          <a-input class="form-item-width" placeholder="请输入手机号码（非必填）" v-model:value="profileInfo.phoneNumber" allow-clear></a-input>
        </a-form-item>
        <a-form-item name="email">
          <a-auto-complete class="form-item-width" placeholder="请输入电子邮箱（非必填）"
                           v-model:value="profileInfo.email"
                           @search="emailHandleSearch"
                           :options="emailOptions"
                           allow-clear>
            <template #option="{ value: val }">
              {{ val.split('@')[0] }} @
              <span style="font-weight: bold">{{ val.split('@')[1] }}</span>
            </template>
          </a-auto-complete>
        </a-form-item>
        <a-form-item name="gender">
          <a-radio-group class="form-item-width" v-model:value="profileInfo.gender">
            <a-radio v-for="item in user_gender" :value="item.value">{{item.label}}</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </template>
  </login-setting-base-component>
</template>

<script setup lang="ts">
import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import {type FormInstance, message} from 'ant-design-vue';
import {initDict} from "@/utils/Dict.ts";
import {reactive, useTemplateRef, type Ref, ref} from "vue";
import type {ProfileInfo} from "@/api/system/profile/type/SysProfile.ts";
import type {Rule} from "ant-design-vue/es/form";
import {saveBasics} from "@/api/system/profile/Profile.ts";
import {useUserStore} from "@/stores/user.ts";
import {ResponseError} from "@/api/global/Type.ts";
const userStore = useUserStore()
const {user_gender} = initDict("user_gender")
// 向外抛出函数
const emits = defineEmits(['back', 'next'])

// 初始化数据
const init = () => {
  const profileInfo = reactive<ProfileInfo>({
    nickname: '',
    gender: '2'
  })

  const userRoles = reactive<Record<string,Rule[]> >({
    nickname: [
      { required: true , message: '用户昵称不能为空'},
      { max: 20 , message: '用户昵称最大20字符'}
    ],
    gender: [
      { required: true , message: '用户性别不能为空'}
    ],
    email: [
      { required: false},
      { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '请输入正确的邮箱'}
    ],
    phoneNumber: [
      { required: false},
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码'}
    ]
  })

  return {
    profileInfo,
    userRoles
  }
}

const {profileInfo, userRoles} = init()

const userBasicRef = useTemplateRef<FormInstance>("userBasicRef")

// 下一步
const handleNext = async (loading:Ref<boolean>) => {
  // 表单校验
  await userBasicRef.value?.validate()
  loading.value = true
  try {
    // 保存基础信息
    const resp = await saveBasics({
      nickname: profileInfo.nickname,
      gender: profileInfo.gender,
      phoneNumber: profileInfo.phoneNumber === ''? undefined: profileInfo.phoneNumber,
      email: profileInfo.email === ''? undefined: profileInfo.email,
    })

    if (resp.code === 200) {
      loading.value = false
      // 重新加载用户信息
      await userStore.initUserInfo()
      emits('next', loading.value)
    } else {
      message.warn(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  } finally {
    loading.value = false
  }
}

const emailOptions = ref<{ value: string }[]>([]);
const emailHandleSearch = (val: string) => {
  let res: { value: string }[];
  if (!val || val.indexOf('@') >= 0) {
    res = [];
  } else {
    res = ['qq.com','126.com', '163.com','aliyun.com','sina.com','sohu.com','gmail.com','outlook.com','hotmail.com'].map(domain => ({ value: `${val}@${domain}` }));
  }
  emailOptions.value = res;
};
</script>

<style scoped>
.form-item-width {
  width: 270px;
}
</style>
