<template>
  <div>
    <a-form layout="vertical"
            :model="profileInfo"
            :rules="userRoles"
            @finish="handleFinish"
            @finishFailed="handleFinishFailed"
    >
      <a-row :span="8">
        <a-form-item label="头像">
          <avatar-modifier v-model="profileInfo.avatar"/>
        </a-form-item>
      </a-row>
      <a-form-item label="用户昵称" name="nickname">
        <a-input class="form-item-width" placeholder="请输入用户昵称" v-model:value="profileInfo.nickname" allow-clear show-count/>
      </a-form-item>
      <a-form-item label="手机号码" name="phoneNumber">
        <a-input class="form-item-width" placeholder="请输入手机号码" v-model:value="profileInfo.phoneNumber" allow-clear/>
      </a-form-item>
      <a-form-item label="电子邮箱" name="email">
        <a-auto-complete class="form-item-width" placeholder="请输入电子邮箱"  v-model:value="profileInfo.email" @search="emailHandleSearch" :options="emailOptions" allow-clear>
          <template #option="{ value: val }">
            {{ val.split('@')[0] }} @
            <span style="font-weight: bold">{{ val.split('@')[1] }}</span>
          </template>
        </a-auto-complete>
      </a-form-item>
      <a-form-item label="性别" name="gender">
        <a-radio-group v-model:value="profileInfo.gender">
          <a-radio :value="item.value" v-for="item in user_gender">{{item.label}}</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item>
        <a-flex :gap="16">
          <a-button type="primary" html-type="submit">提 交</a-button>
        </a-flex>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from "vue";
import {useUserStore} from "@/stores/modules/user";
import AvatarModifier from "@/views/system/profile/components/AvatarModifier.vue";
import type {Rule} from "ant-design-vue/es/form";
import {message} from "ant-design-vue";
import type {ProfileInfo} from "@/api/system/profile/type/SysProfile.ts";
import {saveBasics} from "@/api/system/profile/Profile.ts";
import {initDict} from "@/utils/Dict.ts"
import {cloneDeep} from "lodash-es";
const userStore = useUserStore()
const {user_gender} = initDict('user_gender')


// 初始化数据
const init = () => {
  const profileInfo = reactive<ProfileInfo>({
    avatar: userStore.avatar,
    nickname: userStore.userInfo.nickname,
    gender: userStore.userInfo.gender,
    email: userStore.userInfo.email,
    phoneNumber: userStore.userInfo.phoneNumber
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
      { required: false , message: '邮箱地址不能为空'},
      { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '请输入正确的邮箱'}
    ],
    phoneNumber: [
      { required: false , message: '手机号码不能为空'},
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码'}
    ]
  })

  return {
    profileInfo,
    userRoles
  }
}

/**
 * 保存用户信息
 * @param values
 */
const handleFinish = (values: {nickname: string,gender:string,email:string,phoneNumber:string}) => {
  // 保存到数据库删除url属性
  const avatar = cloneDeep(profileInfo.avatar)
  delete avatar.url

  saveBasics({
    avatar: JSON.stringify(avatar),
    nickname: values.nickname,
    gender: values.gender,
    email: values.email,
    phoneNumber: values.phoneNumber
  }).then(resp => {
    if (resp.code === 200){
      message.success("保存成功")
      userStore.getUserInfo()
    }
  })
}

const handleFinishFailed = ({ errorFields }: { errorFields: any }) => {
  if (errorFields.length > 0) {
    message.error("请将表单填写完整")
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

const  {profileInfo, userRoles}= init()


</script>

<style scoped>
.form-item-width {
  width: 270px;
}
</style>
