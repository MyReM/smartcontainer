import { constantRouterMap, Layout } from '@/router'
import { menuMap, systemTree } from '@/api/system'

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param asyncRouterMap
 * @param roles
 */

function createRouter(menuTree) {
  const accessedRouters = Array.of()
  menuTree.forEach(ele => {
    if ((Array.prototype.isPrototypeOf(ele.children) && ele.children.length === 0) || ele.children === null) {
      let fullScreen = false
      if ((ele.fullScreen !== null || ele.fullScreen !== '') && ele.fullScreen) {
        fullScreen = true
      }
      const menu = {
        path: '/' + ele.menuCode,
        component: fullScreen ? menuMap[ele.menuValue] : Layout,
        redirect: '/' + ele.menuCode + '/' + ele.menuCode,
        name: ele.menuCode,
        hidden: (ele.hidden === true || ele.hidden === 'true'),
        alwaysShow: true,
        meta: { title: ele.menuName, icon: ele.icon },
        children: [{
          path: ele.menuCode,
          name: ele.menuCode + '/#index.html',
          meta: { title: ele.menuName, icon: ele.icon },
          component: menuMap[ele.menuValue]
        }]
      }
      accessedRouters.push(menu)
    } else if (ele.children.length > 0) {
      const children = subTree(ele.children)
      const menu = {
        path: '/' + ele.menuCode,
        component: Layout,
        redirect: '/' + ele.menuCode + '/' + children[0].name,
        name: ele.menuCode,
        // hidden: true,
        alwaysShow: true,
        meta: { title: ele.menuName, icon: ele.icon }
      }
      Object.assign(menu, { 'children': children })
      accessedRouters.push(menu)
    }
  })
  console.log(accessedRouters)
  return accessedRouters
}

function subTree(tree) {
  const menuTree = Array.of()
  tree.forEach((ele) => {
    if ((ele.children !== undefined) && ele.children.length > 0) {
      const children = {
        path: ele.menuCode,
        name: ele.menuCode,
        hidden: (ele.hidden === true || ele.hidden === 'true'),
        component: menuMap[ele.menuValue],
        meta: { title: ele.menuName, icon: ele.icon }
      }
      Object.assign(children, subTree(ele.children))
      menuTree.push(children)
    } else {
      menuTree.push({
        path: ele.menuCode,
        name: ele.menuCode,
        hidden: (ele.hidden === true || ele.hidden === 'true'),
        component: menuMap[ele.menuValue],
        meta: { title: ele.menuName, icon: ele.icon }
      })
    }
  })
  return menuTree
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        systemTree().then((res) => {
          const data = res.data
          let tree = Array.of()
          if (data !== null || !(Array.prototype.isPrototypeOf(data) && data.length === 0)) {
            tree = data
          }
          const accessedRouters = createRouter(tree)
          accessedRouters.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTERS', accessedRouters)
          resolve()
        })
      })
    }
  }
}

export default permission
