export interface LoginType {
	/**
	 * 用户名
	 */
    username: string
	/**
	 * 密码
	 */
    password: string
	/**
	 * 验证码信息
	 */
    captchaVerification?: string
}