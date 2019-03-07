import request from '@/utils/request'

export function login(userCode, password) {
  return request({
    url: '/user/login',
    method: 'post',
    data: {
      userCode,
      password
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/user/index',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
