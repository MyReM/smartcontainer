import request from '@/utils/request'

export const drinks = {
  id: null,
  name: null,
  commoditiesType: null,
  madeDate: null,
  degree: null,
  price: null,
  message: null,
  sum: null
}

export function getDrinksPage(listQuery) {
  return request({
    url: '/drinks/getPage',
    method: 'get',
    params: listQuery
  })
}

export function getAllDrinks(drinks) {
  return request({
    url: '/drinks',
    method: 'get',
    params: drinks
  })
}

export function addDrinks(drinks) {
  return request({
    url: '/drinks/add',
    method: 'post',
    data: drinks
  })
}

export function updateDrinks(drinks) {
  return request({
    url: '/drinks/update',
    method: 'put',
    data: drinks
  })
}

export function deleteDrinks(id) {
  return request({
    url: '/drinks/delete/' + id,
    method: 'delete'
  })
}
