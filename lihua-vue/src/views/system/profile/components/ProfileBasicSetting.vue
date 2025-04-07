<template>
  <a-form
      ref="formRef"
      :hideRequiredMark="true"
      :model="profileInfo"
      :rules="userRoles"
      :colon="false"
      :label-col="{ style: { marginTop: '4px' } }"
  >
    <a-flex gap="small" wrap="wrap">
      <!--      个人中心卡片-->
      <a-card class="info-card" style="flex: 1">
        <a-typography-title :level="5">个人信息</a-typography-title>
        <div style="max-width: 400px;margin: auto">
          <a-form-item>
            <avatar-modifier v-model="profileInfo.avatar" @change="(value: string) => handleFinish({avatar: value})"/>
          </a-form-item>
          <a-form-item label="用户昵称" name="nickname">
            <dynamic-border-input ref="nicknameInputRef"
                                  required
                                  v-model="profileInfo.nickname"
                                  @reset="handleClearValidate"
                                  @submit="(value: string) => handleFinish({nickname : value})"
            />
          </a-form-item>
          <a-form-item label="手机号码" name="phoneNumber">
            <dynamic-border-input ref="phoneNumberInputRef"
                                  v-model="profileInfo.phoneNumber"
                                  @reset="handleClearValidate"
                                  @submit="(value: string) => handleFinish({phoneNumber : value})"/>
          </a-form-item>
          <a-form-item label="电子邮箱" name="email">
            <dynamic-border-input ref="emailInputRef"
                                  v-model="profileInfo.email"
                                  @reset="handleClearValidate"
                                  @submit="(value: string) => handleFinish({email : value})"/>
          </a-form-item>
          <a-form-item label="用户性别">
            <dynamic-border-select ref="genderInputRef"
                                   v-model="profileInfo.gender"
                                   :options="user_gender" @submit="(value: string) => handleFinish({gender : value})"/>
          </a-form-item>
        </div>
      </a-card>
      <a-flex vertical gap="small" style="flex: 1.68;">
        <!--      部门和岗位卡片-->
        <a-card class="info-card">
          <a-typography-title :level="5">部门岗位</a-typography-title>
          <selectable-card :dataSource="deptList"
                           itemKey="id"
                           v-model="defaultDeptId"
                           @change="handleChangeDefaultDept"
          >
          <template #content="{item, color}">
            <a-flex vertical gap="small">
              <a-flex gap="small">
                <a-typography-text strong>
                  {{item.name}}
                </a-typography-text>
                <a-tag :color="color" v-show="item.code === userStore.defaultDept.code">默认部门</a-tag>
              </a-flex>
              <a-typography-text type="secondary">
                {{item.code}}
              </a-typography-text>
              <div v-if="postList.filter(post => post.deptId === item.id).length > 0">
                <a-tag v-for="post in postList.filter(post => post.deptId === item.id)">
                  {{post.name}}
                </a-tag>
              </div>
              <div v-else>
                <a-typography-text  type="secondary">暂无岗位</a-typography-text>
              </div>
            </a-flex>
            </template>
          </selectable-card>
        </a-card>
        <!--      部门和岗位卡片-->
        <a-card class="info-card">
          <a-typography-title :level="5">我的角色</a-typography-title>
          <a-form-item>
            <a-tag v-for="roleName in userStore.roles.map(item => item.name)" :color="themeStore.getColorPrimary()">{{roleName}}</a-tag>
          </a-form-item>
        </a-card>
      </a-flex>
    </a-flex>
  </a-form>
</template>

<script setup lang="ts">
import {nextTick, reactive, ref, useTemplateRef, watch} from "vue";
import {useUserStore} from "@/stores/user";
import AvatarModifier from "@/views/system/profile/components/AvatarModifier.vue";
import type {Rule} from "ant-design-vue/es/form";
import {type FormInstance, message} from "ant-design-vue";
import type {ProfileInfo} from "@/api/system/profile/type/SysProfile.ts";
import {saveBasics, setDefaultDept} from "@/api/system/profile/Profile.ts";
import {initDict} from "@/utils/Dict.ts"
import {ResponseError} from "@/api/global/Type.ts";
import {useThemeStore} from "@/stores/theme.ts";
import DynamicBorderInput from "@/components/dynamic-border-input/index.vue"
import DynamicBorderSelect from "@/components/dynamic-border-select/index.vue"
import SelectableCard from "@/components/selectable-card/index.vue"
import {flattenTree} from "@/utils/Tree"
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";

const userStore = useUserStore()
const {user_gender} = initDict('user_gender')
const themeStore = useThemeStore()

const formRef = useTemplateRef<FormInstance>("formRef")

// 动态边框输入框组件ref
const nicknameInputRef = useTemplateRef<InstanceType<typeof DynamicBorderInput>>("nicknameInputRef")
const phoneNumberInputRef = useTemplateRef<InstanceType<typeof DynamicBorderInput>>("phoneNumberInputRef")
const emailInputRef = useTemplateRef<InstanceType<typeof DynamicBorderInput>>("emailInputRef")
const genderInputRef = useTemplateRef<InstanceType<typeof DynamicBorderSelect>>("genderInputRef")

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

const { profileInfo, userRoles }= init()

/**
 * 保存用户信息
 * @param values
 */
const handleFinish = async (values: {avatar?: string,nickname?: string, gender?: string, email?: string, phoneNumber?: string}) => {
  try {
    const resp = await saveBasics(values)
    if (resp.code === 200){
      dynamicBorderSuccess()
      message.success(resp.msg)
      // 重新获取用户信息
      await userStore.initUserInfo()
    } else {
      dynamicBorderError()
      message.error(resp.msg)
    }
  } catch(e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
    dynamicBorderError()
  }
}

// 动态边框输入框同一执行成功方法调用
const dynamicBorderSuccess = () => {
  nicknameInputRef.value?.success()
  genderInputRef.value?.success()
  emailInputRef.value?.success()
  phoneNumberInputRef.value?.success()
}

// 动态边框输入框同一执行失败方法调用
const dynamicBorderError = () => {
  nicknameInputRef.value?.error()
  genderInputRef.value?.error()
  emailInputRef.value?.error()
  phoneNumberInputRef.value?.error()
}


// 清除验证提示
const handleClearValidate = () => {
  if (!formRef.value) {
    return
  }
  formRef.value.clearValidate()
}

// 初始化部门相关逻辑
const initDept = () => {
  // 默认部门id
  const defaultDeptId = ref<string|undefined>(userStore.defaultDept.id)
  // 部门列表
  const deptList = flattenTree(userStore.deptTrees)
  // 岗位列表
  const postList = userStore.posts
  // 修改默认部门
  const handleChangeDefaultDept = async ({item}:{item: SysDept}) => {
    // item 为空，表示取消了选中，手动将defaultDeptId赋值为默认部门id
    if (!item || !item.id) {
      await nextTick(() => defaultDeptId.value = userStore.defaultDept.id)
      return
    }
    // 修改默认部门
    try {
      const resp = await setDefaultDept(item.id)
      if (resp.code === 200) {
        // 更新默认部门
        userStore.updateDefaultDept(resp.data)
        message.success(resp.msg)
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }

  return {
    deptList,
    defaultDeptId,
    postList,
    handleChangeDefaultDept
  }
}
const {deptList, defaultDeptId, postList, handleChangeDefaultDept} = initDept()

// 默认部门id变化时同步选中
watch(() => userStore.defaultDept.id, (value) => {
  defaultDeptId.value = value
})
</script>
<style scoped>
.info-card {
  flex: 1;
  min-width: 300px
}
</style>