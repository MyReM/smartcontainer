import request from '@/utils/request'

export const antiFake = {
  id: null,
  epc: null,
  qrCode: null,
  typeId: null
}

export function getPages(listQuery) {
  return request({
    url: '/antiFake/getPage',
    method: 'get',
    params: listQuery
  })
}

export function addAntiFake(antiType) {
  return request({
    url: '/antiFake/add',
    method: 'post',
    data: antiType
  })
}

export function updateAntiFake(antiType) {
  return request({
    url: '/antiFake/update',
    method: 'put',
    data: antiType
  })
}

export function deleteAntiFake(id) {
  return request({
    url: '/antiFake/delete/' + id,
    method: 'delete'
  })
}

export function getAllAntiType() {
  return request({
    url: '/antiType/getAll/',
    method: 'get'
  })
}

export function getSerial() {
  return request({
    url: '/antiFake/getSerial',
    method: 'get'
  })
}
export function openNFC(param) {
  return request({
    url: '/antiFake/openNFC/' + param,
    method: 'get'
  })
}
export function closeNFC() {
  return request({
    url: '/antiFake/closeNFC/',
    method: 'get'
  })
}
export function searchNFC() {
  return request({
    url: '/antiFake/searchNFC',
    method: 'get'
  })
}
