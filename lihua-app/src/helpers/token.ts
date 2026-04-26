const TOKEN_KEY: string = "lihua_token"

/**
 * 获取token
 */
export const getToken = (): string | undefined => {
	return uni.getStorageSync(TOKEN_KEY)
}

/**
 * 设置token
 */
export const setToken = (token: string): void => {
	uni.setStorageSync(TOKEN_KEY, token)
}

/**
 * 删除token
 */
export const removeToken = () => {
	uni.removeStorageSync(TOKEN_KEY)
}