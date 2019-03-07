import request from '@/utils/request'

export const commoditiesCode = {
  id: null,
  commoditiesCode: null,
  commoditiesType: null,
  isIn: null,
  uploadFileName: null,
  sufName: null,
  src: null,
  imgId: null,
  creatrTime: null
}

export function getCommoditiesCodePage(listQuery) {
  return request({
    url: '/commoditiesCode/getPage',
    method: 'get',
    params: listQuery
  })
}

export function getCommoditiesCode(updateCommoditiesCode) {
  return request({
    url: '/commoditiesCode',
    method: 'get',
    params: updateCommoditiesCode
  })
}

export function addCommoditiesCode(updateCommoditiesCode) {
  return request({
    url: '/commoditiesCode/add',
    method: 'post',
    data: updateCommoditiesCode
  })
}

export function updateCommoditiesCode(updateCommoditiesCode) {
  return request({
    url: '/commoditiesCode/update',
    method: 'put',
    data: updateCommoditiesCode
  })
}

export function deleteCommoditiesCode(id) {
  return request({
    url: '/commoditiesCode/delete/' + id,
    method: 'delete'
  })
}

export function findAllImgByCommoditiesType(commoditiesType) {
  return request({
    url: 'getImg?commoditiesType=' + commoditiesType,
    method: 'get'
  })
}

export function delImgBySrc(src, id) {
  return request({
    url: 'deleteImgBySrc',
    method: 'delete',
    params: {
      'src': src,
      'id': id
    }
  })
}
