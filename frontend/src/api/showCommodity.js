import request from '@/utils/request'

export const showCommodity = {
  id: null,
  commoditiesName: null,
  commoditiesType: null,
  commoditiesTypeName: null,
  isShow: null,
  sum: null
}

export function getCommodityPage(listQuery) {
  return request({
    url: '/commodity/getPage',
    method: 'get',
    params: listQuery
  })
}

export function getAllCommodity(commodity) {
  return request({
    url: '/commodity',
    method: 'get',
    params: commodity
  })
}

export function addCommodity(param) {
  return request({
    url: '/commodity/add',
    method: 'post',
    data: param
  })
}

export function updateCommodity(param) {
  return request({
    url: '/commodity/update',
    method: 'put',
    data: param
  })
}

export function deleteCommodity(id) {
  return request({
    url: '/commodity/delete/' + id,
    method: 'delete'
  })
}
