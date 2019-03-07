import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    name: 'dashboard',
    // hidden: true,
    meta: { title: '首页', icon: 'dashboard' },
    children: [{
      path: 'dashboard',
      meta: { title: '首页', icon: 'dashboard' },
      component: () => import('@/views/dashboard/index')
    }]
  },

  // {
  //   path: '/system',
  //   component: Layout,
  //   redirect: '/system/user',
  //   name: 'system',
  //   meta: { title: '系统管理', icon: 'example' },
  //   children: [
  //     {
  //       path: 'user',
  //       name: 'user',
  //       component: () => import('@/views/user/index'),
  //       meta: { title: '用户管理', icon: 'user' }
  //     },
  //     {
  //       path: 'role',
  //       name: 'role',
  //       component: () => import('@/views/role/index'),
  //       meta: { title: '角色管理', icon: 'table' }
  //     },
  //     {
  //       path: 'dept',
  //       name: 'dept',
  //       component: () => import('@/views/dept/index'),
  //       meta: { title: '部门管理', icon: 'tree' }
  //     },
  //     {
  //       path: 'menu',
  //       name: 'menu',
  //       component: () => import('@/views/menu/index'),
  //       meta: { title: '菜单管理', icon: 'table' }
  //     },
  //     {
  //       path: 'resource',
  //       name: 'resource',
  //       component: () => import('@/views/resource/index'),
  //       meta: { title: '资源管理', icon: 'table' }
  //     },
  //     {
  //       path: 'permission',
  //       name: 'permission',
  //       component: () => import('@/views/permission/index'),
  //       meta: { title: '权限管理', icon: 'table' }
  //     },
  //     {
  //       path: 'druid',
  //       name: 'druid',
  //       component: () => import('@/views/monitor/druid'),
  //       meta: { title: '监控管理', icon: 'table' }
  //     },
  //     {
  //       path: 'log',
  //       name: 'log',
  //       component: () => import('@/views/log/index'),
  //       meta: { title: '日志管理', icon: 'tree' }
  //     },
  //     {
  //       path: 'test',
  //       name: 'test',
  //       component: () => import('@/views/test/index'),
  //       meta: { title: '系统检测', icon: 'tree' }
  //     },
  //     {
  //       path: 'password',
  //       name: 'password',
  //       hidden: true,
  //       component: () => import('@/views/user/password'),
  //       meta: { title: '修改密码', icon: 'tree' }
  //     }
  //   ]
  // },
  // {
  //   path: '/label',
  //   component: Layout,
  //   meta: { title: '标签管理', icon: 'example' },
  //   children: [
  //     {
  //       path: 'label',
  //       name: 'label',
  //       component: () => import('@/views/label/label/'),
  //       meta: { title: '标签管理' }
  //     },
  //     {
  //       path: 'template',
  //       name: 'template',
  //       hidden: true,
  //       component: () => import('@/views/label/template/'),
  //       meta: { title: '模板管理' }
  //     },
  //     {
  //       path: 'printer',
  //       name: 'printer',
  //       component: () => import('@/views/label/printer/'),
  //       meta: { title: '打印机管理' }
  //     }
  //   ]
  // },
  // {
  //   path: '/print',
  //   component: Layout,
  //   meta: { title: '打印管理', icon: 'example' },
  //   children: [
  //     {
  //       path: 'instructions',
  //       name: 'instructions',
  //       component: () => import('@/views/label/instructions'),
  //       meta: { title: '使用说明', icon: 'tree' }
  //     },
  //     {
  //       path: 'template',
  //       name: 'template',
  //       component: () => import('@/views/template/index'),
  //       meta: { title: '模板管理', icon: 'tree' }
  //     },
  //     {
  //       path: 'template/param',
  //       name: 'template/param',
  //       hidden: true,
  //       component: () => import('@/views/template/param'),
  //       meta: { title: '模板参数', icon: 'tree' }
  //     },
  //     {
  //       path: 'printer',
  //       name: 'printer',
  //       component: () => import('@/views/printer/index'),
  //       meta: { title: '打印机管理', icon: 'form' }
  //     },
  //     {
  //       path: 'label',
  //       name: 'label',
  //       component: () => import('@/views/label/index'),
  //       meta: { title: '标签打印', icon: 'form' }
  //     },
  //     {
  //       path: 'label/add',
  //       name: 'labelAdd',
  //       hidden: true,
  //       component: () => import('@/views/label/add'),
  //       meta: { title: '标签添加', icon: 'form' }
  //     },
  //     {
  //       path: 'label/print',
  //       name: 'labelPrint',
  //       hidden: true,
  //       component: () => import('@/views/label/print'),
  //       meta: { title: '标签打印', icon: 'form' }
  //     },
  //     {
  //       path: 'label/scan',
  //       name: 'labelScan',
  //       component: () => import('@/views/label/scan'),
  //       meta: { title: '标签补漏', icon: 'form' }
  //     }
  //   ]
  // },

  { path: '/mobile/home', component: () => import('@/mobile/home/index') }

  // { path: '*', redirect: '/404', hidden: true }
]

export { Layout }

export default new Router({
  // mode: 'history', // 后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

