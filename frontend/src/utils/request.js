import axios from 'axios'
import qs from 'qs'
import Vue from 'vue'
import { Message, MessageBox } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'

Vue.prototype.$http = axios
const cancelToken = axios.CancelToken
const source = cancelToken.source()
// source.cancel('操作被用户取消')

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  // timeout: 15000, // 请求超时时间,
  cancelToken: source.token
})

// request拦截器
service.interceptors.request.use(config => {
  if (store.getters.token) {
    config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  }
  const method = config.method.toUpperCase()
  if (method === 'POST' || method === 'PUT') {
    if (config.headers['Content-Type'] === undefined) {
      config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    } else {
      config.data = qs.stringify(config.data, { allowDots: true })
    }
  }

  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    } else if (res.code === 302) {
      MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('FedLogOut').then(() => {
          location.reload()// 为了重新实例化vue-router对象 避免bug
        })
      })
      return res
    } else {
      const message = (res.message === undefined || res.message === '' || res.message === null)
      Message({ message: message ? '似乎服务器出了点小差错！' : res.message, type: 'error' })
      return Promise.reject(response)
    }
    // return Promise.reject('error')
  },
  error => {
    console.log('err' + error)// for debug
    let message = null
    if (error.message.indexOf('Network Error') !== -1) {
      message = '与服务器断开连接！'
    } else if (error.message.indexOf('timeout of 15000ms') !== -1) {
      message = '请求或者响应超时，请重试！'
    } else {
      message = '服务器内部运行出错！'
    }
    Message({ message: message, type: 'error' })
    return Promise.reject(error)
  }
)

export default service
