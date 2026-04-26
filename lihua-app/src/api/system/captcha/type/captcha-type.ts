
/**
 * 请求验证码参数
 */
export interface CaptchaRequestData {
	// id
	id: string;
	// 验证码数据
	data: {
		// 当前环境滑块背景宽
		bgImageWidth: number;
		// 当前环境滑块背景高
		bgImageHeight: number;
		// 开始时间
		startTime: number;
		// 结束时间
		stopTime: number;
		// 轨迹
		trackList: Array<{x: number, y: number, type: string, t: number}>
	}
}

/**
 * 请求验证码返回数据
 */
export interface CaptchaResponseData {
	/**
	 * id
	 */
	id: string;
	/**
	 * 验证码类型
	 */
	type?: 'WORD_IMAGE_CLICK' | 'CONCAT' | 'ROTATE' | 'SLIDER';
	/**
	 * 背景图片
	 */
	backgroundImage?: string;
	/**
	 * 临时图片
	 */
	templateImage?: string;
	/**
	 * 背景图标签
	 */
	backgroundImageTag?: string;
	/**
	 * 临时图标签
	 */
	templateImageTag?: string;
	/**
	 * 背景图宽度
	 */
	backgroundImageWidth?: number;
	/**
	 * 背景图高度
	 */
	backgroundImageHeight?: number;
	/**
	 * 临时图宽度
	 */
	templateImageWidth?: number;
	/**
	 * 临时图高度
	 */
	templateImageHeight?: number;
	/**
	 * 其他数据
	 */
	data?: {
		/**
		 * Y轴旋转角度（拼接类型使用）
		 */
		randomY?: number
	}
	
}