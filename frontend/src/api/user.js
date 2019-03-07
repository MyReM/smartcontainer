import request from '@/utils/request'

export function user() {
  return {
    userId: null,
    userCode: null,
    userName: null,
    password: null,
    confirmPassword: null,
    description: '',
    sex: null,
    birthday: null,
    status: null,
    avator: null,
    createTime: null
  }
}

export function getUserList(params) {
  return request({
    url: '/user',
    method: 'get',
    params: params
  })
}

export function findAll(user) {
  return request({
    url: '/user/list',
    method: 'get',
    params: user
  })
}

export function findOne(userId) {
  return request({
    url: '/user/' + userId,
    method: 'get'
  })
}

export function exist(userCode) {
  return request({
    url: '/user/exist',
    method: 'get',
    params: userCode
  })
}

export function findByDeptId(deptId, params) {
  return request({
    url: '/user/deptId/' + deptId,
    method: 'get',
    params: params
  })
}

export function findByRoleId(roleId, params) {
  return request({
    url: '/user/roleId/' + roleId,
    method: 'get',
    params: params
  })
}

export function findDeptByUserId(userId) {
  return request({
    url: '/user/dept/' + userId,
    method: 'get'
  })
}

export function findRoleByUserId(userId) {
  return request({
    url: '/user/role/' + userId,
    method: 'get'
  })
}

export function createUser(user) {
  return request({
    url: '/user',
    method: 'post',
    data: user
  })
}

export function updateUser(user) {
  return request({
    url: '/user',
    method: 'put',
    data: user
  })
}

export function updateDept(userId, deptId) {
  return request({
    url: '/user/' + userId + '/' + deptId,
    method: 'put'
  })
}

export function updateRole(userId, roleId) {
  return request({
    url: '/user/role',
    method: 'put',
    data: { 'userId': userId, 'roleId': roleId }
  })
}

export function deleteUser(userId) {
  return request({
    url: '/user/' + userId,
    method: 'delete'
  })
}

export function deptTree() {
  return request({
    url: '/dept/tree',
    method: 'get'
  })
}
