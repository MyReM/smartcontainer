import request from '@/utils/request'

export function permission() {
  return {
    permissionId: null,
    permissionCode: null,
    permissionName: null,
    comment: null
  }
}

export function findAllPage(params) {
  return request({
    url: '/permission/page',
    method: 'get',
    params: params
  })
}

export function findAllList(params) {
  return request({
    url: '/permission/list',
    method: 'get',
    params: params
  })
}

export function insert(permission) {
  return request({
    url: '/permission',
    method: 'post',
    data: permission
  })
}

export function update(permission) {
  return request({
    url: '/permission',
    method: 'put',
    data: permission
  })
}

export function deleted(permissionId) {
  return request({
    url: '/permission/' + permissionId,
    method: 'delete'
  })
}
