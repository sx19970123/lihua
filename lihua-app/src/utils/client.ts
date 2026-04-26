/**
 * 请求的客户端类型
 */
export const getClientType = (): 'app' | 'wechat_mp' => {
	//#ifdef APP
		return 'app'
	//#endif
	
	//#ifdef MP-WEIXIN
		return 'wechat_mp'
	//#endif
}