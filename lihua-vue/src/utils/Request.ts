import axios, {type AxiosRequestConfig, type AxiosResponse} from 'axios';
import token from "@/utils/Token.ts"
import {type ResponseErrorType, ResponseError, type ResponseType} from "@/api/global/Type.ts"
import { useUserStore } from "@/stores/user";
import router from "@/router";
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
        config.headers['Authorization'] = 'Bearer ' + getToken()
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
        // 清空用户信息
        const userStore= useUserStore()
        userStore.clearUserInfo()
        // 路由跳转到登录页
        router.push({name: 'Login', state: {msg: data.msg}})
        throw data.msg
    }
    // 配置的非法ip访问
    if (data.code === 407) {
        router.push("/407")
        throw data.msg
    }
    // 服务器处理文件异常，提示异常信息
    if (data.code === 501) {
        new ResponseError(data.code, data.msg)
    }
    return resp;
}, error => {
    // 处理错误响应
    if (error.response) {
        // Nginx 返回的错误响应会带有状态码
        const status = error.response.status;
        switch (status) {
            case 404:
                console.error("资源未找到 (404)");
                break;
            case 413:
                console.error("请求体超过限制大小 (413)");
                break;
            case 500:
                console.error("服务器异常 (500)");
                break;
            case 502:
                console.error("网关错误 (502)");
                break;
            case 504:
                console.error("网关超时 (504)");
                break;
            default:
                console.error(`其他错误 (${status})`);
        }
        return Promise.reject(new ResponseError(status, error.response.statusText));
    } else {
        const msg = "无法连接到服务器或服务器响应超时"
        console.error(msg);
        return Promise.reject(new ResponseError(500, msg));
    }
})

// 数据返回统一封装样式
export default <T> (config: AxiosRequestConfig) => {
    return new Promise<ResponseType<T> & Blob>((resolve, reject) => {
        service
          .request<ResponseType<T> & Blob>(config)
          .then((response: AxiosResponse<ResponseType<T> & Blob>) => {
              resolve(response.data)
          })
          .catch((err: ResponseErrorType) => {
              reject(err);
          });
    });
};
