export interface RegisterType {
	/**
	 * 用户名
	 */
	username: string
	/**
	 * 密码
	 */
	password: string
	/**
	 * 密码请求key
	 */
	passwordRequestKey: string
	/**
	 * 确认密码
	 */
	confirmPassword: string
	/**
	 * 确认密码请求key
	 */
	confirmPasswordRequestKey: string
	/**
	 * 验证码信息
	 */
	captchaVerification?: string
}