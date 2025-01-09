<template>
  <div>
    <a-form
            :model="profileInfo"
            :rules="userRoles"
            :colon="false"
            :label-col="{ style: { marginTop: '4px' } }"
            @finish="handleFinish"
            @finishFailed="handleFinishFailed"
    >
      <a-row :span="8" style="margin-bottom: 10px">
        <avatar-modifier v-model="profileInfo.avatar"/>
      </a-row>
<!--      <a-card>-->
        <a-form-item label="用户昵称" class="basic-form-item">
          <dynamic-border-input v-model="profileInfo.nickname"/>
          <!--        <a-input class="form-item-width"-->
          <!--                 placeholder="请输入用户昵称"-->
          <!--                 @focus="handleFocus()"-->
          <!--                 @blur="handleBlur()"-->
          <!--                 @mouseover="handleMouseOver()"-->
          <!--                 @mouseout="handleMouseOut()"-->
          <!--                 size="large"-->
          <!--                 v-model:value="profileInfo.nickname"-->
          <!--                 :style="{'border-color': showBordered || isGetFocus ? themeStore.getColorPrimary() : 'rgba(0,0,0,0)'}"-->
          <!--        >-->
          <!--          <template #suffix>-->
          <!--            <a-button :icon="h(CheckOutlined)" type="text" size="small" :style="{color: themeStore.getColorPrimary()}"  v-if="isGetFocus"></a-button>-->
          <!--            <EditOutlined class="input-prefix-icon-color" v-else/>-->
          <!--          </template>-->
          <!--        </a-input>-->
        </a-form-item>
        <a-form-item label="手机号码" class="basic-form-item" name="phoneNumber">
          <dynamic-border-input v-model="profileInfo.phoneNumber"/>
          <!--        <a-input class="form-item-width" placeholder="请输入手机号码" v-model:value="profileInfo.phoneNumber" allow-clear/>-->
        </a-form-item>
        <a-form-item label="电子邮箱" class="basic-form-item" name="email">
          <dynamic-border-input v-model="profileInfo.email"/>
          <!--        <a-auto-complete class="form-item-width" placeholder="请输入电子邮箱"  v-model:value="profileInfo.email" @search="emailHandleSearch" :options="emailOptions" allow-clear>-->
          <!--          <template #option="{ value: val }">-->
          <!--            {{ val.split('@')[0] }} @-->
          <!--            <span style="font-weight: bold">{{ val.split('@')[1] }}</span>-->
          <!--          </template>-->
          <!--        </a-auto-complete>-->
        </a-form-item>
        <a-form-item label="用户性别" class="basic-form-item">
          <dynamic-border-select v-model="profileInfo.gender"/>
          <!--        <a-select class="form-item-width" v-model:value="profileInfo.gender" :bordered="false">-->
          <!--          <template #suffixIcon><EditOutlined class="input-prefix-icon-color" style="font-size: 14px"/></template>-->
          <!--          <a-select-option :value="item.value" v-for="item in user_gender">{{item.label}}</a-select-option>-->
          <!--        </a-select>-->
          <!--        <a-radio-group v-model:value="profileInfo.gender">-->
          <!--          <a-radio :value="item.value" v-for="item in user_gender">{{item.label}}</a-radio>-->
          <!--        </a-radio-group>-->
        </a-form-item>
        <a-divider/>
        <a-typography-title :level="5">部门信息</a-typography-title>
        <a-typography-text>
          {{userStore.defaultDept.name}}
          <a-tag style="margin-left: 8px" color="processing">默认部门</a-tag>
          <a-tag v-for="post in userStore.defaultDeptPosts">{{post.name}}</a-tag>
        </a-typography-text>
        <a-divider/>
        <a-typography-title :level="5">其他信息</a-typography-title>
        <a-form-item label="我的角色">
          <a-tag v-for="roleName in userStore.roles.map(item => item.name)" :color="themeStore.getColorPrimary()">{{roleName}}</a-tag>
        </a-form-item>
        <a-form-item v-if="false">
          <a-flex :gap="16">
            <a-button type="primary" html-type="submit" :loading="submitLoading">提 交</a-button>
          </a-flex>
        </a-form-item>
<!--      </a-card>-->
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from "vue";
import {useUserStore} from "@/stores/user";
import AvatarModifier from "@/views/system/profile/components/AvatarModifier.vue";
import type {Rule} from "ant-design-vue/es/form";
import {message} from "ant-design-vue";
import type {ProfileInfo} from "@/api/system/profile/type/SysProfile.ts";
import {saveBasics} from "@/api/system/profile/Profile.ts";
import {initDict} from "@/utils/Dict.ts"
import {cloneDeep} from "lodash-es";
import {ResponseError} from "@/api/global/Type.ts";
import {useThemeStore} from "@/stores/theme.ts";
import DynamicBorderInput from "@/components/dynamic-border-input/index.vue"
import DynamicBorderSelect from "@/components/dynamic-border-select/index.vue"
const userStore = useUserStore()
const {user_gender} = initDict('user_gender')
const submitLoading = ref<boolean>(false)
const themeStore = useThemeStore()

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
  submitLoading.value = true
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
      message.success(resp.msg)
      userStore.initUserInfo().catch(e => {
        if (e instanceof ResponseError) {
          message.error(e.msg)
        } else {
          console.log(e)
        }
      })
    } else {
      message.error(resp.msg)
    }
  }).catch((e) => {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  }).finally(() => {
    submitLoading.value = false
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
  width: 370px;
}
.basic-form-item {
  margin-bottom: 22px;
}
</style>
