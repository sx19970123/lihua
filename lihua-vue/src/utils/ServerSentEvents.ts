import {handleClose} from "@/api/system/sse/ServerSentEvents.ts";

export let eventSource: EventSource

// 连接到SSE服务
export const connect = () => {
    if (!eventSource) {
        console.log("Server-Sent Events 开始连接")
        eventSource = new EventSource(import.meta.env.VITE_APP_BASE_API + "/system/sse/connect")
    }

    eventSource.onerror = (event) => {
        console.log("Server-Sent Events 连接中断", event)
    }
}

// 关闭SSE服务连接
export const close = async () => {
    if (eventSource) {
        eventSource.close()
        await handleClose()
        console.log("Server-Sent Events 关闭连接")
    }
}

// 处理SSE返回数据，从callback函数中获取
export const handleSseMessage = (callback: Function) => {
    if (eventSource) {
        eventSource.onmessage = (event) => {
            callback(event.data as string)
        }
    }
}