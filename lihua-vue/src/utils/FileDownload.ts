import { message } from "ant-design-vue";
import Spin from '@/components/spin';
import {ResponseError} from "@/api/global/Type.ts";

// http正则表达式
const httpRegex = /^(https?:\/\/)[^\s$.?#].[^\s]*$/;

// 可选下载参数，针对不同情况
type DownloadParam = {
    // 文件名称（带后缀）
    fileName?: string,
    // 文件路径分割符
    split?: string
}

// 传入请求下载接口的异步函数，自动处理下载
export const handleFunDownload = (fun: Promise<any>, param?: DownloadParam) => {
    const spinInstance = Spin.service({
        tip: '努力加载中...'
    });

    fun.then((resp) => {
        // 函数返回值如果为二进制，则转为url进行下载
        // 需要在api中指定responseType:'blob'
        if (resp instanceof Blob) {
            downloadByBlob(resp, param?.fileName);
        } else {
            const response = resp;
            if (response.code === 200) {
                const data = response.data;
                if (httpRegex.test(data)) {
                    download(data, param?.fileName);
                } else {
                    downloadByPath(data);
                }
            } else {
                message.error(response.msg);
            }
        }
    }).catch((e) => {
        if (e instanceof ResponseError) {
            message.error(e.msg)
        } else {
            console.error(e)
        }
    }).finally(() => {
        spinInstance.close();
    })
}

// 通过文件路径下载
export const downloadByPath = (filePath: string) => {
    const downURL = "" + encodeURIComponent(filePath);
    download(downURL);
}

// 通过blob下载
export const downloadByBlob = (blob: Blob, fileName?: string) => {
    const url = URL.createObjectURL(blob);
    download(url, fileName);
}

// 通过url地址下载
export const download = (url: string, fileName?: string) => {
    const linkElement = document.createElement('a');
    linkElement.href = url;
    if (fileName) {
        linkElement.download = fileName;
    }
    document.body.appendChild(linkElement);
    linkElement.click();
    document.body.removeChild(linkElement);
}
