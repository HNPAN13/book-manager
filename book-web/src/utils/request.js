import axios from 'axios'
const request = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // Session跨域携带Cookie，必须开启
  timeout: 5000
})

// 请求拦截器：当前项目用Session，不需要token，注释掉token相关
request.interceptors.request.use(config => {
  // const token = localStorage.getItem('token')
  // if (token) {
  //   config.headers.token = token
  // }
  return config
})

// 响应拦截器：统一处理返回、401未登录
request.interceptors.response.use(
  res => {
    return res
  },
  err => {
    console.error("请求异常：", err)
    if (err.response?.status === 401) {
      // Session模式不需要清除token，保留页面跳转即可
      location.href = '/login'
    }
    return Promise.reject(err)
  }
)
export default request
