<template>
	<view class="auth-body">
		<view class="auth-content" :style="{ transform: openKeyboard ? 'translateY(-30%)' : 'translateY(0)' }">
			<sar-space direction="vertical" justify="center" size="large">
				<text class="auth-title">欢迎登录狸花猫</text>
				<sar-space align="center" size="0rpx" v-if="isRegistrationEnable">
					<text class="text-font">没有账号？</text>
					<sar-button type="pale-text" inline root-class="text-btn" @click="toRegister">
						快速注册
					</sar-button>
				</sar-space>
				<text class="text-font err-msg" v-if="serverConnectionFailed" @click="reload">连接服务器失败，点击重试</text>
				<sar-input placeholder="用户名" v-model="loginData.username" root-class="auth-item" :class="{ 'show-caret': openKeyboard }"
					clearable show-clear-only-focus>
					<template #prepend>
						<sar-icon color="var(--sar-tertiary-color)" family="outlined" name="UserOutlined" />
					</template>
				</sar-input>

				<sar-input placeholder="密码" v-model="loginData.password" type="password" root-class="auth-item" :class="{ 'show-caret': openKeyboard }"
					clearable show-clear-only-focus>
					<template #prepend>
						<sar-icon color="var(--sar-tertiary-color)" family="outlined" name="LockOutlined" />
					</template>
				</sar-input>

				<sar-checkbox size="28rpx" v-model:checked="enableRememberMe"><text class="text-font">记住账号</text></sar-checkbox>
				<sar-button 
					root-class="auth-item auth-item-btn" 
					:loading="loginLoading"
					@click="() => isEnableCaptcha ? openCaptcha() : handleLogin()">登 录</sar-button>
			</sar-space>
		</view>

		<!-- 用户协议，键盘弹起后隐藏 -->
		<view class="auth-protocol" v-if="!openKeyboard">
			<sar-space align="center" size="0rpx">
				<sar-checkbox type="circle" v-model:checked="checkProtocol" @change="cacheProtocol">我已阅读并同意</sar-checkbox>
				<sar-button type="pale-text" inline root-class="text-btn" @click="toUserAgreement">用户协议</sar-button>
				与
				<sar-button type="pale-text" inline root-class="text-btn" @click="toPrivacyPolicy">隐私政策</sar-button>
			</sar-space>
		</view>
		<!-- 验证码 -->
		<Captcha @success="handleLogin" ref="captchaRef" v-if="isEnableCaptcha"/>
	</view>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { enableCaptcha, enableSignUp } from '@/api/system/setting/setting'
import type { LoginType } from '@/api/system/authentication/type/login-type'
import router from '@/router/router'
import Captcha from '@/components/captcha/index.vue'
import {toast} from '@/utils/toast'
import {onShow, onHide} from '@dcloudio/uni-app'
import {rememberMe, getRememberedInfo} from '@/helpers/remember'
import {setToken} from '@/helpers/token'
import {login} from "@/api/system/authentication/authentication";
const captchaRef = ref<InstanceType<typeof Captcha>>()
const serverConnectionFailed = ref<boolean>(false)
/**
 * 初始化登录相关
 */
const initLogin = () => {
	// 用户登录数据
	const loginData = ref<LoginType>({username: '', password: ''})
	// 登录loading
	const loginLoading = ref<boolean>(false)
	
	// 检查登录信息是否填写完整
	const checkLoginData = () => {
		const data = loginData.value
		
		if (!data.username) {
			toast("请输入用户名")
			return false
		}
		
		if (!data.password) {
			toast("请输入密码")
			return false
		}
		
		if (!checkProtocol.value) {
			toast("请先阅读并勾选用户协议")
			return false
		}
		
		return true
	}
	
	// 用户登录
	const handleLogin = async (captchaVerification?: string) => {
		// 检查表单填写是否完整
		if (!checkLoginData()) {
			return
		}
		
		try {
			loginLoading.value = true
      loginData.value.captchaVerification = captchaVerification
			const resp = await login(loginData.value)
			// 登录成功
			if (resp.code === 200) {
        setToken(resp.data)
				// 处理记住账号
				handleRememberMe()
				// 跳转至首页
				router.reLaunch({
					url: "/pages/index/index"
				})
			} else {
				toast(resp.msg)
			}
		} finally {
			loginLoading.value = false
		}
	}
	
	return {
		loginData,
		loginLoading,
		checkLoginData,
		handleLogin
	}
}

const {loginData, loginLoading, checkLoginData, handleLogin} = initLogin()

/**
 * 初始化验证码相关
 */
const initCaptcha = () => {
	// 是否启用验证码
	const isEnableCaptcha = ref<boolean>(false)
	
	// 是否启用验证码
	const captcha = async () => {
		try {
			const resp = await enableCaptcha()
			if (resp.code === 200) {
				isEnableCaptcha.value = resp.data
			} else {
				toast(resp.msg)
			}
			serverConnectionFailed.value = false
		} catch(err) {
			serverConnectionFailed.value = true
		}
	}
	
	// 打开验证码
	const openCaptcha = () => {
		// 检查表单填写是否完整
		if (!checkLoginData()) {
			return
		}
		// 打开验证码
		const ref = captchaRef.value
		if (ref) {
			ref.open()
		}
	}
	
	return {
		isEnableCaptcha,
		captcha,
		openCaptcha
	}
}

const {isEnableCaptcha, captcha, openCaptcha} = initCaptcha()


/**
 * 初始化记住我相关
 */
const initRememberMe = () => {
	// 是否启用记住我
	const enableRememberMe = ref<boolean>(false)
	// 获取记住我信息
	const initRememberMeInfo = () => {
		const rememberedInfo = getRememberedInfo()
		if (rememberedInfo) {
			enableRememberMe.value = true
			loginData.value = rememberedInfo
		} else {
			enableRememberMe.value = false
		}
	}
	// 处理记住我
	const handleRememberMe = () => {
		const {username, password} = loginData.value
		rememberMe(enableRememberMe.value, username, password)
	}
	
	return {
		enableRememberMe,
		initRememberMeInfo,
		handleRememberMe
	}
}

const {enableRememberMe, initRememberMeInfo, handleRememberMe} = initRememberMe()


/**
 * 初始化用户协议相关
 */
const initProtocol = () => {
	// 缓存key常量
	const key = "lihua_protocol"
	
	// 检查用户协议
	const checkProtocol = ref<boolean>(false)
	
	// 缓存用户协议勾选状态
	const cacheProtocol = () => {
		if (checkProtocol.value) {
			uni.setStorageSync(key, checkProtocol)
		} else {
			uni.removeStorageSync(key)
		}
	}
	
	// 回显用户协议勾选状态
	const initProtocolStatus = () => {
		checkProtocol.value = !!uni.getStorageSync(key)
	}
	
	return {
		checkProtocol,
		cacheProtocol,
		initProtocolStatus
	}
}

const {checkProtocol, cacheProtocol, initProtocolStatus} = initProtocol()

/**
 * 用户注册相关
 */
const initRegister = () => {
	// 是否启用用户注册
	const isRegistrationEnable = ref<boolean>(false)
	
	// 获取用户注册状态
	const getRegisterStatus = async () => {
		const resp = await enableSignUp()
		if (resp.code === 200) {
			isRegistrationEnable.value = resp.data
		}
	}
	
	return {
		isRegistrationEnable,
		getRegisterStatus
	}
}

const {isRegistrationEnable, getRegisterStatus} = initRegister()

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


// 前往注册
const toRegister = () => {
	if (!checkProtocol.value) {
		toast("请先阅读并勾选用户协议")
		return
	}
	openKeyboard.value = false
	router.navigateTo({
		url: "/pages/login/Register?enableCaptcha=" + isEnableCaptcha.value,
		animationType: "slide-in-bottom"
	})
}

// 前往隐私政策
const toPrivacyPolicy = () => {
	router.navigateTo({
		url: "/subpackages/system/protocol/PrivacyPolicy",
	})
}

// 前往用户协议
const toUserAgreement = () => {
	router.navigateTo({
		url: "/subpackages/system/protocol/UserAgreement",
	})
}

/**
 * 监听注册成功返回数据
 */
const onRegisterSuccess = (username: string) => {
	loginData.value = {username, password: ''}
	rememberMe(false)
	enableRememberMe.value = false
	nextTick(() => toast("注册成功，用户名已自动代入", 2500))
}

const reload = () => {
	uni.$off('registerSuccess', onRegisterSuccess)
	captcha()
	getRegisterStatus()
	initRememberMeInfo()
	initProtocolStatus()
	uni.$on('registerSuccess', onRegisterSuccess)
}

onMounted(() => {
	reload()
})

onUnmounted(() => {
	uni.$off('registerSuccess', onRegisterSuccess)
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
.err-msg {
	color: var(--sar-danger);
}
</style>