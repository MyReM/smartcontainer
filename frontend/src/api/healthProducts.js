import request from '@/utils/request'

export const healthProducts = {
  id: null,
  name: null,
  commoditiesType: null,
  madeDate: null,
  overDate: null,
  madeData: null,
  effect: null,
  price: null,
  message: null,
  sum: null
}

export function getHealthProductsPage(listQuery) {
  return request({
    url: '/healthProducts/getPage',
    method: 'get',
    params: listQuery
  })
}

export function getAllHealthProducts(healthProducts) {
  return request({
    url: '/healthProducts',
    method: 'get',
    params: healthProducts
  })
}

export function addHealthProducts(healthProducts) {
  return request({
    url: '/healthProducts/add',
    method: 'post',
    data: healthProducts
  })
}

export function updateHealthProducts(healthProducts) {
  return request({
    url: '/healthProducts/update',
    method: 'put',
    data: healthProducts
  })
}

export function deleteHealthProducts(id) {
  return request({
    url: '/healthProducts/delete/' + id,
    method: 'delete'
  })
}
