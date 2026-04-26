import {measureTextWidth} from '@/utils/TextUtils'

type NotifyStyle = {height: number; width: number; top: number; left: number}
type TouchEventData = {
  clientX: number;
  clientY: number;
  pageX: number;
  pageY: number;
  screenX: number;
  screenY: number;
  target: EventTarget;
  currentImageIndex?: number;
}
type NotifyContent = {
	title?: string
	content: string
	image?: string
	duration?: number
}
const defaultImage = '_www/static/notice/MessageOutlined.png'
const defaultTitle = '通知'

class MessageNotify {
	// 通知载体
	private view: PlusNativeObjView | any
	
	// 通知容器尺寸
	private noticeContainer: NotifyStyle | any
	
	// 通知图片尺寸
	private noticeImage: NotifyStyle | any
	
	// 通知标题尺寸
	private noticeTitle: NotifyStyle & {size: number} | any
	
	// 通知内容尺寸
	private noticeContent: NotifyStyle & {size: number} | any
	
	// 圆角
	private radius: string | any
	
	// 通知状态为开启
	private noticeIsShow: boolean | any
	
	// 通知点击事件
	private clickEvent?: () => void
	
	// 通知移动事件
	private moveEndEvent?: (direction: 'right' | 'left' | 'bottom' | 'top') => void
	
	// 自动关闭毫秒数
	private duration:number | any
	
	// 关闭延迟
	private closeTimeout: any
	
	// 通知栏滑动状态
	private draggingMeta: {
		// 是否正在滑动
		noticeIsdragging: boolean
		// 开始的y值
		startY: number,
		// 开始的x值
		startX: number,
		// 透明度
		opacity?: number,
		// 滑动方向
		direction?: 'x' | 'y',
		// 向下滑动的最大top值
		maxTop: number,
		// 当前的top值
		currentTop: number
	} | any
	// 操作系统名称
	private osName: string | any
	
	// 系统主题
	private theme: string | any
	// 颜色
	private color: {
		// 卡片背景颜色
		backageColor: '#e5e5e5' | '#2b2b2b',
		// 标题颜色
		titleColor: '#000' | '#fff',
		// 内容颜色
		contentColor: 'rgba(0, 0, 0, 0.45)' | 'rgba(255, 255, 255, 0.45)',
		// 头像遮罩颜色
		maskColor: 'rgba(0,0,0,0)' | 'rgba(0,0,0,0.2)'
	} | any
	
	// 通知内容
	private notifyContent?: NotifyContent
	
	constructor() {
		try {
			// 系统信息
			const sysInfo = uni.getSystemInfoSync()

			// 初始化容器尺寸
			const windowInfo = uni.getWindowInfo()
			// 操作系统名称
			this.osName = sysInfo.osName
			// 当前主题
			this.theme = sysInfo.theme || 'light'
			// 容器最大宽度，480 以下的设备都以边距16进行计算
			const maxWidth = 480
			// 边距
			const margin = 8
			// 高度
			const height = 72
			// 容器全部宽度
			const width = windowInfo.screenWidth > maxWidth ? maxWidth : windowInfo.screenWidth
			// 左位置
			const left = width === maxWidth ? (windowInfo.screenWidth - width) / 2 : margin
			
			// 圆角
			this.radius = "16px"
			// 通知是否在显示中
			this.noticeIsShow = false
			// 自动关闭毫秒数
			this.duration = 3000
		
			// 容器尺寸
			this.noticeContainer = {
				width: width - left * 2,
				height: height,
				top: windowInfo.statusBarHeight,
				left: left
			}
			
			// 图片尺寸
			this.noticeImage = {
				top: margin,
				left: margin,
				width: height - margin * 2,
				height: height - margin * 2
			}
			
			// 标题尺寸
			this.noticeTitle = {
				top: margin * 1.5,
				left: this.noticeImage.width + margin * 2,
				width: width - (this.noticeImage.width + margin * 3 + margin),
				height: height / 2,
				size: 17
			}
			
			// 内容尺寸
			this.noticeContent = {
				top: height / 2 + margin / 2,
				left: this.noticeImage.width + margin * 2,
				width: width - (this.noticeImage.width + margin * 3 + margin),
				height: height / 2,
				size: 16
			}
			
			
			// 创建原生View对象
			this.view = new (plus as any).nativeObj.View('messageNotify', {
				top: this.noticeContainer.top + 'px',
				left: this.noticeContainer.left + 'px',
				width: this.noticeContainer.width + 'px',
				height: this.noticeContainer.height + 'px'
			});
			
			
			// 滑动元数据
			this.draggingMeta = {noticeIsdragging: false, startX: 0, startY: 0, maxTop: windowInfo.screenHeight * (1 / 3), currentTop: this.noticeContainer.top}
			
			// 添加点击、滑动事件
			this.addEventListener(() => {
				if (this.clickEvent) {
					// 滑动过程中、startX、startY 为 0 都无法触发点击事件；触发点击事件移动需要先触发touchstart事件
					if (this.draggingMeta.noticeIsdragging || this.draggingMeta.startX === 0 || this.draggingMeta.startY === 0) {
						return
					}
					// 触发业务点击
					this.clickEvent()
				}
				// 关闭通知
				this.hide()
			}, (direction) => {
				// 触发业务滑动
				if (this.moveEndEvent) {
					this.moveEndEvent(direction)
				}
			})
			
			// 监听主题变化
			this.watchTheme()
			
			// 根据当前主题赋值颜色
			if (this.theme === 'light') {
				this.color = {
					backageColor: '#e5e5e5',
					titleColor: '#000',
					contentColor: 'rgba(0, 0, 0, 0.45)',
					maskColor: 'rgba(0,0,0,0)',
				}
			} else {
				this.color = {
					backageColor: '#2b2b2b',
					titleColor: '#fff',
					contentColor: 'rgba(255, 255, 255, 0.45)',
					maskColor: 'rgba(0,0,0,0.2)',
				}
			}
		} catch(e) {
			console.error("创建消息通知出错，可能是当前系统不兼容原生对象")
			console.error(e)
		}
	}
	
	/**
	 * 显示弹窗
	 */
	public show = (notifyContent: NotifyContent, clickCallback?: () => void, moveEndCallback?: (direction: 'right' | 'left' | 'bottom' | 'top') => void) => {
		const {title, content, image, duration} = notifyContent
		if (!content) {
			throw new Error("通知内容不存在")
		}
		// 赋值消息内容
		this.notifyContent = notifyContent
		// 赋值自动消失时间
		if (duration) {
			this.duration = duration
		}
		// 赋值事件
		this.clickEvent = clickCallback
		this.moveEndEvent = moveEndCallback
		
		// 在拖动过程中有新消息，直接重绘
		if (this.draggingMeta.noticeIsdragging) {
			this.view.reset()
			this.drawNotice(title || defaultTitle, content, image || defaultImage)
			return
		}
		
		const step = this.noticeContainer.top / 10
		
		// 先关闭已存在的消息再打开
		this.hide().then(() => {
			// 设置动画开始时top值和透明度
			this.view.setStyle({top: '0px', opacity: 0, left: this.noticeContainer.left + 'px'})
			// 绘制通知
			this.drawNotice(title || defaultTitle, content, image || defaultImage)
			// 显示通知（窗口在屏幕外，并且透明度为0）
			this.view.show()
			this.noticeIsShow = true
			// 执行动画，由上向下滑落，减小透明度
			let top = 0
			let opacity = 0
			const interval = setInterval(() => {
				// top值判断动画是否结束
				if (top >= this.noticeContainer.top) {
					this.view.setStyle({top: this.noticeContainer.top + 'px', opacity: 1, left: this.noticeContainer.left + 'px'})
					clearInterval(interval)
					this.autoClose()
					return
				}
				// 每帧步进
				top = top + step
				opacity = opacity + 0.2
				// 刷新样式
				this.view.setStyle({
					top: top + 'px',
					opacity: opacity
				})
			}, 8)
		})
	}
	
	/**
	 * 关闭弹窗
	 * 使用定时器渐变消失
	 */
	public hide = () => {
		return new Promise((resolve, _reject) => {
			// 取消自动关闭
			this.cancelAutoClose()
			// 通知为关闭状态直接返回
			if (!this.noticeIsShow) {
				resolve({})
				return
			}
			let opacity = 1
			let interval = setInterval(() => {
				if (opacity <= 0) {
					clearInterval(interval)
					// 销毁并关闭组件
					this.view.reset()
					this.view.hide()
					this.noticeIsShow = false
					this.draggingMeta.noticeIsdragging = false
					resolve({})
					return
				}
				opacity = opacity - 0.2
				this.view.setStyle({
					opacity: opacity
				})
			}, 16)
		})
	}
	
	// 绘制通知
	private drawNotice = (title: string, content: string, image: string) => {
		const {backageColor, titleColor, contentColor, maskColor} = this.color
		// 绘制通知最外层content
		this.view.drawRect({color: backageColor, radius: this.radius})
		// 绘制标题
		this.view.drawText(this.textCut(title, this.noticeTitle.size, this.noticeTitle.width), {top: this.noticeTitle.top + 'px', left: this.noticeTitle.left + 'px', width: this.noticeTitle.width + 'px', height: this.noticeTitle.height + 'px'}, {align: 'left', verticalAlign: 'top', size: this.noticeTitle.size + 'px', color: titleColor})
		// 绘制内容
		this.view.drawText(this.textCut(content, this.noticeContent.size, this.noticeContent.width), {top: this.noticeContent.top + 'px', left: this.noticeContent.left + 'px', width: this.noticeContent.width + 'px', height: this.noticeContent.height + 'px'}, {align: 'left', verticalAlign: 'top', color: contentColor, size: this.noticeContent.size + 'px'})
		// 绘制左侧图片
		this.view.drawBitmap(image, {}, {top: this.noticeImage.top + 'px', left: this.noticeImage.left + 'px', width: this.noticeImage.width + 'px', height: this.noticeImage.height + 'px'})
		// 绘制图片遮罩，呈现圆角（安卓和ios渲染方式不同，根据系统类型进行调用）
		if (this.osName === 'android') {
			this.view.drawRect({color: maskColor, borderWidth: this.noticeImage.left * 1.7 + 'px', radius: this.radius, borderColor: backageColor}, {height: this.noticeContainer.height - this.noticeImage.left * 2 + 'px',width: this.noticeContainer.height - this.noticeImage.left * 2 + 'px', top: this.noticeImage.left + 'px', left: this.noticeImage.left + 'px'}, 'mask')
		} else {
			this.view.drawRect({color: maskColor, borderWidth: this.noticeImage.left + 'px' ,radius: this.radius, borderColor: backageColor}, {height: this.noticeContainer.height + 'px', width: this.noticeImage.width + this.noticeImage.left * 2 + 'px'}, 'mask')
		}
	}
	
	// 添加事件
	private addEventListener = (clickCallback: () => void, moveEndCallback: (direction: 'right' | 'left' | 'bottom' | 'top') => void) => {
		this.view.addEventListener("click", clickCallback)
		this.view.addEventListener("touchstart", this.touchStart)
		
		if (this.osName === 'android') {
			// todo 安卓手机概率性无法触发touchend
			// 等待官方解决，期间进行兼容操作，只允许向上滑动关闭和点击打开
			// 官方解决完成后，该方法直接删除即可
			this.view.addEventListener("touchmove", (event: TouchEventData) => this.androidMove(event, moveEndCallback))
		} else {
			this.view.addEventListener("touchmove", this.touchMove)
		}
		
		this.view.addEventListener("touchend", (event: TouchEventData) => this.touchEnd(event, moveEndCallback))
	}

	// 开始滑动
	private touchStart = (event: TouchEventData) => {
		this.draggingMeta.startX = event.screenX
		this.draggingMeta.startY = event.screenY
		// 取消自动关闭
		this.cancelAutoClose()
	}
	
	// 滑动过程中
	private touchMove = (event: TouchEventData) => {
		if (!this.draggingMeta.direction) {
			// 判断滑动方向
			const {screenX, screenY} = event
			const x = Math.abs(this.draggingMeta.startX - screenX)
			const y = Math.abs(this.draggingMeta.startY - screenY)
			this.draggingMeta.direction = x > y ? 'x' : 'y'
			// 修改滑动状态
			this.draggingMeta.noticeIsdragging = true
		}
		
		// 上下滑动
		if (this.draggingMeta.direction === 'y' && this.draggingMeta.startY !== 0) {
			let targetTop = this.noticeContainer.top + event.screenY - this.draggingMeta.startY
			// 滑动距离（为正数表示向下滑动）
			const specificDirection = event.screenY - this.draggingMeta.startY
			
			// 下滑，有阻尼
			if (specificDirection > 0) {
				this.draggingMeta.opacity = 1
				// 计算算上阻尼的targetTop
				// 阻尼系数，越高越容易滑动
				const DAMPING = 300;
				const damped = (specificDirection * DAMPING) / (specificDirection + DAMPING)
				targetTop = this.noticeContainer.top + damped;
			} else {
				this.draggingMeta.opacity = 1 - Math.abs(Math.trunc(specificDirection)) * 0.005
			}
			const maxTop = this.draggingMeta.maxTop
			this.draggingMeta.currentTop =  targetTop >= maxTop ? maxTop : targetTop 
			
			this.view.setStyle({
				top: this.draggingMeta.currentTop + 'px',
				opacity: this.draggingMeta.opacity
			})
		}
		
		// 左右滑动
		if (this.draggingMeta.direction === 'x' && this.draggingMeta.startX !== 0) {
			const targetLeft = this.noticeContainer.left + event.screenX - this.draggingMeta.startX
			const specificDirection = Math.abs(event.screenX - this.draggingMeta.startX)
			const opacity = 1 - Math.trunc(specificDirection) * 0.005
			this.draggingMeta.opacity = opacity
			this.view.setStyle({
				left: targetLeft + 'px',
				opacity: opacity
			})
		}
	}
	
	// 滑动结束
	private touchEnd = (event: TouchEventData, moveEndCallback: (direction: 'right' | 'left' | 'bottom' | 'top') => void) => {
		// 滑动方向
		let direction: 'right' | 'left' | 'bottom' | 'top'
		// 关闭阈值
		let threshold: number = 0
		if (this.draggingMeta.direction === 'x') {
			direction = event.screenX > this.draggingMeta.startX ? 'right' : 'left'
			threshold = 0.4
		} else {
			direction = event.screenY > this.draggingMeta.startY? 'bottom' : 'top'
			direction === 'top' ? threshold = 0.8 : threshold = 0.4
		}
		// 滑动满足阈值后即销毁关闭
		if (this.draggingMeta.opacity && this.draggingMeta.opacity < threshold) {
			this.view.reset()
			this.view.hide()
			if (moveEndCallback) {
				moveEndCallback(direction)
			}
		} else {
			// 否则复原
			this.view.setStyle({top: this.noticeContainer.top + 'px', opacity: 1, left: this.noticeContainer.left + 'px'})
			// 重新开始自动关闭
			this.autoClose()
		}
		// 向下滑动超过maxTop比例则触发回调
		if (direction === 'bottom' && this.draggingMeta.currentTop > this.draggingMeta.maxTop * (3 / 5)) {
			this.view.reset()
			this.view.hide()
			if (moveEndCallback) {
				moveEndCallback(direction)
				// 重新开始自动关闭
				this.autoClose()
			}
		}
		
		// 延迟20ms，不与点击事件打架
		setTimeout(() => {
			// 重置拖动状态
			this.draggingMeta.noticeIsdragging = false
			this.draggingMeta.startX = 0
			this.draggingMeta.startY = 0
			this.draggingMeta.direction = undefined
		}, 20)
	}
	
	// 安卓正在滑动
	// todo 安卓手机概率性无法触发touchend
	// 等待官方解决，期间进行兼容操作，只允许向上滑动关闭和点击打开
	// 官方解决完成后，该方法直接删除即可
	private androidMove = (event: TouchEventData, moveEndCallback: (direction: 'right' | 'left' | 'bottom' | 'top') => void) => {
		if (this.draggingMeta.startX === 0 || this.draggingMeta.startY === 0) {
			return
		}
		
		if (!this.draggingMeta.direction) {
			// 判断滑动方向
			const {screenX, screenY} = event
			const x = Math.abs(this.draggingMeta.startX - screenX)
			const y = Math.abs(this.draggingMeta.startY - screenY)
			this.draggingMeta.direction = x > y ? undefined : 'y'
			if (this.draggingMeta.direction) {
				this.draggingMeta.noticeIsdragging = true
			}
		}
		
		if (this.draggingMeta.direction === 'y') {
			const specificDirection = event.screenY - this.draggingMeta.startY
			this.draggingMeta.opacity = 1 - Math.abs(Math.trunc(specificDirection)) * 0.005
			// 只能向上滑动
			if (specificDirection <= 0) {
				let targetTop = this.noticeContainer.top + event.screenY - this.draggingMeta.startY
				const maxTop = this.draggingMeta.maxTop
				this.draggingMeta.currentTop =  targetTop >= maxTop ? maxTop : targetTop 
				this.view.setStyle({
					top: this.draggingMeta.currentTop + 'px',
					opacity: this.draggingMeta.opacity
				})
				
				// 向上滑动50像素就执行关闭
				if (specificDirection < - 40) {
					this.androidMoveClose(moveEndCallback);
				}
			}
		}
	}
	
	// 安卓滑动关闭
	// todo 安卓手机概率性无法触发touchend，
	// 等待官方解决，期间进行兼容操作，只允许向上滑动关闭和点击打开
	// 官方解决完成后，该方法直接删除即可
	private androidMoveClose = (moveEndCallback: (direction: 'right' | 'left' | 'bottom' | 'top') => void) => {
		// 重置拖动状态
		if (!this.draggingMeta.noticeIsdragging) {
			return
		}
		
		this.view.reset()
		this.view.hide()
		if (moveEndCallback) {
			moveEndCallback('top')
		}
		
		this.draggingMeta.noticeIsdragging = false
		this.draggingMeta.startX = 0
		this.draggingMeta.startY = 0
		this.draggingMeta.direction = undefined
	}
	
	// 截取文本
	private textCut = ( text: string, fontSize: number, contentWidth: number ) => {
	  if (!text) return ''
	
	  const ellipsis = '...'
	  const ellipsisWidth = measureTextWidth(ellipsis, fontSize)
	  contentWidth = contentWidth
	  // 宽度过小
	  if (ellipsisWidth > contentWidth) {
	    return ''
	  }
	
	  // 原文无需截取
	  if (measureTextWidth(text, fontSize) <= contentWidth) {
	    return text
	  }
	
	  let left = 0
	  let right = text.length
	  let result = ''
	
	  while (left <= right) {
	    const mid = Math.floor((left + right) / 2)
	    const slice = text.slice(0, mid)
	    const width = measureTextWidth(slice, fontSize) + ellipsisWidth
	
	    if (width <= contentWidth) {
	      result = slice
	      left = mid + 1
	    } else {
	      right = mid - 1
	    }
	  }
	
	  return result + ellipsis
	}

	// 自动关闭
	private autoClose = () => {
		// 先取消再添加
		this.cancelAutoClose()
		this.closeTimeout = setTimeout(() => {
			this.hide()
			clearTimeout(this.closeTimeout)
		}, this.duration)
	}
	
	// 取消自动关闭
	private cancelAutoClose = () => {
		if (this.closeTimeout) {
			clearTimeout(this.closeTimeout)
			this.closeTimeout = undefined
		}
	}
	
	// 监听主题变化
	private watchTheme = () => {
		uni.onThemeChange((resp) => {
			// 监听到的主题可能为auto，auto 则调用系统信息接口获取主题
			const theme = resp.theme as 'light' | 'dark' | 'auto'
			if (theme === 'auto') {
				this.theme = uni.getSystemInfoSync().theme || 'light'
			} else {
				this.theme = theme
			}
			
			if (this.theme === 'light') {
				this.color = {
					backageColor: '#e5e5e5',
					titleColor: '#000',
					contentColor: 'rgba(0, 0, 0, 0.45)',
					maskColor: 'rgba(0,0,0,0)',
				}
			} else {
				this.color = {
					backageColor: '#2b2b2b',
					titleColor: '#fff',
					contentColor: 'rgba(255, 255, 255, 0.45)',
					maskColor: 'rgba(0,0,0,0.2)',
				}
			}
			
			// 通知正在开启时变换主题，重新绘制
			if (this.noticeIsShow && this.notifyContent) {
				const {title, content, image} = this.notifyContent
				this.view.reset()
				this.drawNotice(title || defaultTitle, content, image || defaultImage)
			}
		});
	}

}

export default new MessageNotify()
