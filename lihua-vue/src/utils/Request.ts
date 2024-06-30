import axios, {type AxiosRequestConfig, type AxiosResponse} from 'axios';
import token from "@/utils/Token.ts"
import type {ResponseType} from "@/api/global/Type.ts"
import { useUserStore } from "@/stores/modules/user";
import {message} from "ant-design-vue";

const { getToken } = token

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
const service = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    timeout: 10000
});


// 请求拦截器
service.interceptors.request.use(config => {
    // 每次请求将 token 设置到请求头
    if (getToken()) {
        config.headers['token'] =  getToken()
    }
    return config;
}, error => {
    Promise.reject(error).then(r => {})
})

// 响应拦截器
service.interceptors.response.use((resp) => {
    const data = resp.data
    // token 失效或解析异常，清空用户信息返回登陆
    if (data.code === 401) {
        const userStore= useUserStore()
        userStore.clearUserInfo(data.msg)
        throw data.msg
    }
    // 服务器处理文件异常，提示异常信息
    if (data.code === 501) {
        message.error(data.msg)
    }
    return resp;
})

// 数据返回统一封装样式
// function request<T>(config: AxiosRequestConfig): Promise<ResponseType<T>>;
// function request(config: AxiosRequestConfig): Promise<Blob>;
// function request<T>(config: AxiosRequestConfig) {
//     return new Promise<ResponseType<T> & Blob>((resolve, reject) => {
//         service
//             .request<ResponseType<T> & Blob>(config)
//             .then((response: AxiosResponse<ResponseType<T> & Blob>) => {
//                 resolve(response.data)
//             })
//             .catch((err) => {
//                 reject(err);
//             });
//     });
// }
// export default request

// 数据返回统一封装样式
export default <T> (config: AxiosRequestConfig) => {
    return new Promise<ResponseType<T> & Blob>((resolve, reject) => {
        service
          .request<ResponseType<T> & Blob>(config)
          .then((response: AxiosResponse<ResponseType<T> & Blob>) => {
              resolve(response.data)
          })
          .catch((err) => {
              reject(err);
          });
    });
};
