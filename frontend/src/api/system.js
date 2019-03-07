import request from '@/utils/request'

export const menuMap = {
  'user': () => import('@/views/core/user/index'),
  'role': () => import('@/views/core/role/index'),
  'dept': () => import('@/views/core/dept/index'),
  'menu': () => import('@/views/core/menu/index'),
  'resource': () => import('@/views/core/resource/index'),
  'permission': () => import('@/views/core/permission/index'),
  'log': () => import('@/views/core/log/index'),
  'blank': () => import('@/views/blank/index'),
  'showMessage': () => import('@/views/model/showMessage'),
  'showMessages': () => import('@/views/model/showMessages'),
  'showCommodity': () => import('@/views/model/showCommodity'),
  'drinks': () => import('@/views/model/drinks'),
  'smoke': () => import('@/views/model/smoke'),
  'healthProducts': () => import('@/views/model/healthProducts'),
  'EPCMessage': () => import('@/views/model/EPCMessage'),
  'EPCEnum': () => import('@/views/model/EPCEnum')
}

export const viewMap = [
  { name: '空白页面', value: 'blank' },
  { name: '用户页面', value: 'user' },
  { name: '角色页面', value: 'role' },
  { name: '公司页面', value: 'dept' },
  { name: '菜单页面', value: 'menu' },
  { name: '资源页面', value: 'resource' },
  { name: '权限页面', value: 'permission' },
  { name: '日志页面', value: 'log' },
  { name: '商品展示信息', value: 'showMessage' },
  { name: '商品详细信息', value: 'showMessages' },
  { name: '商品展示管理', value: 'showCommodity' },
  { name: '酒类管理', value: 'drinks' },
  { name: '烟类管理', value: 'smoke' },
  { name: '保健品管理', value: 'healthProducts' },
  { name: 'EPC产品信息管理', value: 'EPCMessage' },
  { name: 'EPC参数设置', value: 'EPCEnum' }
]

export const iconList = [
  { name: 'dashboard', value: 'dashboard' },
  { name: 'example', value: 'example' },
  { name: 'eye', value: 'eye' },
  { name: 'form', value: 'form' },
  { name: 'password', value: 'password' },
  { name: 'table', value: 'table' },
  { name: 'tree', value: 'tree' },
  { name: 'user', value: 'user' },
  { name: 'system', value: 'system' },
  { name: 'printer', value: 'printer' }
]

export function systemTest() {
  return request({
    url: '/system/test',
    method: 'get'
  })
}

export function systemTree() {
  return request({
    url: '/system/tree',
    method: 'get'
  })
}

export function updatePassword() {
  return request({
    url: '/system/updatePassword',
    method: 'post'
  })
}
