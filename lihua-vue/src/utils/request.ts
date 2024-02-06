import axios from "axios"
import token from "@/utils/token"
const { getToken , removeToken } = token

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
service.interceptors.response.use(resp => {
    const data = resp.data
    // token 失效或解析异常，删除缓存token
    if (data.code === 502 || data.code === 503) {
        removeToken()
    }
    return Promise.resolve(data);
})


export default service;
