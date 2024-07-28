import {handleClose} from "@/api/system/sse/ServerSentEvents.ts";
import {useUserStore} from "@/stores/modules/user.ts";
import token from "@/utils/Token.ts"
const { getToken } = token
export let eventSource: EventSource | null

// 连接到SSE服务
export const connect = () => {
    if (!eventSource) {
        const userStore = useUserStore()
        console.log("Server-Sent Events 开始连接")
        eventSource = new EventSource(import.meta.env.VITE_APP_BASE_API + "/system/sse/connect/" + userStore.userId)
    }

    eventSource.onerror = (event) => {
        console.log("Server-Sent Events 连接中断", event)
    }
}

// 关闭SSE服务连接
export const close = async () => {
    if (eventSource) {
        const userStore = useUserStore()
        console.log("Server-Sent Events 关闭连接")
        // 关闭 eventSource 连接
        eventSource.close()
        eventSource = null
        // 主动退出情况下，通知后端释放sse资源
        if (getToken() && userStore.userId) {
            await handleClose()
        }
    }
}

// 处理SSE返回数据，从callback函数中获取
export const handleSseMessage = <T> (callback: MessageCallbackType<T>) => {
    if (eventSource) {
        eventSource.onmessage = (event) => {
            const data = JSON.parse(event.data) as SSEResponseType<T>
            callback(data)
        }
    }
}

// sse 消息callback 函数类型
export type MessageCallbackType<T> = (response: SSEResponseType<T>) => void

// 处理SSE返回类型
export interface SSEResponseType<T> {
    type: string;
    data: T;
}