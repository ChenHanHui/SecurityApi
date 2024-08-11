import axios from 'axios'
import {ElLoading, ElMessage, ElNotification} from 'element-plus'
import errorCode from '@/utils/errorCode'
import {blobValidate, tansParams} from '@/utils/index'
import cache from '@/plugins/cache'
import {saveAs} from 'file-saver'
import {decrypt, encrypt, sign, verify} from '@/utils/encrypt/rsaEncrypt'
import {generateHash} from "@/utils/encrypt/hashEncrypt"
import cloneDeep from 'lodash/cloneDeep'

let downloadLoadingInstance

// 是否需要token
const isToken = true
// 是否需要防止数据重复提交
const isRepeatSubmit = false
// 是否启用RSA签名
const isRsaSign = true

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: '/api',
  // 超时
  timeout: 10000
})

// request拦截器
service.interceptors.request.use(async config => {
  // 是否需要设置 token
  // if (isToken) {
  //   config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  // }
  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.params = {}
    config.url = url
  } else if ((config.method === 'post' || config.method === 'put')) {
    const requestObj = {
      url: config.url,
      data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
      time: new Date().getTime()
    }
    if (isRepeatSubmit) {
      const sessionObj = cache.session.getJSON('sessionObj')
      if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
        const requestObjClone = cloneDeep(requestObj)
        if (requestObjClone.data) {
          requestObjClone.data = await generateHash(requestObjClone.data) // 将数据生成哈希值
        }
        const requestSize = Object.keys(JSON.stringify(requestObjClone)).length // 请求数据大小
        const limitSize = 5 * 1024 * 1024 // 限制存放数据5M
        if (requestSize >= limitSize) {
          console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
        } else {
          cache.session.setJSON('sessionObj', requestObjClone)
        }
      } else {
        const requestObjClone = cloneDeep(requestObj)
        if (requestObjClone.data) {
          requestObjClone.data = await generateHash(requestObjClone.data) // 将数据生成哈希值
        }
        const s_url = sessionObj.url // 请求地址
        const s_data = sessionObj.data // 请求数据
        const s_time = sessionObj.time // 请求时间
        const interval = 1000 // 间隔时间(ms)，小于此时间视为重复提交
        if (s_data === requestObjClone.data && requestObjClone.time - s_time < interval && s_url === requestObjClone.url) {
          const message = '数据正在处理，请勿重复提交'
          console.warn(`[${s_url}]: ` + message)
          return Promise.reject(new Error(message))
        }
        const requestSize = Object.keys(JSON.stringify(requestObjClone)).length // 请求数据大小
        const limitSize = 5 * 1024 * 1024 // 限制存放数据5M
        if (requestSize >= limitSize) {
          console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
        } else {
          cache.session.setJSON('sessionObj', requestObjClone)
        }
      }
    }
    if (config.encryption === true) {
      if (!requestObj.data) {
        const message = '接口数据已开启加密，提交的数据为空'
        console.warn(`[${requestObj.url}]: ` + message)
        return Promise.reject(new Error(message))
      }
      const content = encrypt(requestObj.data)
      if (isRsaSign) {
        const signText = sign(content)
        config.data = {
          content,
          'sign': signText
        }
      } else {
        config.data = {
          content
        }
      }
    }
  }
  return config
}, error => {
    console.log(error)
    Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
    // 二进制数据则直接返回
    if (res.request.responseType ===  'blob' || res.request.responseType ===  'arraybuffer') {
      return res.data
    }
    // 未设置状态码则默认成功状态
    const code = res.status !== 200 ? res.status : res.data.code
    if (code !== 200) {
      // 获取错误信息
      const message = res.data.message || errorCode[code] || errorCode['default']
      if (code === 400) {
        ElMessage({ message: message, type: 'warning' })
      } else {
        ElNotification.error({ title: message })
        return Promise.reject('error')
      }
    }
    if (res.data.encryption === true) {
      if (!res.data.data) {
        return Promise.resolve(res.data)
      }
      if (isRsaSign) {
        if (!res.data.sign) {
          const message = '响应的签名数据为空'
          console.warn(message)
          return Promise.reject(new Error(message))
        }
        const v = verify(res.data.data, res.data.sign)
        if (!v) {
          const message = '验签未通过，数据被篡改！'
          console.warn(message)
          return Promise.reject(new Error(message))
        }
      }
      res.data.data = decrypt(res.data.data)
      return Promise.resolve(res.data)
    }
    return Promise.resolve(res.data)
  },
  error => {
    console.log('err: ' + error)
    let { message } = error
    if (message === "Network Error") {
      message = "后端接口连接异常"
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时"
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常"
    }
    ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
    return Promise.reject(error)
  }
)

// 通用下载方法
export function download(url, params, filename, config) {
  downloadLoadingInstance = ElLoading.service({ text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)", })
  return service.post(url, params, {
    transformRequest: [(params) => { return tansParams(params) }],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }).then(async (data) => {
    const isBlob = blobValidate(data)
    if (isBlob) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text()
      const rspObj = JSON.parse(resText)
      const errMsg = errorCode[rspObj.code] || rspObj.message || errorCode['default']
      ElMessage.error(errMsg)
    }
    downloadLoadingInstance.close()
  }).catch((r) => {
    console.error(r)
    ElMessage.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close()
  })
}

export default service
