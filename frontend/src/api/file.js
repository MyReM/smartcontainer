import request from '@/utils/request'

export function findOne(fileId) {
  return request({
    url: '/file/' + fileId,
    method: 'get'
  })
}

export function download(fileName) {
  return request({
    url: '/file/download',
    method: 'post',
    data: { 'fileName': fileName },
    responseType: 'blob'
  })
}
