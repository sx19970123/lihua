import type {ResponseType} from "@/api/global/Type.ts";
import {message} from "ant-design-vue";
import Spin from '@/components/spin';

// 传入请求下载接口的异步函数，自动处理下载
export const handleFunDownload = (fun: Promise<ResponseType<string> & Blob>, split?: string) => {
    const spinIntance= Spin.service()
    fun.then(resp => {
        if (resp.code === 200) {
            download(resp.data, split)
        } else {
            message.error(resp.msg)
        }
        spinIntance.close()
    })
}

// 通过url下载文件
const download = (url: string, split?: string) => {
    const linkElement = document.createElement('a');
    let baseDownload =  import.meta.env.VITE_APP_BASE_API + '/system/file/download/' + url
    linkElement.href = split ? baseDownload + '?split=' + split : baseDownload;
    document.body.appendChild(linkElement);
    linkElement.click();
    document.body.removeChild(linkElement);
}