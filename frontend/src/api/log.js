import request from '@/utils/request'

export function log() {
  return {
    logId: null,
    message: null,
    type: '',
    level: null,
    createTime: null
  }
}

export function findAll(params) {
  return request({
    url: '/log',
    method: 'get',
    params: params
  })
}

export function deleteLog(logId) {
  return request({
    url: '/log/' + logId,
    method: 'delete'
  })
}

export function getFilter() {
  return request({
    url: '/log/filter',
    method: 'get'
  })
}

export function deleteAll() {
  return request({
    url: '/log',
    method: 'delete'
  })
}
