import {handleClose} from "@/api/system/sse/ServerSentEvents.ts";
import {useUserStore} from "@/stores/user.ts";
import token from "@/utils/Token.ts"
import {createBrowserId} from "@/utils/BrowserId.ts";
import {ResponseError} from "@/api/global/Type.ts";
import {message} from "ant-design-vue";
import {nextTick} from "vue";
const { getToken } = token
export let eventSource: EventSource | null
// 重连次数
let reconnectionFrequency = 0
// 连接到SSE服务
export const connect = async () => {
    if (!eventSource && !window.location.href.includes("miniWindow=true")) {
        eventSource = new EventSource(import.meta.env.VITE_APP_BASE_API + "/system/sse/connect/" + await createClientKey())
        await nextTick(() => {
            if (eventSource && eventSource.readyState === 0) {
                console.log("Server-Sent Events 连接成功")
            } else {
                console.error("Server-Sent Events 连接失败，eventSource：", eventSource)
            }
        })

        eventSource.onerror = (event) => {
            console.error("Server-Sent Events 连接中断", event)
        }

        eventSource.onopen = () => {
            if (reconnectionFrequency === 3) {
                console.log("Server-Sent Events 超出重连次数")
                close()
            } else {
                reconnectionFrequency++
                console.log(`Server-Sent Events 重新连接（${reconnectionFrequency}）`)
            }
        }
    }
}

// 关闭SSE服务连接
export const close = async () => {
    if (eventSource) {
        console.log("Server-Sent Events 关闭连接")
        // 关闭 eventSource 连接
        eventSource.close()
        eventSource = null
        // 通知后端释放sse资源
        if (getToken()) {
           try {
               await handleClose(await createClientKey())
           } catch (e) {
               if (e instanceof ResponseError) {
                   message.error(e.msg)
               } else {
                   console.error(e)
               }
           }
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