import Vue from 'vue'

import 'normalize.css/normalize.css'// A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import router from './router'
import store from './store'

import '@/icons' // icon
import '@/permission' // permission control

import permission from '@/directive/permission/index.js' // 权限判断指令

Vue.use(ElementUI, { locale })
Vue.use(permission)

import vuescroll from 'vuescroll'
import 'vuescroll/dist/vuescroll.css'
Vue.use(vuescroll)

import moment from 'moment'
Vue.prototype.$moment = moment

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
