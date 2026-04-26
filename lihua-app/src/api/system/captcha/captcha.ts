import request from "@/utils/request";
import type {CaptchaRequestData, CaptchaResponseData} from "@/api/system/captcha/type/captcha-type"

/**
 * 获取验证码数据
 */
export const getCaptchaData = () => {
	return request<CaptchaResponseData>({
		url: 'captcha/get',
		method: 'POST'
	})
}

/**
 * 校验验证码
 */
export const check = (data: CaptchaRequestData) => {
	return request<CaptchaResponseData>({
		url: 'captcha/check',
		method: 'POST',
		data: data
	})
}