import {handleClose} from "@/api/system/sse/ServerSentEvents.ts";
import {useUserStore} from "@/stores/modules/user.ts";
import token from "@/utils/Token.ts"
import {createBrowserId} from "@/utils/BrowserId.ts";
const { getToken } = token
export let eventSource: EventSource | null

// 连接到SSE服务
export const connect = async () => {
    if (!eventSource) {
        console.log("Server-Sent Events 连接成功")
        eventSource = new EventSource(import.meta.env.VITE_APP_BASE_API + "/system/sse/connect/" + await createClientKey())
    }

    eventSource.onerror = (event) => {
        console.log("Server-Sent Events 连接中断", event)
    }

    eventSource.onopen = (event: Event) => {
        setTimeout(() => {
            console.log("Server-Sent Events 重连成功", event)
        },500)
    }
}

// 关闭SSE服务连接
export const close = async () => {
    if (eventSource) {
        console.log("Server-Sent Events 关闭连接")
        // 关闭 eventSource 连接
        eventSource.close()
        eventSource = null
        // 主动退出情况下，通知后端释放sse资源
        if (getToken()) {
            await handleClose(await createClientKey())
        }
    }
}

// 创建客户端key
const createClientKey = async () => {
    // 获取浏览器id
    const browserId = await createBrowserId()
    const userStore = useUserStore()
    return userStore.userId + ":" + browserId
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