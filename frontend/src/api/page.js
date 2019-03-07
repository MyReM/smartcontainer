import request from '@/utils/request'

export function druid() {
  return request({
    url: '/page/druid',
    method: 'get',
    headers: {
      'Content-Type': 'text/html;Charset=UTF-8',
      accept: 'text/html, text/plain'
    }
  })
}
