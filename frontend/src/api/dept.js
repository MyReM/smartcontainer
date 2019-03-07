import request from '@/utils/request'

export function dept() {
  return {
    deptId: null,
    simpleName: null,
    fullName: null,
    comment: null,
    status: null,
    level: null,
    parentId: null
  }
}

export function findOneDept(deptId) {
  return request({
    url: '/dept/' + deptId,
    method: 'get'
  })
}

export function findAllDept(params) {
  return request({
    url: '/dept',
    method: 'get',
    params: params
  })
}

export function insertDept(dept) {
  return request({
    url: '/dept',
    method: 'post',
    data: dept
  })
}

export function updateDept(dept) {
  return request({
    url: '/dept',
    method: 'put',
    data: dept
  })
}

export function findDeptTree() {
  return request({
    url: '/dept/tree',
    method: 'get'
  })
}

export function deleteDept(deptId) {
  return request({
    url: '/dept/' + deptId,
    method: 'delete'
  })
}
