import request from "@/utils/request";

/**
 * 是否开启验证码
 */
export const enableCaptcha = () => {
	return request<boolean>({
		url: 'app/system/setting/base/enableCaptcha',
		method: 'GET'
	})
}

/**
 * 是否开启自助注册
 */
export const enableSignUp = () => {
	return request<boolean>({
		url: 'app/system/setting/base/enableSignUp',
		method: 'GET'
	})
}