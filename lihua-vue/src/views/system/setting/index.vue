<template>
  <div class="header-content-center">
    <a-card>
      <a-row>
        <a-col :span="4">
          <a-menu v-model:selected-keys="selectKeys"
                  style="height: 100%"
                  @click="handleChangeSetting"
          >
            <a-menu-item-group title="账号安全设置">
              <a-menu-item key="DefaultPasswordSetting"> 系统默认密码</a-menu-item>
              <a-menu-item key="IntervalUpdatePassword"> 定期修改密码</a-menu-item>
              <a-menu-item key="SameAccountLoginSetting"> 同账号登录限制</a-menu-item>
            </a-menu-item-group>

            <a-menu-item-group title="登录设置">
              <a-menu-item key="SignInSetting"> 自助注册</a-menu-item>
              <a-menu-item key="VerificationCodeSetting"> 登录验证码</a-menu-item>
            </a-menu-item-group>

            <a-menu-item-group title="访问控制">
              <a-menu-item key="RestrictAccessIpSetting"> 限制访问IP</a-menu-item>
            </a-menu-item-group>

            <a-menu-item-group title="其他设置">
              <a-menu-item key="GrayModelSetting"> 灰色模式</a-menu-item>
            </a-menu-item-group>
          </a-menu>
        </a-col>
        <a-col :span="20">
          <transition :name="themeStore.routeTransition" mode="out-in">
            <component :is="activeComponent" style="margin: 20px"/>
          </transition>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {useThemeStore} from "@/stores/theme.ts";
import DefaultPasswordSetting from "@/views/system/setting/components/DefaultPasswordSetting.vue";
import GrayModelSetting from "@/views/system/setting/components/GrayModelSetting.vue";
import SignInSetting from "@/views/system/setting/components/SignInSetting.vue";
import UpdatePasswordSetting from "@/views/system/setting/components/IntervalUpdatePasswordSetting.vue";
import VerificationCodeSetting from "@/views/system/setting/components/VerificationCodeSetting.vue";
import RestrictAccessIpSetting from "@/views/system/setting/components/RestrictAccessIpSetting.vue";
import SameAccountLoginSetting from "@/views/system/setting/components/SameAccountLoginSetting.vue";
import {markRaw, ref} from "vue";

const themeStore = useThemeStore()

const allComponents = ref([
  {
    name: 'DefaultPasswordSetting',
    com: markRaw(DefaultPasswordSetting)
  },
  {
    name: 'GrayModelSetting',
    com: markRaw(GrayModelSetting)
  },
  {
    name: 'SignInSetting',
    com: markRaw(SignInSetting)
  },
  {
    name: 'IntervalUpdatePassword',
    com: markRaw(UpdatePasswordSetting)
  },
  {
    name: 'VerificationCodeSetting',
    com: markRaw(VerificationCodeSetting)
  },
  {
    name: 'RestrictAccessIpSetting',
    com: markRaw(RestrictAccessIpSetting)
  },
  {
    name: 'SameAccountLoginSetting',
    com: markRaw(SameAccountLoginSetting)
  }
])
// 菜单回显
const selectKeys = ref(['DefaultPasswordSetting'])
// 选中组件
const activeComponent = ref(markRaw(DefaultPasswordSetting))
// 处理选择设置
const handleChangeSetting = ({key}: {key: string}) => {
  const target = allComponents.value.filter(item => item.name === key)[0]
  activeComponent.value = target.com
}
</script>


<style scoped>

</style>
