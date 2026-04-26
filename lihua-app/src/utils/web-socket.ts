import {getOnceToken} from "@/api/system/authentication/authentication";
import {getUUID} from "@/utils/uuid/uuid"
import {getClientType} from '@/utils/client'

/**
 * webSocket连接具体实现逻辑
 * 
 */
class WebSocketManager {
	private webSocket?: UniNamespace.SocketTask
	// 是否在连接状态
	private isConnected: boolean
    // 事件监听器
    private listeners?: Map<string, (data: any) => void>
    // 心跳
    private heartbeat?: any
    // 重试次数
    private retryNumber: number
    // 最大重试次数
    private maxRetryNumber: number = 3
    // 重试间隔
    private retryInterval: number = 2 * 1000
    // 是否开启重连
    private enableRetry: boolean = true

    constructor() {
        this.listeners = new Map()
        this.retryNumber = 0
		this.isConnected = false
    }

    /**
     * 建立连接
     */
    public connect = async () => {
        if (!this.webSocket) {
			try {
			    const { code, data } = await getOnceToken()
			
			    if (code !== 200 || !data) {
			        console.error("WebSocket获取连接token失败")
			        return;
			    }
			
				// 拼接连接地址
				const url = import.meta.env.VITE_APP_WS_API + '?token=' + data + '&clientId=' + await getUUID() + '&clientType=' + getClientType()
			
				// 建立连接
				this.webSocket = uni.connectSocket({
					url,
					success: () => console.log("WebSocket连接中"),
					fail: (e) => {
						console.error("WebSocket连接失败", e)
						this.reconnect()
					}
				})
				
				// 连接成功
				this.webSocket.onOpen(() => {
					console.info('WebSocket连接成功')
					this.retryNumber = 0
					this.enableRetry = true
					this.isConnected = true
					this.startHeartbeat()
				})
				
				// 连接错误
				this.webSocket.onError((err) => {
					this.isConnected = false
					this.reconnect()
					console.error('WebSocket连接错误:', err)
				})
				
				// 连接关闭
				this.webSocket.onClose((event) => {
					console.info('WebSocket连接关闭:', event)
					this.isConnected = false
					this.webSocket = undefined
					this.closeHeartbeat()
					if (this.enableRetry) {
						this.reconnect()
					}
				})
				
				// 接收消息
				this.webSocket.onMessage((res) => {
					this.receiveMessage(res)
				})
			} catch (e) {
			    console.error("websocket连接失败",e)
			}
		} else {
			console.log("当前websocket实例已存在")
		}
    }
	
	// 注册事件
	public addEventListener = (type: string, callback: (data: any) => void) => {
	    if (!this.listeners) {
	        this.listeners = new Map()
	    }
	    this.listeners.set(type, callback);
	}
	
	// 删除事件
	public removeEventListener = (type: string) => {
	    this.listeners?.delete(type)
	}
	
	// 发送数据
	public sendMessage = (type: string, data: any): Promise<boolean> => {
	    return new Promise((resolve, reject) => {
			if (!this.isReady()) {
			    console.warn("WebSocket 未连接，消息发送失败")
			    return resolve(false)
			}
			try {
			    const json = JSON.stringify({type, data, timestamp: new Date().getTime()})
			    uni.sendSocketMessage({
			    	data: json,
					success: () => resolve(true),
					fail: (error) => {
						console.error("WebSocket消息发送失败", error);
						resolve(false)
					}
			    })
			} catch (error) {
				reject(error)
			    console.error("WebSocket消息发送失败，无法处理的消息格式", error);
			}
		})
	}
	
	// 主动关闭连接
	public closeConnect = () => {
		console.log("WebSocket主动关闭")
	    this.enableRetry = false
		this.webSocket?.close({code: 1000})
		this.webSocket = undefined
	}

    // 重试连接
    private reconnect = () => {
        if (this.retryNumber >= this.maxRetryNumber) {
            console.error("websocket 超过重试次数")
            return
        }
		this.webSocket = undefined
        this.retryNumber ++
        setTimeout(() => {
            console.log("websocket 执行第" + this.retryNumber + "次重试")
            this.connect()
        }, this.retryNumber * this.retryInterval)
    }

    // 接收数据
    private receiveMessage = (event: UniNamespace.OnSocketMessageCallbackResult) => {
        const data = event.data
        if (typeof data === "string") {
            try {
                // json转换为对象
                const webSocketMessage: WebSocketMessage = JSON.parse(data)
                // 从注册的事件中拿到对象
                const listener = this.listeners?.get(webSocketMessage.type)
                if (listener) {
                    listener(webSocketMessage.data)
                }
            } catch (error) {
                console.error("WebSocket消息处理失败，无法处理的消息格式", error);
            }
        } else {
            // 非文本消息
            console.error("WebSocket消息处理失败，无法处理的消息格式");
        }
    }

    // 开启心跳
    private startHeartbeat = () => {
        this.closeHeartbeat()
        this.heartbeat = setInterval(() => {
            if (this.isReady()) {
                this.sendMessage("WS_HEARTBEAT", "ping")
            }
        }, 1000 * 30)
    }

    // 关闭心跳
    private closeHeartbeat = () => {
        if (this.heartbeat) {
            clearInterval(this.heartbeat);
        }

        this.heartbeat = undefined
    }

    // websocket 是否准备就绪
    private isReady = (): boolean => {
        return this.isConnected;
    }
}

/**
 * WebSocket 接收消息类型
 */
interface WebSocketMessage {
    type: string;
    data: string;
    timestamp: number;
}

export const webSocket = new WebSocketManager()
