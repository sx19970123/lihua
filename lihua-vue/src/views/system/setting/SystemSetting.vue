<template>
  <div class="header-content-center">
    <a-row :gutter="8">
      <a-col :xxl="{span: 4}" :xl="{span: 5}" :lg="{span: 6}" :md="{span: 6}" :sm="{span: 6}" :xs="{span: 6}">
        <a-card style="height: 100%">
          <a-menu v-model:selected-keys="selectKeys"
                  style="border: 0;width: 100%"
                  :inlineCollapsed="collapsed"
                  @click="handleChangeSetting"
          >
            <a-menu-item-group title="账号">
              <a-menu-item key="DefaultPasswordSetting">
                <KeyOutlined />
                <span>系统默认密码</span>
              </a-menu-item>
              <a-menu-item key="IntervalUpdatePassword">
                <FieldTimeOutlined />
                <span>定期修改密码</span>
              </a-menu-item>
              <a-menu-item key="SameAccountLoginSetting">
                <LoginOutlined />
                <span>同账号登录限制</span>
              </a-menu-item>
            </a-menu-item-group>
            <a-menu-item-group title="登录">
              <a-menu-item key="SignInSetting">
                <IdcardOutlined />
                <span>自助注册</span>
              </a-menu-item>
              <a-menu-item key="VerificationCodeSetting">
                <RobotOutlined />
                <span>验证码</span>
              </a-menu-item>
            </a-menu-item-group>
            <a-menu-item-group title="其他">
              <a-menu-item key="RestrictAccessIpSetting">
                <GatewayOutlined />
                <span>限制访问IP</span>
              </a-menu-item>
              <a-menu-item key="GrayModelSetting"> <BgColorsOutlined />
                <span>灰色模式</span>
              </a-menu-item>
            </a-menu-item-group>
          </a-menu>
        </a-card>
      </a-col>
      <a-col :xxl="{span: 20}" :xl="{span: 19}" :lg="{span: 18}" :md="{span: 18}" :sm="{span: 18}" :xs="{span: 18}">
        <transition :name="themeStore.routeTransition" mode="out-in">
          <component class="scrollbar" :is="activeComponent" style="height: 100%"/>
        </transition>
      </a-col>
    </a-row>
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
import {markRaw, onMounted, onUnmounted, ref} from "vue";

const themeStore = useThemeStore()
const collapsed = ref<boolean>(false)
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
// 处理屏幕宽度变化
const handleChangeInnerWidth = () => {
  const width = window.innerWidth
  collapsed.value = width <= 992;
}

onMounted(() => {
  handleChangeInnerWidth()
  window.addEventListener('resize', handleChangeInnerWidth)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleChangeInnerWidth)
})
</script>