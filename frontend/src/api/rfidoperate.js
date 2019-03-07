import request from '@/utils/request'

export function getNonIn() {
  return request({
    url: '/rfid/getNonIn',
    method: 'get'
  })
}

export function openRFID() {
  return request({
    url: '/rfid/openRFID',
    method: 'get'
  })
}

export function getRFID() {
  return request({
    url: '/rfid/getRFID',
    method: 'get'
  })
}

export function closeRFID() {
  return request({
    url: '/rfid/close',
    method: 'get'
  })
}
