<template>
	<sar-space direction="vertical">
		<!-- 输入框 -->
		<!-- 微信小程序下 插槽使用v-if禁用也会出现样式变化，所以对整个组件进行v-if -->
		<sar-input v-if="props.showPrepend" :placeholder="props.placeholder" root-class="rounded-input" :class="clazz" @input="handleChangePwd" v-model="pwdValue" :maxlength="30" type="password" show-eye clearable show-clear-only-focus>
			<template #prepend v-if="props.showPrepend">
				<sar-icon color="var(--sar-tertiary-color)" family="outlined" name="LockOutlined" />
			</template>
		</sar-input>
		<sar-input v-else :placeholder="props.placeholder" root-class="rounded-input" :class="clazz" @input="handleChangePwd" v-model="pwdValue" :maxlength="30" type="password" show-eye clearable show-clear-only-focus />
		<!-- 密码强度指示条 -->
		<sar-space size="small" class="indicator-bar" v-if="props.value">
			<sar-progress-bar root-style="width: 100%" :percent="weak" color="#ff4d4f" :show-text="false" />
			<sar-progress-bar root-style="width: 100%" :percent="medium" color="#faad14" :show-text="false" />
			<sar-progress-bar root-style="width: 100%" :percent="strong" color="#52c41a" :show-text="false" />
		</sar-space>
	</sar-space>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
// 不同强度正则表达式
const weakRegex = /^.{6,}$/;
const mediumRegex = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/;
const strongRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[^A-Za-z0-9]).{10,}$/;

const props = defineProps<{value?: string, showPrepend?: boolean, placeholder?: string, clazz?: string}>()
const emits = defineEmits(['update:value'])

// 强
const strong = ref<0 | 100>(0)
// 中
const medium = ref<0 | 100>(0)
// 弱
const weak = ref<0 | 100>(0)

// 密码值
const pwdValue = ref<string>()

const handleChangePwd = () => {
	const value = pwdValue.value
	weak.value = 0
	medium.value = 0
	strong.value = 0
	
	emits('update:value', value)
	
	if (!value) {
		return
	}
	
	if (strongRegex.test(value)) {
		weak.value = 100
		medium.value = 100
		strong.value = 100
	} else if (mediumRegex.test(value)) {
		weak.value = 100
		medium.value = 100
	} else if (weakRegex.test(value)) {
		weak.value = 100
	}
	
}


onMounted(() => {
	pwdValue.value = props.value
	handleChangePwd()
})
</script>

<style scoped lang="scss">
.indicator-bar {
	padding-left: 8rpx; 
	padding-right: 8rpx;
}	
</style>