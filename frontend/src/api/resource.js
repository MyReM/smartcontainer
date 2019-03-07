import request from '@/utils/request'

export function resource() {
  return {
    resourceId: null,
    resourceName: null,
    resourceValue: null,
    comment: null
  }
}

export function findAll(params) {
  return request({
    url: '/resource',
    method: 'get',
    params: params
  })
}

export function insert(resource) {
  return request({
    url: '/resource',
    method: 'post',
    data: resource
  })
}

export function update(resource) {
  return request({
    url: '/resource',
    method: 'put',
    data: resource
  })
}

export function deleted(resourceId) {
  return request({
    url: '/resource/' + resourceId,
    method: 'delete'
  })
}
