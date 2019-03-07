import request from '@/utils/request'

export const commodity = {
  id: null,
  commodityName: null, // 商品名稱
  commoditiesType: null, // 商品类型
  commoditiesTypeName: null, // 商品类型名称
  isShow: null // 是否显示商品
}

export const drink = {
  name: null,
  commoditiesType: null,
  commoditiesTypeName: null,
  madeDate: null,
  degree: null,
  price: null,
  sum: null,
  totalNumber: null,
  message: null,
  img: null
}

export const healthProduct = {
  name: null,
  commoditiesType: null,
  commoditiesTypeName: null,
  madeDate: null,
  overDate: null,
  madeData: null,
  effect: null,
  price: null,
  sum: null,
  totalNumber: null,
  message: null,
  img: null
}

export const smoke = {
  name: null,
  commoditiesType: null,
  commoditiesTypeName: null,
  madeDate: null,
  price: null,
  sum: null,
  totalNumber: null,
  message: null,
  img: null
}
export function getAllCommodity(params) {
  return request({
    url: '/commodity',
    method: 'get',
    params: params
  })
}
export function getCommodity(param) {
  return request({
    url: '/getCommodity',
    method: 'post',
    data: param
  })
}
