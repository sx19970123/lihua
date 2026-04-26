<template>
	<view class="auth-body">
		<view class="auth-content" :style="{ transform: openKeyboard ? 'translateY(-30%)' : 'translateY(0)' }">
			<sar-space direction="vertical" size="large">
				<text class="auth-title">欢迎注册狸花猫</text>
				<sar-space align="center" size="0rpx">
					<text class="text-font">已有账号？</text>
					<sar-button type="pale-text" inline root-class="text-btn" @click="toLogin">
						返回登录
					</sar-button>
				</sar-space>
	
				<sar-input 
					placeholder="用户名" 
					root-class="auth-item" 
					:class="{ 'show-caret': openKeyboard }"
					clearable
					show-clear-only-focus
					v-model="registerData.username"
					@blur="handleCheckUsername"
				>
					<template #prepend>
						<sar-icon color="var(--sar-tertiary-color)" family="outlined" name="UserOutlined" />
					</template>
				</sar-input>
				
				<password-input 
					v-model:value="registerData.password" 
					:class="{ 'show-caret': openKeyboard }"
					placeholder="密码" 
					showPrepend>
				</password-input>
				
				<sar-input 
					placeholder="再次输入" 
					type="password" 
					root-class="auth-item" 
					:class="{ 'show-caret': openKeyboard }"
					clearable
					show-clear-only-focus
					show-eye
					v-model="registerData.confirmPassword"
				>
					<template #prepend>
						<sar-icon color="var(--sar-tertiary-color)" family="outlined" name="LockOutlined" />
					</template>
				</sar-input>
				
				<sar-button 
					root-class="auth-item auth-item-btn" 
					:loading="registerLoading"
					@click="enableCaptcha? openCaptcha(): handleRegister()">注 册</sar-button>
			</sar-space>
		</view>
		
		<!-- 验证码 -->
		<Captcha @success="handleRegister" ref="captchaRef" v-if="enableCaptcha"/>
	</view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import type { RegisterType } from '@/api/system/authentication/type/register-type'
import {register, checkUserName} from '@/api/system/authentication/authentication'
import router from '@/router/router'
import Captcha from '@/components/captcha/index.vue'
import {toast} from '@/utils/toast'
import {onShow, onHide, onLoad} from "@dcloudio/uni-app"
import {cloneDeep} from "lodash-es"
import PasswordInput from '@/components/password-input/index.vue'

const captchaRef = ref<InstanceType<typeof Captcha>>()

/**
 * 返回登录
 */
const toLogin = () => {
	openKeyboard.value = false
	router.navigateBack({})
}

/**
 * 初始化登录相关
 */
const initRegister = () => {
	// 用户注册数据
	const registerData = ref<RegisterType>({username: '', password: '', passwordRequestKey: '', confirmPassword: '', confirmPasswordRequestKey: ''})
	// 用户名是否已存在
	const usernameExists = ref<boolean>(false)
	// 用户注册loading
	const registerLoading = ref<boolean>(false)
	
	// 检查注册信息是否填写完整
	const checkRegisterData = async () => {
		const data = registerData.value
		
		// 用户名非空校验
		if (!data.username) {
			toast("请输入用户名")
			return false
		}
		
		// 用户名规则校验
		if (!(/^[a-zA-Z0-9@.]+$/.test(data.username))) {
			toast("用户名只允许大小写英文、数字、@、.")
			return false
		}
		
		// 提交时用户名重复情况下再次验证
		if (usernameExists.value) {
			await handleCheckUsername()
			if (usernameExists.value) {
				return false
			}
		}
		
		// 密码非空校验
		if (!data.password) {
			toast("请输入密码")
			return false
		}
		
		// 密码长度校验
		if (!(data.password.length >= 6 && data.password.length <= 30)) {
			toast("密码长度6-30位")
			return false
		}
		
		// 二次密码校验
		if (data.password !== data.confirmPassword) {
			toast("两次密码不一致")
			return false
		}
		
		return true
	}
	
	/**
	 * 检查用户名是否存在
	 */
	const handleCheckUsername = async () => {
		const username = registerData.value.username
		if (!username) {
			return
		}
		
		const resp = await checkUserName(username)
		if (resp.code === 200) {
			if (!resp.data) {
				toast("该用户名已存在")
			}
			usernameExists.value = !resp.data
		} else {
			toast(resp.msg)
		}
	}
	
	/**
	 * 用户注册
	 */
	const handleRegister = async (captchaId?: string) => {
		// 检查表单填写是否完整
		if (!await checkRegisterData()) {
			return
		}
		
		try {
			registerLoading.value = true
			const data = cloneDeep(registerData.value) as RegisterType
			
			// 验证码id
			data.captchaVerification = captchaId
			// 注册
			const resp = await register(data)
			// 注册成功
			if (resp.code === 200) {
				uni.$emit('registerSuccess', data.username)
				toLogin()
			} else {
				toast(resp.msg)
			}
		} finally {
			registerLoading.value = false
		}
	}
	
	return {
		registerData,
		registerLoading,
		checkRegisterData,
		handleCheckUsername,
		handleRegister
	}
}

const {registerData, registerLoading, checkRegisterData, handleCheckUsername, handleRegister} = initRegister()

/**
 * 初始化验证码相关
 */
const initCaptcha = () => {
	// 是否启用验证码
	const enableCaptcha = ref<boolean>(false)
	
	// 打开验证码
	const openCaptcha = async () => {
		// 检查表单填写是否完整
		if (!await checkRegisterData()) {
			return
		}
		// 打开验证码
		const ref = captchaRef.value
		if (ref) {
			ref.open()
		}
	}
	
	return {
		enableCaptcha,
		openCaptcha
	}
}

const {enableCaptcha, openCaptcha} = initCaptcha()

/**
 * 初始化键盘监听
 */
const initKeyboardStatus = () => {
	// 控制键盘弹起状态
	const openKeyboard = ref<boolean>(false)
	// 键盘高度变化监听
	const handleChangeKeyboardHeight = (data : UniNamespace.OnKeyboardHeightChangeResult) => {
		openKeyboard.value = data.height > 0
	}
		
	return {
		openKeyboard,
		handleChangeKeyboardHeight
	}
}

const {openKeyboard, handleChangeKeyboardHeight} = initKeyboardStatus()

onLoad((option) => {
	if (option) {
		enableCaptcha.value = option.enableCaptcha
	}
})

onShow(() => {
	uni.onKeyboardHeightChange(handleChangeKeyboardHeight)
})

onHide(() => {
	uni.offKeyboardHeightChange(handleChangeKeyboardHeight)
})
</script>

<style lang="scss">
@import "@/static/style/auth.scss";
@import "@/static/style/input.scss";
</style>