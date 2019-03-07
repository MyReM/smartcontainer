import request from '@/utils/request'

export const menu = {
  menuId: null,
  menuCode: null,
  menuName: null,
  comment: null,
  status: 1,
  hidden: false,
  fullScreen: false,
  level: null,
  sortNum: null,
  icon: null,
  parentId: null
}

export function findOneMenu(deptId) {
  return request({
    url: '/menu/' + deptId,
    method: 'get'
  })
}

export function findAllMenu(params) {
  return request({
    url: '/menu',
    method: 'get',
    params: params
  })
}

export function insertMenu(menu) {
  return request({
    url: '/menu',
    method: 'post',
    data: menu
  })
}

export function updateMenu(menu) {
  return request({
    url: '/menu',
    method: 'put',
    data: menu
  })
}

export function findMenuTree() {
  return request({
    url: '/menu/tree',
    method: 'get'
  })
}

export function getParentId(menuId) {
  return request({
    url: '/menu/getParentId',
    method: 'get',
    params: { menuId: menuId }
  })
}

export function deleteMenu(deptId) {
  return request({
    url: '/menu/' + deptId,
    method: 'delete'
  })
}
