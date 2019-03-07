import request from '@/utils/request'

export const antiType = {
  id: null,
  type: null,
  typeName: null,
  url: null,
  remark: null
}

export function getPages(listQuery) {
  return request({
    url: '/antiType/getPage',
    method: 'get',
    params: listQuery
  })
}

export function addAntiType(antiType) {
  return request({
    url: '/antiType/add',
    method: 'post',
    data: antiType
  })
}

export function updateAntiType(antiType) {
  return request({
    url: '/antiType/update',
    method: 'put',
    data: antiType
  })
}

export function deleteAntiType(id) {
  return request({
    url: '/antiType/delete/' + id,
    method: 'delete'
  })
}
