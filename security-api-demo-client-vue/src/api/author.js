import request from '@/utils/request'

const baseUrl = '/author'

/**
 * 列表
 */
export function list(params) {
  return request({ url: '/list', method: 'get', params })
}

/**
 * 直接返回对象，不加密
 */
export function get(data) {
  return request({ url: `${baseUrl}/get`, method: 'post', data })
}

/**
 * 请求加密，响应加密
 */
export function inDecodeOutEncode(data) {
  return request({ url: `${baseUrl}/inDecodeOutEncode`, method: 'post', data, inDecode: true, outEncode: true })
}

export default { get, inDecodeOutEncode }