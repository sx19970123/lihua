import type {ResponseType} from "@/api/global/Type.ts";
import {message} from "ant-design-vue";

export const download = (fun: Promise<ResponseType<string> & Blob>) => {
    fun.then(resp => {
        if (resp.code === 200) {
            handleDownload(resp.data, "test.xlsx")
        } else {
            message.error(resp.msg)
        }
    })
}

const handleDownload = (url:string, filename:string) => {
    const link = document.createElement('a');
    link.href = 'http://localhost:8080/system/file/download/' + url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    console.log(import.meta.env.VITE_APP_BASE_API + '/system/user/download/' + url)
}