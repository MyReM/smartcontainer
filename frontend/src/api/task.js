import request from '@/utils/request'

export const task = {
  tasklId: null,
  fullCode: null,
  labelId: null,
  userId: null,
  status: null,
  updateTime: null
}

export function findAll(params) {
  return request({
    url: '/task',
    method: 'get',
    params: params
  })
}

export function findOne(fullCode) {
  return request({
    url: '/task/findByFullCode',
    method: 'get',
    params: { fullCode: fullCode }
  })
}

export function patch(fullCode, printerId) {
  return request({
    url: '/task/patchByFullCode',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: {
      fullCode: String(fullCode),
      printerId: printerId
    }
  })
}

export function update(fullCode) {
  return request({
    url: '/task',
    method: 'post',
    data: { fullCode: fullCode }
  })
}
