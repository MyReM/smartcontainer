import request from '@/utils/request'

export const smoke = {
  id: null,
  name: null,
  commoditiesType: null,
  madeDate: null,
  price: null,
  message: null,
  sum: null
}

export function getSmokePage(listQuery) {
  return request({
    url: '/smoke/getPage',
    method: 'get',
    params: listQuery
  })
}

export function getAllSmoke(drinks) {
  return request({
    url: '/smoke',
    method: 'get',
    params: drinks
  })
}

export function addSmoke(drinks) {
  return request({
    url: '/smoke/add',
    method: 'post',
    data: drinks
  })
}

export function updateSmoke(drinks) {
  return request({
    url: '/smoke/update',
    method: 'put',
    data: drinks
  })
}

export function deleteDrinks(id) {
  return request({
    url: '/smoke/delete/' + id,
    method: 'delete'
  })
}
