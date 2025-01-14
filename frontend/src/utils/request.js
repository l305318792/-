import axios from "axios"
import {getToken} from "@/utils/storage.js";

//全局定义一个单例的axios对象
const request = axios.create({
  baseURL: "http://localhost:9281",
  timeout: 8000,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  }
});

//全局拦截器，所有请求都会先执行这个
request.interceptors.request.use(
  config => {
    // 如果有token，添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = token
    }
    
    // 如果是登录或找回密码请求，使用 x-www-form-urlencoded 格式
    if (config.url.includes('/login') || config.url.includes('/findPassword')) {
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded'
      if (config.data && !(config.data instanceof URLSearchParams)) {
        config.data = new URLSearchParams(config.data)
      }
    } else if (config.data instanceof FormData) {
        // 如果是FormData，不设置Content-Type，让浏览器自动设置
        delete config.headers['Content-Type'];
    } else {
        // 其他请求使用 JSON 格式
        config.headers['Content-Type'] = 'application/json'
    }
    
    return config
  },
  error => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 添加响应拦截器
request.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (error.response) {
      // 请求已发出，但服务器响应的状态码不在 2xx 范围内
      console.error('Response error:', error.response.status, error.response.data);
      // 打印详细的错误信息
      if (error.response.data && error.response.data.message) {
        console.error('Error message:', error.response.data.message);
      }
    } else {
      // 请求未发出就发生了错误
      console.error('Request error:', error.message);
    }
    return Promise.reject(error);
  }
);

// 导出axios对象
export default request;