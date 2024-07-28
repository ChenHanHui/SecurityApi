import request from '@/utils/request'

const baseUrl = '/author'

// 说明：将请求参数加密，只需设置encryption为true即可，响应参数是否需要解密由拦截器自动控制

/**
 * 请求不加密，（响应解密，拦截器自动控制）
 */
export function get(params) {
  return request({ url: `${baseUrl}/get`, method: 'get', params })
}

/**
 * 请求不加密，（响应解密，拦截器自动控制）
 */
export function outEncode(data) {
  return request({ url: `${baseUrl}/outEncode`, method: 'post', data })
}

/**
 * 请求加密（响应解密，拦截器自动控制）
 */
export function inDecodeOutEncode(data) {
  return request({ url: `${baseUrl}/inDecodeOutEncode`, method: 'post', data, encryption: true })
}

/**
 * 请求加密（响应不需要解密，拦截器自动控制）
 */
export function inDecode(data) {
  return request({ url: `${baseUrl}/inDecode`, method: 'post', data, encryption: true })
}

/**
 * 后端获取请求参数解密前后数据
 */
export function originalData(data) {
  return request({ url: `${baseUrl}/originalData`, method: 'post', data, encryption: true })
}

export default { get, outEncode, inDecodeOutEncode, inDecode, originalData }