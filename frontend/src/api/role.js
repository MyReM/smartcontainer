import request from '@/utils/request'

export function role() {
  return {
    roleId: null,
    roleName: null,
    parentId: null,
    comment: '',
    status: null,
    level: null,
    createTime: null
  }
}

export function findOneRole(deptId) {
  return request({
    url: '/role/' + deptId,
    method: 'get'
  })
}

export function findAllRolePage(params) {
  return request({
    url: '/role/page',
    method: 'get',
    params: params
  })
}

export function findRoleTree() {
  return request({
    url: '/role/tree',
    method: 'get'
  })
}

export function insertRole(role) {
  return request({
    url: '/role',
    method: 'post',
    data: role
  })
}

export function updateRole(role) {
  return request({
    url: '/role',
    method: 'put',
    data: role
  })
}

export function allotPermission(roleId, permissionList) {
  return request({
    url: '/role/' + roleId + '/permission',
    method: 'post',
    data: permissionList
  })
}

export function findMenuIdByRoleId(roleId) {
  return request({
    url: '/role/menu/' + roleId,
    method: 'get'
  })
}

export function allotMenu(roleId, menuList) {
  return request({
    url: '/role/menu/' + roleId,
    method: 'post',
    data: menuList
  })
}

export function findDeptTree() {
  return request({
    url: '/role/tree',
    method: 'get'
  })
}

export function deleteRole(roleId) {
  return request({
    url: '/role/' + roleId,
    method: 'delete'
  })
}

